import json, random, sys
from plotter import plot, plot_comparison, plot_comparison_2D

with open("../src/main/resources/config/config.json") as f:
    config = json.load(f) 

with open("../src/main/resources/result.json") as f:
    all_particles = json.load(f)


number_of_particles = config["number_of_particles"]
M_grid_size = config["m_grid_dimension"]
L_grid_side = config["l_grid_side"]  
frames = config["frames"]

