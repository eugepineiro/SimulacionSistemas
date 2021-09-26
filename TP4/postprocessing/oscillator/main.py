import json, math
import numpy as np

from plotter import plot_oscillator_position_by_time

def mse_mati(positions):
    pos = np.array(positions)
    return sum(np.power(pos[:len(positions)-1] - pos[1:], 2))/len(pos[1:])

def mse(positions):
    diferences = []
    for i in range(0, len(positions)-1):
        diferences.append(math.pow(positions[i+1] - positions[i], 2))
    
    return sum(diferences)/len(diferences)

with open("../../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_oscillator_results.json") as f:
    oscillator_results = json.load(f)

print(len(oscillator_results))

positions = list(map(
    lambda res: list(map(lambda p: p['particles'][0]['y'], res['results'])),
    oscillator_results
))
names = list(map(
    lambda res: ' '.join(list(map(lambda w: w.capitalize(), res['integration'].split('_')))),
    oscillator_results
))
times = list(map(lambda t: t['time'], oscillator_results[0]['results']))

print(mse_mati(positions[0]))
print(mse(positions[0]))

plot_oscillator_position_by_time(positions, times, names)
