import json, math
from re import A 

from plotter import plot_distance_per_date, plot_spaceship_velocity_per_frame, plot_spaceship_arrival_time_per_velocity
from utils import get_min_distances, calculate_distance, get_arrival_time, get_planet_and_spaceship_positions_by_key

planet_name = input("Enter Planet Name (mars/jupiter): ")
planet_name = planet_name.lower()
if planet_name != 'mars' and planet_name != 'jupiter': 
    planet_name = 'mars'

print(f"Runnning {planet_name} Postprocessing")

with open(f"../../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_{planet_name}_results_with_multiple_dates.json") as f:
    results_with_multiple_dates = json.load(f)

with open(f"../../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_{planet_name}_results.json") as f:
    planet_results = json.load(f)

with open("../../src/main/resources/config/config.json") as f:
    config = json.load(f)

with open(f"../../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_{planet_name}_results_with_multiple_velocities.json") as f:
    results_with_multiple_velocities_wrapper = json.load(f)

planet_name = planet_name.upper()

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
print(f'{arrival_time} segundos')

print(f'Minima distancia: {min_distance}')

planet_radius = 3389.92 # km

print(f'Radio de marte: {planet_radius}')

print(f'Distancia entre marte y la nave: {min_distance - planet_radius}')


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

for vel in velocities: 
    arrival_time = get_arrival_time(planet_positions_by_velocity[vel], spaceship_positions_by_velocity[vel], planet_results_with_multiple_velocities[vel], min_distances_by_velocity[vel])
    arrival_times.append(arrival_time)

double_velocities = list(map(lambda s: float(s), velocities))

plot_spaceship_arrival_time_per_velocity(double_velocities, arrival_times, results_with_multiple_velocities_wrapper['date'], config['dt'], config['dt']*config['save_factor'])