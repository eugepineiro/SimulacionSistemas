from plotter import plot_polarization_by_frame
from plotter import plot_polarization_by_density
import json 

with open("../src/main/resources/config/config.json") as f:
    config = json.load(f) 

interaction_radius = config["r_interaction_radius"] 
M_grid_size = config["m_grid_dimension"]
L_grid_side = config["l_grid_side"]
density = config["density"]
speed = config["speed"]
noise = config["noise_amplitude"]

with open("../src/main/resources/postprocessing/SdS_TP2_2021Q2G01_results.json") as f:
    results = json.load(f) 
 

density_array = []
noise_array = []
number_of_particles_array = []
polarization_array = [] # polarizations by frame

for i in range(len(results)):
    density_array.append(results[i]["density"])
    noise_array.append(results[i]["noise"])
    number_of_particles_array.append(results[i]["n"])
    polarization_array.append(results[i]["polarization"])

plot_polarization_by_frame(results, 0.4, 50)
#plot_polarization_by_density(results,3.0 , 10)
 