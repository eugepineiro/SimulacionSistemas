import json
from plotter import plot_big_particle_trajectories, plot_dcm, plot_speed_probability_distribution_initial_time, plot_time_probability_distribution, plot_speed_probability_distribution
from json_parser import parse_extended_events
import numpy as np

with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results_multiple_n_event_times.json") as f:
    times_and_n = json.load(f)

# with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results_multiple_n_small_particles_speeds.json") as f:
#     speeds_and_n = json.load(f)

# with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results_multiple_temperatures_big_particle_positions.json") as f:
#     trajectories_and_speeds = json.load(f)

# with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results_multiple_simulations_big_particle_positions.json") as f:
#     simulations_big_trajectories_results = json.load(f)

# with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results_small_particles_positions.json") as f:
#     small_trajectories_results = json.load(f)

with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results_multiple_n.json") as f:
    multiple_n_results = json.load(f)

with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results_multiple_temperatures.json") as f:
    multiple_temperatures_result = json.load(f)

with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results_multiple_simulations.json") as f:
    multiple_simulations_results = json.load(f)

if not multiple_n_results is None:
    n_array = list(map(
        lambda n_sim: n_sim['n'],
        multiple_n_results
    ))

############# 3.1 Plot Times #############

if not times_and_n is None:
    times_by_n = list(map(lambda a: a['results'], times_and_n))
    n_array = list(map(lambda a: a['n'], times_and_n))

    plot_time_probability_distribution(times_by_n, n_array) 

# if not multiple_n_results is None:
#     times_by_n = list(map(
#         lambda n_sim: list(map(
#             lambda extended_event: extended_event['event']['time'],
#             n_sim['events']
#         )), 
#         multiple_n_results
#     ))
#     plot_time_probability_distribution(times_by_n, n_array) 

############# 3.2 Plot Speeds #############

# if not speeds_and_n is None:
#     speeds_by_n = list(map(lambda a: a['results'], speeds_and_n))
#     n_array = list(map(lambda a: a['n'], speeds_and_n))
#     plot_speed_probability_distribution(speeds_by_n, n_array)

#     plot_speed_probability_distribution_initial_time(speeds_by_n, n_array)

if not multiple_n_results is None:
    speeds_by_n = list(map(
        lambda n_sim: list(map(
            lambda extended_event: list(map(
                lambda p: p['speed'],
                filter(lambda p: p['type'] == 'SMALL', extended_event['frame'])
            )),
            n_sim['results']
        )),
        multiple_n_results
    ))
    plot_speed_probability_distribution(speeds_by_n, n_array) # in t=2/3
    plot_speed_probability_distribution_initial_time(speeds_by_n, n_array) # in t=0

# if not trajectories_and_speeds is None:
#     trajectories_by_t = list(
#         map(
#             lambda a: [
#                 list(map(lambda pos: pos['x'], a['positions'])), 
#                 list(map(lambda pos: pos['y'], a['positions']))
#             ],
#             trajectories_and_speeds
#         )
#     )
#     speeds = list(
#         map(
#             lambda a: [
#                 a['minSpeed'], 
#                 a['maxSpeed']
#             ],
#             trajectories_and_speeds
#         )
#     )
#     number_of_particles = trajectories_and_speeds[0]['n']
#     plot_big_particle_trajectories(trajectories_by_t, speeds, number_of_particles)

if not multiple_temperatures_result is None:
    trajectories_and_speeds = []

    for temp_sim in multiple_temperatures_result:
        t = []
        for extended_events in temp_sim['events']:
            for p in extended_events['frame']:
                if p['type'] == 'BIG':
                    t.append(p)
        trajectories_and_speeds.append(t)
    
    trajectories_by_t = list(
        map(
            lambda a: [
                list(map(lambda pos: pos['x'], a)), 
                list(map(lambda pos: pos['y'], a))
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
            multiple_temperatures_result
        )
    )
    number_of_particles = multiple_temperatures_result[0]['n']
    plot_big_particle_trajectories(trajectories_by_t, speeds, number_of_particles)


############# 3.4 Plot DCM #############

# if not simulations_big_trajectories_results is None:
#     simulations_big_trajectories = list(map(lambda a: a['positions'], simulations_big_trajectories_results))
#     plot_dcm(simulations_big_trajectories, 'BIG', 1)
# if not small_trajectories_results is None:
#     small_trajectories = list(map(lambda a: a['positions'], small_trajectories_results))
#     plot_dcm(small_trajectories, 'SMALL', 1)

if not multiple_simulations_results is None:
    
    # Big Particle
    simulations_big_trajectories = []

    for idx, temp_sim in enumerate(multiple_temperatures_result):
        big_t = []
        for extended_event in temp_sim['events']:
            for p in extended_event['frame']:
                if p['type'] == 'BIG':
                    p['time'] = extended_event['event']['time']
                    big_t.append(p)
        simulations_big_trajectories.append(big_t)

    plot_dcm(simulations_big_trajectories, 'BIG', 1)

    # Small Particle
    extended_events = multiple_simulations_results[0]

    small_particles = list(filter(lambda p: p['type'] == 'SMALL', extended_events[0]['frame']))
    simulations_small_trajectories_map = {}

    for p in small_particles:
        simulations_small_trajectories_map[p['id']] = []

    for extended_event in extended_events:
        for p in extended_event['frame']:
            if p['type'] == 'SMALL':
                p['time'] = extended_event['event']['time']
                simulations_small_trajectories_map[p['id']].append(p)

    simulations_small_trajectories = simulations_small_trajectories_map.values()

    plot_dcm(simulations_small_trajectories, 'SMALL', 1)

