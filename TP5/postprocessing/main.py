import json
import numpy as np
from plotter import plot_evacuated_particles_by_time, plot_avg_times_by_evacuated_particles, plot_avg_times_by_evacuated_particles_inverted, plot_avg_flow_rate_by_target_width, plot_flow_rate_by_time_with_multiple_simulations

with open("../src/main/resources/postprocessing/SdS_TP5_2021Q2G01_multiple_simulations_results.json") as f:
    multiple_simulations_results = json.load(f)

with open("../src/main/resources/postprocessing/SdS_TP5_2021Q2G01_multiple_width_and_particles_results.json") as f:
    multiple_width_and_particles_results = json.load(f)

WINDOW_SIZE = 5

# Exercise a

dt = multiple_simulations_results[0]['dt']
max_t = max(list(map( lambda sim: sim['escapeTimes'][-1], multiple_simulations_results)))
 
times = list(np.arange(0, max_t+dt, step=dt))

evacuated_particles = [i+1 for i in range(len(multiple_simulations_results[0]['escapeTimes']))]

times_by_simulation = list(map(
    lambda sim: sim['escapeTimes'],
    multiple_simulations_results
))

plot_evacuated_particles_by_time(evacuated_particles, times_by_simulation)

# Exercise b

plot_avg_times_by_evacuated_particles(evacuated_particles, times_by_simulation)

plot_avg_times_by_evacuated_particles_inverted(evacuated_particles, times_by_simulation)

# Exercise c

def get_flow_rates_by_time(times_by_simulation, window_size):

    times_by_simulation_np = np.array(times_by_simulation)
    mean = np.mean(times_by_simulation_np, axis=0)

    escape_times = []
    flow_rates = []
    
    c = len(mean)
    for i in range(0, c, window_size):
        n = window_size - 1 if (c - i) >= window_size else (c - i) - 1 # TODO(mati): revisar el else
        escape_times.append((mean[i+n] + mean[i]) / 2)
        flow_rates.append(n/(mean[i+n] - mean[i]))

    return escape_times, flow_rates 

target_widths = []
number_of_particles = []
flow_rates_by_pairs = []
stationary_flow_rates_by_pairs = []

# [
#     // n1, d1
#         [// fs1
#         // fs2
#         // fs3
#         ...]
#     // n2, d2
# ]

time_to_start_averaging = 20
time_to_finish_averaging = float('inf')

for pair_simulations in multiple_width_and_particles_results:

    d = pair_simulations[0]['targetWidth']
    n = len(pair_simulations[0]['escapeTimes'])

    target_widths.append(d)
    number_of_particles.append(n)

    # c

    evacuated_particles = [i+1 for i in range(n)]

    times_by_simulation = list(map(
        lambda sim: sim['escapeTimes'],
        pair_simulations
    ))

    flow_rates_by_pairs.append(get_flow_rates_by_time(times_by_simulation, WINDOW_SIZE))

plot_flow_rate_by_time_with_multiple_simulations(flow_rates_by_pairs, number_of_particles, target_widths)

averaging_limits_by_n = {
    '200': [7, 63],
    '260': [5, 58],
    '320': [4, 56],
    '380': [3.5, 55]
}

plot_avg_flow_rate_by_target_width(flow_rates_by_pairs, averaging_limits_by_n, number_of_particles, target_widths)