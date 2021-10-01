import json

from TP4.postprocessing.mars.plotter import plot_distance_per_date
from TP4.postprocessing.mars.utils import get_min_distances

with open("../../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_mars_results_with_multiple_dates.json") as f:
    mars_results_with_multiple_dates = json.load(f)

dates = list(map(
    lambda res: list(map(lambda p: p['date'], res['results'])),
    mars_results_with_multiple_dates
)) 

positions = list(map(
    lambda res: list(map(lambda p: [ p['particles'][0]['x'], p['particles'][0]['y'] ], res['results'])),
    mars_results_with_multiple_dates
))

# TODO filter by particle type
print(len(dates))
print(len(positions))

# distances = get_min_distances(positions)

# plot_distance_per_date(dates, distances)