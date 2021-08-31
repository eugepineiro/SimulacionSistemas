from plotter_dash import plot_results
import json 

with open("../src/main/resources/config/config.json") as f:
    config = json.load(f) 

# interaction_radius = config["r_interaction_radius"] 
# M_grid_size = config["m_grid_dimension"]
# # L_grid_side = config["l_grid_side"]
# # if "density" in config:
# #     density = config["density"]
# # else:
# #     density = config["n_number_of_particles"] / (config["l_grid_side"])**2
# speed = config["speed"]
# noise = config["noise_amplitude"]
# density_range = config["polarization"]["density_range"]
# density_increase = config["polarization"]["density_increase"]
# number_of_particles_range = config["polarization"]["number_of_particles_range"]
# noise_range = config["polarization"]["noise_range"]

with open("../src/main/resources/postprocessing/SdS_TP2_2021Q2G01_results.json") as f:
    results = json.load(f) 
 

# density_array = []
# noise_array = []
# number_of_particles_array = []
# polarization_array = [] # polarizations by frame

# for i in range(len(results)):
#     density_array.append(results[i]["density"])
#     noise_array.append(results[i]["noise"])
#     number_of_particles_array.append(results[i]["n"])
#     polarization_array.append(results[i]["polarization"])

# pairs_d_n = set() 
# pairs_noise_n = set()

# for d, n in zip(density_array, number_of_particles_array): 
#     pairs_d_n.add((d,n))

# for noise, n in zip(noise_array, number_of_particles_array): 
#     pairs_noise_n.add((noise,n))
    
# print("Plotting Polarization by Frame, by Density and by Noise:\n")
# print("Possible pairs Density-Number Of Particles")
# print(pairs_d_n)

# print("\nPossible pairs Noise-Number Of Particles")
# print(pairs_noise_n)

# print("... Plotting ...")
#fixed_density_by_frame,  = input(f"Enter Pair Density - Number Of Particles: ").split()
#fixed_density = input(f"Enter fixed density {density_range}: ")
#fixed_noise = input(f"Enter fixed noise: {noise_range}: ")

plot_results(results)
#plot_polarization_by_frame(results, 0.4, 50)
#plot_polarization_by_density(results,3.0 , 10)
 