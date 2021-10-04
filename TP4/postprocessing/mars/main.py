import json, math
from re import A

from plotter import plot_distance_per_date, plot_spaceship_velocity_per_frame
from utils import get_min_distances, calculate_distance

with open("../../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_mars_results_with_multiple_dates.json") as f:
    mars_results_with_multiple_dates = json.load(f)

with open("../../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_mars_results.json") as f:
    mars_results = json.load(f)

with open("../../src/main/resources/config/config.json") as f:
    config = json.load(f)

############# EJ 1.a ############# 

dates = list(mars_results_with_multiple_dates.keys()) 

mars_positions_by_date = {}
spaceship_positions_by_date = {}

for date in dates:
    frames_particles = list(map(lambda f: f['particles'], mars_results_with_multiple_dates[date]))
    mars_frames = list(map(
        lambda f: list(filter(lambda p: p['type'] == 'MARS', f))[0], 
        frames_particles
    ))
    mars_positions = list(map(
        lambda p: {'x': p['x'], 'y': p['y']},
        mars_frames
    ))
    mars_positions_by_date[date] = mars_positions
    spaceship_frames = list(map(
        lambda f: list(filter(lambda p: p['type'] == 'SPACESHIP', f))[0], 
        frames_particles
    ))
    spaceship_positions = list(map(
        lambda p: {'x': p['x'], 'y': p['y']},
        spaceship_frames
    ))
    spaceship_positions_by_date[date] = spaceship_positions

min_distances_by_date = get_min_distances(mars_positions_by_date, spaceship_positions_by_date)  

plot_distance_per_date(min_distances_by_date, config['dt'])  

##############  EJ 1.b ############# 
 


best_launch_date = None
min_distance = 0

for date in min_distances_by_date:
    distance = min_distances_by_date[date]
    if best_launch_date is None or distance < min_distance:
        min_distance = distance
        best_launch_date = date

frames = list(map(lambda f: f['particles'], mars_results_with_multiple_dates[best_launch_date]))
spaceship_frames = list(map(
        lambda f: list(filter(lambda p: p['type'] == 'SPACESHIP', f))[0], 
        frames
    ))

spaceship_velocities = list(map(
    lambda p: math.sqrt(math.pow(p['vx'], 2) + math.pow(p['vy'], 2)),
    spaceship_frames
))  

times = list(map(lambda f: f['time'], mars_results_with_multiple_dates[best_launch_date]))


plot_spaceship_velocity_per_frame(spaceship_velocities, times, best_launch_date, config['dt'] )

# min distance arrival time

best_date_mars_positions = mars_positions_by_date[best_launch_date]
best_date_spaceship_positions = spaceship_positions_by_date[best_launch_date]

idx = 0
while idx < len(best_date_mars_positions):
    dist = calculate_distance(best_date_mars_positions[idx], best_date_spaceship_positions[idx])
    if dist == min_distance: # found
        break
    idx += 1

arrival_time = mars_results_with_multiple_dates[best_launch_date][idx]['time']
print(f'{arrival_time} segundos')

print(min_distance)

mars_radius = 3389.92 # km

print(mars_radius)

print(f'Distancia entre marte y la nave: {min_distance - mars_radius}')


##############  EJ 1.c #############

mars_at_arrival = mars_positions_by_date[best_launch_date][idx]
spaceship_at_arrival = mars_positions_by_date[best_launch_date][idx]

vr_x = spaceship_at_arrival['vx'] - mars_at_arrival['vx']
vr_y = spaceship_at_arrival['vy'] - mars_at_arrival['vy']

print(f'Velocidad relativa en x: {vr_x} km/s, Velocidad relativa en y: {vr_y} km/s')
print(f'Modulo de la velocidad relativa: {math.sqrt(vr_x**2 + vr_y**2)}')