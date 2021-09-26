from TP4.postprocessing.oscillator.plotter import plot_oscillator_position_by_time
import json
from plotter import plot_oscillator_position_by_time

with open("../src/main/resources/postprocessing/SdS_TP4_2021Q2G01_oscillator_results") as f:
    oscillator_results = json.load(f)

plot_oscillator_position_by_time()
