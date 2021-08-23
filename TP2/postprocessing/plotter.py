import plotly.express as px
import pandas as pd
import numpy as np


def plot_polarization_by_frame(results, density, n):

    polarization_array, noise_array = get_polarization_by('density', density, 'n', n, results, 'noise')
    frames = np.arange(len(polarization_array[0]))

    print(polarization_array)
    print(noise_array) 

    df = pd.DataFrame(dict(
        polarization =  polarization_array[0],   
        frame = frames
    ))
    fig = px.line(df, x="frame", y="polarization", title="Polarization By Frame", markers=True)
    fig.show()


def plot_polarization_by_density(results, noise, n):

    polarization_array, d_array = get_polarization_by('noise', noise, 'n', n, results, 'density')
    density_array = [0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1]

    print(polarization_array)
    print(d_array)
    frame = 0
    df = pd.DataFrame(dict(
        polarization =  polarization_array[frame],   
        density = d_array
    ))
    fig = px.line(df, x="density", y="polarization", title="Polarization By Density", markers=True)
    fig.show()

def get_polarization_by(param1, value1, param2, value2, results, other_param): 
    
    polarizations_array=[]
    other_param_array = []

    for i in range(len(results)): 
         
        if(results[i][param1] == value1 and results[i][param2] == value2):

            avg_polarization_array = get_avg_polarization(results[i]["polarization"])
            polarizations_array.append(avg_polarization_array) 
            other_param_array.append(results[i][other_param])
            
    return polarizations_array, other_param_array


def get_avg_polarization(polarizations): 

    avg_polarization_array = []
    total_simulations = len(polarizations) 
    total_frames =  len(polarizations[0])

    for frame in range(total_frames): # for each simulation, avg each frame
        sum = 0
        for simulation in range(total_simulations):
            sum = sum + polarizations[simulation][frame]
        
        avg_polarization = sum/total_simulations
    
        avg_polarization_array.append(avg_polarization)
    
    return avg_polarization_array