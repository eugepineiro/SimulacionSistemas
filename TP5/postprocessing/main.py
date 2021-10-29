import json
import numpy as np
from plotter import plot_evacuated_particles_by_time, plot_avg_times_by_evacuated_particles, plot_avg_flow_rate_by_target_width

with open("../src/main/resources/postprocessing/SdS_TP5_2021Q2G01_multiple_simulations_results.json") as f:
    multiple_simulations_results = json.load(f)

with open("../src/main/resources/postprocessing/SdS_TP5_2021Q2G01_multiple_width_and_particles_results.json") as f:
    multiple_width_and_particles_results = json.load(f)

# Exercise a

evacuated_particles_by_simulation = []

dt = multiple_simulations_results[0]['dt']
max_t = max(list(map( lambda sim: sim['escapeTimes'][-1], multiple_simulations_results)))
 

times = list(np.arange(0, max_t+dt, step=dt))

evacuated_particles_by_simulation = list(map( 
    lambda sim: list(map(
        lambda t: len(
            list(filter(lambda et: et <= t, sim['escapeTimes']))
        ), times
    )), multiple_simulations_results
))

# for simulation in multiple_simulations_results:
#     print(simulation['escapeTimes'][-1])

# print(times)
# print(evacuated_particles_by_simulation)

plot_evacuated_particles_by_time(evacuated_particles_by_simulation, times)

# Exercise b

evacuated_particles_by_simulation_np = np.array(evacuated_particles_by_simulation)
mean = np.mean(evacuated_particles_by_simulation_np, axis=1)
std = np.std(evacuated_particles_by_simulation_np, axis=1)
plot_avg_times_by_evacuated_particles(mean, times)

# Exercise c

def get_flow_rate(simulation):
    escape_times = simulation['escapeTimes']
    # first approach
    return len(escape_times) / (escape_times[-1]*simulation['targetWidth'])

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

for pair_simulations in multiple_width_and_particles_results:

    d = pair_simulations[0]['targetWidth']
    n = len(pair_simulations[0]['escapeTimes'])

    target_widths.append(d)
    number_of_particles.append(n)

    flow_rates_by_pairs.append(list(map(lambda sim: get_flow_rate(sim), pair_simulations)))

plot_avg_flow_rate_by_target_width(flow_rates_by_pairs, number_of_particles, target_widths)