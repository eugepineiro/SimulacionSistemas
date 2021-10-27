import json
from plotter import plot_evacuated_particles_by_time

with open("../../src/main/resources/config/config.json") as f:
    config = json.load(f)

with open("../../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_results.json") as f:
    results = json.load(f)

