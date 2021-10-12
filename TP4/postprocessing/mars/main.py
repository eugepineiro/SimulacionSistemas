import json, math
from re import A

from plotter import plot_energies_per_time, plot_distance_per_date, plot_spaceship_velocity_per_frame, plot_spaceship_arrival_time_per_velocity
from utils import get_min_distances, calculate_distance, get_arrival_time, get_planet_and_spaceship_positions_by_key

GRAVITY_CONSTANT = 6.693 * (10**(-20))

MARS_RADIUS = 3389.92 # km
MAX_MARS_ORBIT_TOLERANCE = 1000

JUPITER_RADIUS = 69911 # km
MAX_JUPITER_ORBIT_TOLERANCE = 10000

planet_name = input("Enter Planet Name (mars/jupiter): ")
planet_name = planet_name.lower()
if planet_name != 'mars' and planet_name != 'jupiter': 
    planet_name = 'mars' 
planet_radius = MARS_RADIUS if planet_name == 'mars' else JUPITER_RADIUS
max_planet_orbit_tolerance = MAX_MARS_ORBIT_TOLERANCE if planet_name == 'mars' else MAX_JUPITER_ORBIT_TOLERANCE

print(f"Runnning {planet_name.capitalize()} Postprocessing")

with open("../../src/main/resources/config/config.json") as f:
    config = json.load(f)

with open(f"../../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_{planet_name}_results_with_multiple_dt.json") as f:
    results_with_multiple_dt = json.load(f)

with open(f"../../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_{planet_name}_results_with_multiple_dates.json") as f:
    results_with_multiple_dates = json.load(f)

with open(f"../../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_{planet_name}_results_with_multiple_velocities.json") as f:
    results_with_multiple_velocities_wrapper = json.load(f)

planet_name = planet_name.upper()

############# Energy Variation Check #############

def kinetic_energy(particles):
    e = 0
    for p in particles:
        e += (1.0/2) * p['mass'] * (p['vx']**2 + p['vy']**2)  # 1/2 m * v^2
    return e

def potential_energy(particles):
    e = 0
    for i in range(len(particles)):
        p_i = particles[i]
        for j in range(i+1, len(particles)):
            p_j = particles[j]
            e += - GRAVITY_CONSTANT * (p_i['mass'] * p_j['mass']) / calculate_distance(p_i, p_j)
    return e

times_by_dt = {}
energies_by_dt = {}

for dt in results_with_multiple_dt:
    times_by_dt[dt]       = list(map(lambda f: f['time'], results_with_multiple_dt[dt]))
    energies_by_dt[dt]    = list(map(lambda f: kinetic_energy(f['particles']) + potential_energy(f['particles']), results_with_multiple_dt[dt]))

last_time = times_by_dt['2400.0'][-1]

for key in energies_by_dt.keys(): 
    max_dt_len = 0
    for i, t in enumerate(times_by_dt[key]):
        if t < last_time:
            max_dt_len = i
    energies_by_dt[key] =  energies_by_dt[key][:max_dt_len]
    times_by_dt[key] = times_by_dt[key][:max_dt_len]

plot_energies_per_time(times_by_dt, energies_by_dt)

############# EJ 1.a ############# 

dates = list(results_with_multiple_dates.keys()) 
 
planet_positions_by_date, spaceship_positions_by_date =  get_planet_and_spaceship_positions_by_key(dates, results_with_multiple_dates, planet_name)

min_distances_by_date = get_min_distances(planet_positions_by_date, spaceship_positions_by_date)  

plot_distance_per_date(min_distances_by_date, config['dt'])  

##############  EJ 1.b ############# 
 
best_launch_date = None
min_distance = 0

for date in min_distances_by_date:
    distance = min_distances_by_date[date]
    if best_launch_date is None or distance < min_distance:
        min_distance = distance
        best_launch_date = date

frames = list(map(lambda f: f['particles'], results_with_multiple_dates[best_launch_date]))
spaceship_frames = list(map(
        lambda f: list(filter(lambda p: p['type'] == 'SPACESHIP', f))[0], 
        frames
    ))

spaceship_velocities = list(map(
    lambda p: math.sqrt(math.pow(p['vx'], 2) + math.pow(p['vy'], 2)),
    spaceship_frames
))  

times = list(map(lambda f: f['time'], results_with_multiple_dates[best_launch_date]))


plot_spaceship_velocity_per_frame(spaceship_velocities, times, best_launch_date, config['dt'] )

# min distance arrival time

best_date_planet_positions = planet_positions_by_date[best_launch_date]
best_date_spaceship_positions = spaceship_positions_by_date[best_launch_date]

idx = 0
while idx < len(best_date_planet_positions):
    dist = calculate_distance(best_date_planet_positions[idx], best_date_spaceship_positions[idx])
    if dist == min_distance: # found
        break
    idx += 1

arrival_time = results_with_multiple_dates[best_launch_date][idx]['time']

print(f'Minima distancia: {min_distance}')

print(f'Distancia entre marte y la nave: {min_distance - planet_radius if (min_distance - planet_radius > 0) else "Nave dentro de marte"}')

print(f'Fecha de salida: {best_launch_date}')
print(f'Tiempo de arribo: {arrival_time} segundos ({(1.0 * arrival_time / 3600):.2f} hours - {(1.0 * arrival_time / (3600*24)):.2f} days)')

##############  EJ 1.c #############

for particle in  results_with_multiple_dates[best_launch_date][idx]['particles']:
    if particle['type'] == 'SPACESHIP': 
        spaceship_at_arrival = particle
    elif particle['type'] == planet_name:
        planet_at_arrival = particle

vr_x = spaceship_at_arrival['vx'] - planet_at_arrival['vx']
vr_y = spaceship_at_arrival['vy'] - planet_at_arrival['vy']

print(f'Velocidad relativa en x: {vr_x} km/s, Velocidad relativa en y: {vr_y} km/s')
print(f'Modulo de la velocidad relativa: {math.sqrt(vr_x**2 + vr_y**2)}')

##############  EJ 2 #############
planet_results_with_multiple_velocities = results_with_multiple_velocities_wrapper['map']
velocities = list(planet_results_with_multiple_velocities.keys())

planet_positions_by_velocity, spaceship_positions_by_velocity =  get_planet_and_spaceship_positions_by_key(velocities, planet_results_with_multiple_velocities, planet_name)

min_distances_by_velocity = get_min_distances(planet_positions_by_velocity, spaceship_positions_by_velocity)  

arrival_times = []

max_dist_to_consider = planet_radius + max_planet_orbit_tolerance

double_velocities = sorted(list(map(lambda s: float(s), velocities)))
filtered_velocities = []

for vel_n in double_velocities:
    vel = str(vel_n)
    # print(f'{vel} -> {min_distances_by_velocity[vel]} comparing to {max_dist_to_consider}')
    if min_distances_by_velocity[vel] <= max_dist_to_consider:
        arrival_time = get_arrival_time(planet_positions_by_velocity[vel], spaceship_positions_by_velocity[vel], planet_results_with_multiple_velocities[vel], min_distances_by_velocity[vel])
        arrival_times.append(arrival_time)
        filtered_velocities.append(vel_n)

plot_spaceship_arrival_time_per_velocity(filtered_velocities, arrival_times, results_with_multiple_velocities_wrapper['date'], config['dt'], config['dt']*config['save_factor'])