import json
from plotter import plot_big_particle_trajectories, plot_dcm, plot_speed_probability_distribution_initial_time, plot_time_probability_distribution, plot_speed_probability_distribution
import numpy as np

with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results_multiple_n_event_times.json") as f:
    times_and_n = json.load(f)

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

############# 3.2 Plot Speeds #############

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

if not multiple_simulations_results is None:
    
    extended_events = multiple_simulations_results[0]
    small_particles = list(filter(lambda p: p['type'] == 'SMALL', extended_events[0]['frame']))

    # Big ar.edu.itba.ss.Particle
    simulations_big_trajectories = []

    for idx, temp_sim in enumerate(multiple_simulations_results):
        big_t = []
        for extended_event in temp_sim:
            for p in extended_event['frame']:
                if p['type'] == 'BIG':
                    p['time'] = extended_event['event']['time']
                    big_t.append(p)
        simulations_big_trajectories.append(big_t)

    plot_dcm(simulations_big_trajectories, 'BIG', len(small_particles), 1)

    # Small ar.edu.itba.ss.Particle
    simulations_small_trajectories_map = {}

    for p in small_particles:
        simulations_small_trajectories_map[p['id']] = []

    for extended_event in extended_events:
        for p in extended_event['frame']:
            if p['type'] == 'SMALL':
                p['time'] = extended_event['event']['time']
                simulations_small_trajectories_map[p['id']].append(p)

    simulations_small_trajectories = simulations_small_trajectories_map.values()

    plot_dcm(simulations_small_trajectories, 'SMALL', len(small_particles), 1)

