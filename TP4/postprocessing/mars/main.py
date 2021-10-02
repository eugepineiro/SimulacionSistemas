import json, math

from plotter import plot_distance_per_date, plot_spaceship_velocity_per_frame
from utils import get_min_distances

with open("../../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_mars_results_with_multiple_dates.json") as f:
    mars_results_with_multiple_dates = json.load(f)

with open("../../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_mars_results.json") as f:
    mars_results = json.load(f)


dates = list(mars_results_with_multiple_dates.keys()) 

 
# fecha 1 : mars [ [x,y] , [x,y], {x,y} ] ,  sp [ [x,y] , [x,y], {x,y} ]
# results_dict[fecha] = []
# results_dict[fecha].append(posi)

# TODO filter by particle type
print(len(dates)) 
mars_positions_by_date = {}
spaceship_positions_by_date = {}

# EJ 1.a 

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

print(len(mars_positions_by_date.keys()))


min_distances = get_min_distances(mars_positions_by_date, spaceship_positions_by_date)  

plot_distance_per_date(min_distances)  

# EJ 1.b 
 
# frames = list(map(lambda f: f['particles'], mars_results))
# spaceship_frames = list(map(
#         lambda f: list(filter(lambda p: p['type'] == 'SPACESHIP', f))[0], 
#         frames
#     ))
# spaceship_velocities = list(map(
#     lambda p: math.sqrt(math.pow(p['vx'], 2) + math.pow(p['vy'], 2)),
#     spaceship_frames
# )) 

# times = list(map(lambda f: f['time'], spaceship_frames))

# plot_spaceship_velocity_per_frame(spaceship_velocities, times)