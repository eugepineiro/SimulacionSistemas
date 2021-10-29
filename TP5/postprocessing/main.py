import json
import numpy as np
from plotter import plot_evacuated_particles_by_time, plot_avg_times_by_evacuated_particles, plot_avg_times_by_evacuated_particles_inverted, plot_avg_flow_rate_by_target_width, plot_flow_rate_by_time_with_multiple_simulations

with open("../src/main/resources/postprocessing/SdS_TP5_2021Q2G01_multiple_simulations_results.json") as f:
    multiple_simulations_results = json.load(f)

with open("../src/main/resources/postprocessing/SdS_TP5_2021Q2G01_multiple_width_and_particles_results.json") as f:
    multiple_width_and_particles_results = json.load(f)

# Exercise a

evacuated_particles_by_simulation = []

dt = multiple_simulations_results[0]['dt']
max_t = max(list(map( lambda sim: sim['escapeTimes'][-1], multiple_simulations_results)))
 
times = list(np.arange(0, max_t+dt, step=dt))

times_by_simulation = list(map(
    lambda sim: sim['escapeTimes'],
    multiple_simulations_results
))

evacuated_particles_by_simulation = list(map( 
    lambda sim: list(map(
        lambda t: len(
            list(filter(lambda et: et <= t, sim['escapeTimes']))
        ), sim['escapeTimes']
    )), multiple_simulations_results
))

# for simulation in multiple_simulations_results:
#     print(simulation['escapeTimes'][-1])

# print(times)
# print(evacuated_particles_by_simulation)

plot_evacuated_particles_by_time(evacuated_particles_by_simulation, times_by_simulation)

# Exercise b

plot_avg_times_by_evacuated_particles(evacuated_particles_by_simulation, times_by_simulation)

plot_avg_times_by_evacuated_particles_inverted(evacuated_particles_by_simulation, times_by_simulation)

# Exercise c

def get_flow_rates_by_time(simulation):
    escape_times = simulation['escapeTimes']
    target_width = simulation['targetWidth']

    flow_rates = []
    for i in range(len(escape_times)):
        escaped_particles = i+1
        flow_rates.append(escaped_particles / (escape_times[i] * target_width))

    return escape_times, flow_rates 

target_widths = []
number_of_particles = []
flow_rates_by_pairs = []
 

# [
#     // n1, d1
#         [// fs1
#         // fs2
#         // fs3
#         ...]
#     // n2, d2
# ]

# escape_times, flow_rates = get_flow_rates_by_time(multiple_width_and_particles_results[0][0])

sims = list(map(lambda sim: get_flow_rates_by_time(sim), multiple_width_and_particles_results[0]))

plot_flow_rate_by_time_with_multiple_simulations(sims, multiple_width_and_particles_results[0][0]['targetWidth'], len(multiple_width_and_particles_results[0][0]['escapeTimes']))

time_to_start_averaging = 20
time_to_finish_averaging = float('inf')

for pair_simulations in multiple_width_and_particles_results:

    d = pair_simulations[0]['targetWidth']
    n = len(pair_simulations[0]['escapeTimes'])

    target_widths.append(d)
    number_of_particles.append(n)

    sims = list(map(lambda sim: get_flow_rates_by_time(sim), pair_simulations))
    
    flow_rate_means = []
    for escape_times, flow_rates in sims:
        mean = 0
        c = 0
        for i in range(0, len(escape_times)):
            et = escape_times[i]
            fr = flow_rates[i]
            if (et > time_to_start_averaging) and (et < time_to_finish_averaging):
                mean += fr
                c += 1
        mean /= c

        flow_rate_means.append(mean)

    flow_rates_by_pairs.append(flow_rate_means)

plot_avg_flow_rate_by_target_width(flow_rates_by_pairs, number_of_particles, target_widths)