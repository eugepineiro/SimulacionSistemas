import json
from plotter import plot_time_probability_distribution
from json_parser import parse

with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results.json") as f:
    results_raw = json.load(f)
    results = parse(results_raw)

events = list(map(lambda ev: ev.event, results))

plot_time_probability_distribution(events)
#plot_probability_distribution(speeds, 'speed')
