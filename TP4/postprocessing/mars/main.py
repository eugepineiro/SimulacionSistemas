import json

from TP4.postprocessing.mars.plotter import plot_distance_per_date

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

distances = []


plot_distance_per_date(dates, distances)