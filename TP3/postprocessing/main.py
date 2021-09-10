import json, random, sys
from plotter import plot_times_pdf

with open("../src/main/resources/config/config.json") as f:
    config = json.load(f) 

with open("../src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results.json") as f:
    results = json.load(f)


number_of_particles = config["n_number_of_particles"]
M_grid_size = config["m_grid_dimension"]
L_grid_side = config["l_grid_side"]  
frames = config["frames"]

# 1.1 Time avg 
print(len(results))
avg_times = []
for i in range(len(results)-1): 
    avg_times.append( results[i+1] - results[i])

plot_times_pdf(avg_times)
