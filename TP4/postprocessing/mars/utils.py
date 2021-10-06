import math 


def calculate_distance(p1, p2): 
    
    return math.sqrt(math.pow(p2['x'] - p1['x'],2) +  math.pow(p2['y'] - p1['y'],2) )

def get_min_distances(mars_positions_by_date, spaceship_positions_by_date): # positions = [[x,y] , [x,y]]

    min_distances_by_date = {}

    for date in mars_positions_by_date.keys():

        min_distance = math.inf
        
        mars_positions = mars_positions_by_date[date]
        spaceship_positions = spaceship_positions_by_date[date]

        for i in range(len(mars_positions)):
            
            mars_position = mars_positions[i]
            spaceship_position = spaceship_positions[i]

            distance = calculate_distance(mars_position, spaceship_position)

            if distance < min_distance:
                min_distance = distance

        min_distances_by_date[date] = min_distance

        # print(date, min_distances_by_date[date])

    return min_distances_by_date

def get_planet_and_spaceship_positions_by_key(keys, results_dict, planet_name): 

    #keys = list(results_dict.keys())
    planet_positions_by_key = {} 
    spaceship_positions_by_key = {}

    for key in keys:
        frames_particles = list(map(lambda f: f['particles'], results_dict[key]))
        planet_frames = list(map(
            lambda f: list(filter(lambda p: p['type'] == planet_name, f))[0], 
            frames_particles
        ))
        planet_positions = list(map(
            lambda p: {'x': p['x'], 'y': p['y']},
            planet_frames
        ))
        planet_positions_by_key[key] = planet_positions
        spaceship_frames = list(map(
            lambda f: list(filter(lambda p: p['type'] == 'SPACESHIP', f))[0], 
            frames_particles
        ))
        spaceship_positions = list(map(
            lambda p: {'x': p['x'], 'y': p['y']},
            spaceship_frames
        ))
        spaceship_positions_by_key[key] = spaceship_positions

    return planet_positions_by_key, spaceship_positions_by_key

def get_arrival_time(planet_positions, spaceship_positions, results_dict, min_distance):
    idx = 0
    while idx < len(planet_positions):
        dist = calculate_distance(planet_positions[idx], spaceship_positions[idx])
        if dist == min_distance: # found
            break
        idx += 1

    arrival_time = results_dict[idx]['time']

    return arrival_time