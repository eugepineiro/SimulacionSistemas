import json
from plotter import plot_oscillator_position_by_time

with open("../../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_oscillator_results.json") as f:
    oscillator_results = json.load(f)

print(len(oscillator_results))

positions = list(map(lambda p: p['particles'][0]['y'], oscillator_results))
times = list(map(lambda t: t['time'], oscillator_results))

print(positions)

plot_oscillator_position_by_time([positions], times, ['verlet_original'])
