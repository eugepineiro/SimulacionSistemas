import json

from plotter import plot_distance_per_date
from utils import get_min_distances

with open("../../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_mars_results_with_multiple_dates.json") as f:
    mars_results_with_multiple_dates = json.load(f)
 


dates = list(mars_results_with_multiple_dates.keys()) 

 
# fecha 1 : mars [ [x,y] , [x,y], {x,y} ] ,  sp [ [x,y] , [x,y], {x,y} ]
# results_dict[fecha] = []
# results_dict[fecha].append(posi)

# TODO filter by particle type
print(len(dates)) 
mars_positions_by_date = {}
spaceship_positions_by_date = {}

for date in dates:
    frames = list(map(lambda f: f['particles'], mars_results_with_multiple_dates[date]))
    mars_frames = list(map(
        lambda f: list(filter(lambda p: p['type'] == 'MARS', f))[0], 
        frames
    ))
    mars_positions = list(map(
        lambda p: {'x': p['x'], 'y': p['y']},
        mars_frames
    ))
    mars_positions_by_date[date] = mars_positions
    spaceship_frames = list(map(
        lambda f: list(filter(lambda p: p['type'] == 'SPACESHIP', f))[0], 
        frames
    ))
    spaceship_positions = list(map(
        lambda p: {'x': p['x'], 'y': p['y']},
        spaceship_frames
    ))
    spaceship_positions_by_date[date] = spaceship_positions

print(len(mars_positions_by_date.keys()))


min_distances = get_min_distances(mars_positions_by_date, spaceship_positions_by_date)  

plot_distance_per_date(min_distances)  