import json
from plotter import plot_big_particle_trajectories, plot_dcm, plot_speed_probability_distribution_initial_time, plot_time_probability_distribution, plot_speed_probability_distribution
from json_parser import parse_extended_events
import numpy as np

# with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results.json") as f:
#     results_raw = json.load(f)
#     results = parse_extended_events(results_raw)

with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results_multiple_n_event_times.json") as f:
    times_and_n = json.load(f)

with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results_multiple_n_small_particles_speeds.json") as f:
    speeds_and_n = json.load(f)

# with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results_multiple_temperatures_big_particle_positions.json") as f:
#     trajectories_and_speeds = json.load(f)

# with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results_multiple_simulations_big_particle_positions.json") as f:
#     simulations_big_trajectories_results = json.load(f)

# with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results_small_particles_positions.json") as f:
#     small_trajectories_results = json.load(f)


############# 3.1 Plot Times #############
if not times_and_n is None:
    times_by_n = list(map(lambda a: a['results'], times_and_n))
    n_array = list(map(lambda a: a['n'], times_and_n))

    plot_time_probability_distribution(times_by_n, n_array) 

############## 3.2 Plot Speeds ############# 
if not speeds_and_n is None:
    speeds_by_n = list(map(lambda a: a['results'], speeds_and_n))
    n_array = list(map(lambda a: a['n'], speeds_and_n))
    # plot_speed_probability_distribution(speeds_by_n, n_array) # in t=2/3

    # plot_speed_probability_distribution_initial_time(speeds_by_n, n_array) # in t=0

############# 3.3 Plot trajectories #############
if not trajectories_and_speeds is None:
    trajectories_by_t = list(
        map(
            lambda a: [
                list(map(lambda pos: pos['x'], a['positions'])), 
                list(map(lambda pos: pos['y'], a['positions']))
            ],
            trajectories_and_speeds
        )
    )
    speeds = list(
        map(
            lambda a: [
                a['minSpeed'], 
                a['maxSpeed']
            ],
            trajectories_and_speeds
        )
    )
    number_of_particles = trajectories_and_speeds[0]['n']
    #plot_big_particle_trajectories(trajectories_by_t, speeds, number_of_particles)

############# 3.4 Plot DCM #############

if not simulations_big_trajectories_results is None:
    simulations_big_trajectories = list(map(lambda a: a['positions'], simulations_big_trajectories_results))
    #plot_dcm(simulations_big_trajectories, 'BIG', 1)

if not small_trajectories_results is None:
    small_trajectories = list(map(lambda a: a['positions'], small_trajectories_results))
    #plot_dcm(small_trajectories, 'SMALL', 1)

