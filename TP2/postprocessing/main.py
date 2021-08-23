import json 

with open("../src/main/resources/config/config.json") as f:
    config = json.load(f) 

interaction_radius = config["r_interaction_radius"] 
M_grid_size = config["m_grid_dimension"]
L_grid_side = config["l_grid_side"]
density = config["density"]
speed = config["speed"]
noise = config["noise_amplitude"]

with open("../src/main/resources/results.json") as f:
    results = json.load(f) 

number_of_simulations = len(results)
density_array = []
polarization_array = []

for simulation in range(number_of_simulations):
    density_array.append(results[simulation]["density"])
    polarization_array.append(results[simulation]["polarization"])


# TP2/src/main/resources/ovito
