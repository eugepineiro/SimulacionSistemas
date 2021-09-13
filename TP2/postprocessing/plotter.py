import plotly.express as px
import plotly.graph_objects as go

import pandas as pd
import numpy as np


def plot_polarization_by_frame(results, density, n):

    polarization_array, noise_array = get_polarization_by('density', density, 'n', n, results, 'noise')
    frames = np.arange(len(polarization_array[0]))

    print(polarization_array)
    print(noise_array) 

    df = pd.DataFrame(dict(
        Polarization =  polarization_array[0],   
        Frame = frames
    ))
    
    fig = go.Figure() #px.line(df,x="Frame", y="Polarization", title="Polarization By Frame", markers=True)
    for i in range(len(polarization_array)): 
        #fig.add_scatter(x=polarization_array[i], y=frames, mode='lines+markers', name='noise = '+str(noise_array[i]))
        fig.add_trace(go.Scatter(
            x=frames, 
            y=polarization_array[i], 
            mode='lines+markers', 
            name=f'noise = {noise_array[i]:.2f}'
            )
        )

    fig.update_layout(
    title="Polarization By Frame",
    xaxis_title="Frame",
    yaxis_title="Polarization",
    legend_title=f"References\nDensity: {density}\nNumber of Particles: {n}\n",
    font=dict(
        #family="Courier New, monospace",
        #size=18,
        #color="RebeccaPurple"
        )
    )

    fig.update_yaxes(type="log")

    fig.show()

def plot_polarization_by_density(results, noise, n, density_range, density_increase):

    polarization_array, d_array = get_polarization_by('noise', noise, 'n', n, results, 'density') # d_array: densities with that noise and n 
    density_array = list(np.arange(density_range[0],density_range[1], density_increase, dtype=float))  #plot x_axis 

    avg_polarizations_by_density = []
    for i in range(len(polarization_array)): 
        avg = sum(polarization_array[i][1200:]) / float(len(polarization_array[0][1200:]))
        avg_polarizations_by_density.append(avg)

    print(len(polarization_array))
    print(d_array) 
    df = pd.DataFrame(dict(
        Polarization =  avg_polarizations_by_density,   
        Density = d_array
    ))
    fig = px.box(df, x="Density", y="Polarization", title="Polarization By Density")
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