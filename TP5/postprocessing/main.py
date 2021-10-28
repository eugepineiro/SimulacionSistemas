import json
import numpy as np
from plotter import plot_evacuated_particles_by_time,plot_avg_times_by_evacuated_particles

with open("../src/main/resources/postprocessing/SdS_TP5_2021Q2G01_multiple_simulations_results.json") as f:
    multiple_simulations_results = json.load(f)

# Exercise a

evacuated_particles_by_simulation = []

dt = multiple_simulations_results[0]['dt']
max_t = max(list(map( lambda sim: sim['escapeTimes'][-1], multiple_simulations_results)))
print(max_t)

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