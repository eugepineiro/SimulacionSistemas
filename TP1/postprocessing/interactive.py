import json, random, sys
import math

import pygame, sys
from pygame.locals import *


# Colors
WHITE = (255,255,255)
BLACK = (0,0,0)
BLUE = (0,0,255)

# Config
BACKGROUND_COLOR = BLACK

PARTICLE_RGBA = (230,170,100,128)
NEIGHBOUR_RGBA = (100,0,200,128)
CHOSEN_PARTICLE_RGBA = (255,255,255,255)
INTERACTION_RADIUS_RGBA = (255,255,255,40)
INTERACTION_RADIUS_WIDTH = 1

def draw_circle(d, x, y, radius, rgba, border_width=None):
    surface = d.convert_alpha()
    surface.fill([0,0,0,0])
    pygame.draw.circle(surface, rgba, (x, y), radius)
    d.blit(surface, (0, 0))
    pygame.display.flip()

def draw_particle(d, x, y, radius, rgba):
    # print("Printing circle")
    draw_circle(d, x, y, radius, rgba)

def draw_interaction_radius(d, x, y, radius, rgba):
    draw_circle(d, x, y, radius, rgba)

def get_chosen_particle(all_particles, pos):
    (mouse_x, mouse_y) = pos

    for particle in all_particles:
        (x, y) = (particle['x'], particle['y'])
        radius = particle['radius']

        dist = math.sqrt((x-mouse_x)**2 + (y-mouse_y)**2)

        if dist <= radius:
            return particle

    return None

def main():
    with open("../src/main/resources/config/config.json") as f:
        config = json.load(f) 

    with open("../src/main/resources/result.json") as f:
        all_particles = json.load(f)

    pygame.init()
    pygame.display.set_caption('Particles')

    # SETUP

    WINDOW_SIZE = 800
    WINDOW_WIDTH = WINDOW_SIZE
    WINDOW_HEIGHT = WINDOW_SIZE

    d = pygame.display.set_mode((WINDOW_WIDTH,WINDOW_HEIGHT))

    # DATA

    # Scaling 

    scale_factor = 1.0 * WINDOW_SIZE / config["l_grid_side"]
    
    for p in all_particles:
        p['x'] *= scale_factor
        p['y'] *= scale_factor
        p['radius'] *= scale_factor

        for n in p['neighbours']:
            n['x'] *= scale_factor
            n['y'] *= scale_factor
            n['radius'] *= scale_factor

    config['r_interaction_radius'] *= scale_factor

    chosen_particle = None
    neighbours_positions = []
    particles_positions = set( map(lambda p: (p['x'], p['y']), all_particles) )

    def draw_scene():
        d.fill(BACKGROUND_COLOR)

        # Draw particles
        neighbours = set() if chosen_particle is None else set( map(lambda p: (p['x'], p['y']), chosen_particle['neighbours']) )

        if not chosen_particle is None:
            # Draw neighbours
            for neighbour in chosen_particle['neighbours']:
                draw_particle(d, neighbour['x'], neighbour['y'], neighbour['radius'], NEIGHBOUR_RGBA)

            # Draw chosen particle
            draw_particle(d, chosen_particle['x'], chosen_particle['y'], chosen_particle['radius'], CHOSEN_PARTICLE_RGBA)
            draw_interaction_radius(d, chosen_particle['x'], chosen_particle['y'], chosen_particle['radius'] + config["r_interaction_radius"], INTERACTION_RADIUS_RGBA)

        for particle in all_particles:
            x, y = particle['x'], particle['y']
            if chosen_particle is None or (x, y) != (chosen_particle['x'], chosen_particle['y']):
                if not (x, y) in neighbours:
                    # print(f"Printing {(particle['x'], particle['y'], particle['radius'])}")
                    draw_particle(d, particle['x'], particle['y'], particle['radius'], PARTICLE_RGBA)


    draw_scene()

    pygame.display.update()

    while True:

        for event in pygame.event.get():

            if event.type == QUIT:
                pygame.quit()
                sys.exit()

            if event.type == pygame.KEYDOWN:
                if event.key==K_LEFT:
                    print("Hola")
            
            if event.type == pygame.MOUSEBUTTONDOWN:
                pos = pygame.mouse.get_pos()

                previous_selected = chosen_particle
                
                chosen_particle = get_chosen_particle(all_particles, pos)

                if chosen_particle != previous_selected:
                    draw_scene()


        pygame.display.update()

main()