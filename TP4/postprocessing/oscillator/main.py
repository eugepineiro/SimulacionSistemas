import json, math
import numpy as np

from plotter import plot_oscillator_position_by_time

def analytic_solution(integrations_times):
    # Analytic Solution 
    m = 70
    k = 10**4
    gamma = 100

    r = lambda t: math.exp(-(gamma/(2*m))*t) * np.cos(((k/m) - (gamma**2)/(4*m**2))**(0.5) * t)
    y = [r(t) for t in integrations_times]

    return y

def mse(positions, analytical_positions):
    pos = np.array(positions)
    anal_pos = np.array(analytical_positions)
    return sum(np.power(pos - anal_pos, 2))/len(pos)

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

analytical_y = analytic_solution(times)

plot_oscillator_position_by_time(positions, analytical_y, times, names)

mses = list(map(
    lambda res: mse(res, analytical_y),
    positions
))

print(names)
print(mses)