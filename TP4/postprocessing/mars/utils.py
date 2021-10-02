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