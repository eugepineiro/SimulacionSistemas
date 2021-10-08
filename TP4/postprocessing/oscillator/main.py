import json, math, os
import numpy as np

from plotter import plot_oscillator_position_by_time, plot_oscillator_error_by_dt

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

# ============ ej 1.3 ======================
PATH = "../../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_oscillator_results_with_multiple_dt.json"
if(os.path.exists(PATH)):
    with open(PATH) as f:
        multiple_dt = json.load(f)
        multiple_dt = { float(k): v for k, v in multiple_dt.items() }

    dts = []
    errors = {
        "beeman": [],
        "verlet_original": [],
        "gear": []
    }


    for dt in sorted(multiple_dt):
        dts.append(dt)
        times = list(map(lambda t: t['time'], multiple_dt[dt][0]['results']))
        analytical = analytic_solution(times)

        for osc in multiple_dt[dt]:
            positions = list(map(lambda p: p["particles"][0]["y"], osc["results"]))
            # print(positions)
            errors[osc["integration"]].append(mse(positions, analytical))

    print(dts, errors)
    plot_oscillator_error_by_dt(dts, errors)
