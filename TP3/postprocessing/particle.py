import json, random, sys
from plotter import plot, plot_comparison, plot_comparison_2D

with open("../src/main/resources/config/config.json") as f:
    config = json.load(f) 

with open("../src/main/resources/SdS_TP3_2021Q2G01_results.json") as f:
    results = json.load(f)


number_of_particles = config["number_of_particles"]
M_grid_size = config["m_grid_dimension"]
L_grid_side = config["l_grid_side"]  
frames = config["frames"]

# 1.1 Time avg 
avg_time = sum(results) / len(results)


