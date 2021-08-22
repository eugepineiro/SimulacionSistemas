import json, random, sys
from plotter import plot, plot_comparison, plot_comparison_2D

with open("../src/main/resources/config/config.json") as f:
    config = json.load(f) 

with open("../src/main/resources/result.json") as f:
    all_particles = json.load(f)

interaction_radius = config["r_interaction_radius"] 
number_of_particles = len(all_particles)
M_grid_size = config["m_grid_dimension"]
L_grid_side = config["l_grid_side"]
periodic_condition = config["periodic_return"]
compare_with_brute_force = config["compare_with_brute_force"]

if compare_with_brute_force["compare"]:
    with open("../src/main/resources/compare.json") as f:
        comparison_data = json.load(f)

if(len(sys.argv) == 2):
    try:
        aux = int(sys.argv[1])
        if aux < 0:
            raise Exception('Id must be zero or positive')
        elif aux >= len(all_particles):
            raise Exception('There is not enough particles')
        else:
            idx = aux
    except:
        raise Exception("Id must be a number")
else:
    idx = random.randint(0,number_of_particles-1)

particle = all_particles[idx]
print("ID: ", particle['id'])

all_particles_id = []
all_particles_x = []
all_particles_y = []
all_particles_radius = []
neighbours_id = []
neighbours_x = []
neighbours_y = []
neighbours_radius = []


for i in range(number_of_particles): 
    if(i != idx):
        all_particles_id.append(all_particles[i]['id'])
        all_particles_x.append(all_particles[i]['x'])
        all_particles_y.append(all_particles[i]['y'])
        all_particles_radius.append(all_particles[i]['radius'])

for i in range(len(all_particles[idx]['neighbours'])): 
    neighbours_id.append(all_particles[idx]['neighbours'][i]['id'])
    neighbours_x.append(all_particles[idx]['neighbours'][i]['x'])
    neighbours_y.append(all_particles[idx]['neighbours'][i]['y'])
    neighbours_radius.append(all_particles[idx]['neighbours'][i]['radius'])    

if (compare_with_brute_force['compare']):
    plot_comparison(comparison_data, L_grid_side)
    plot_comparison_2D(comparison_data)
else:
    plot(all_particles_x,all_particles_y, all_particles_radius, all_particles_id, particle, interaction_radius, [neighbours_x, neighbours_y, neighbours_radius, neighbours_id], M_grid_size, L_grid_side, periodic_condition)
