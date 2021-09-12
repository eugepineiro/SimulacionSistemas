import json
from plotter import plot_time_probability_distribution, plot_speed_probability_distribution
from json_parser import parse_extended_events

# with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results.json") as f:
#     results_raw = json.load(f)
#     results = parse_extended_events(results_raw)

with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results_multiple_n_event_times.json") as f:
    times_and_n = json.load(f)

with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results_multiple_n_speeds.json") as f:
    speeds_and_n = json.load(f)

# [
#     {N, times},
#     {N, times}
# ]

# 3.1 Plot Times
times_by_n = list(map(lambda a: a['results'], times_and_n))
n_array = list(map(lambda a: a['n'], times_and_n))
plot_time_probability_distribution(times_by_n, n_array) 

# 3.2 Plot Speeds
speeds_by_n = list(map(lambda a: a['results'], times_and_n))
n_array = list(map(lambda a: a['n'], times_and_n))
plot_speed_probability_distribution(speeds_by_n, n_array)
