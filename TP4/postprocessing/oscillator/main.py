import json, math
from plotter import plot_oscillator_position_by_time

def mse(positions):
    diferences = []
    for i in range(0, len(positions)-1):
        diferences.append(math.pow(positions[i+1] - positions[i], 2))
    
    return sum(diferences)/len(diferences)

with open("../../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_oscillator_results.json") as f:
    oscillator_results = json.load(f)

print(len(oscillator_results))

positions = list(map(lambda p: p['particles'][0]['y'], oscillator_results))
times = list(map(lambda t: t['time'], oscillator_results))

print(mse(positions))

plot_oscillator_position_by_time([positions], times, ['Verlet Original'])
