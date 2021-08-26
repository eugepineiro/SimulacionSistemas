import dash
import dash_core_components as dcc
import dash_html_components as html
from dash.dependencies import Input, Output, State
import plotly.express as px
import plotly.graph_objects as go

import pandas as pd
import numpy as np

# source: https://dash.plotly.com/layout

external_stylesheets = [
    {
        "href": "https://fonts.googleapis.com/css2?"
                "family=Lato:wght@400;700&display=swap",
        "rel": "stylesheet",
    },
]

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

def get_polarization_by_frame_figure(results, density, n):
    polarization_array, noise_array = get_polarization_by('density', density, 'n', n, results, 'noise')
    if len(polarization_array) > 0:
        frames = np.arange(len(polarization_array[0]))
    else: 
        raise ValueError(f"There is no polarization with density {density} and number of particles {n}\n")
    
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

    return fig

def plot_polarization_by_density_figure(results, noise, n, density_range, density_increase):

    #polarization_array, d_array = get_polarization_by('noise', noise, 'n', n, results, 'density') # d_array: densities with that noise and n 
    #density_array = list(np.arange(density_range[0],density_range[1], density_increase, dtype=float))  #plot x_axis 

    # avg_polarizations_by_density = []

    polarizations_by_simulation = list(filter(lambda p: p['noise'] == noise and p['n'] == n, results))
    avg_polarizations_by_simulation = []
    densities = []

    for i in range(len(polarizations_by_simulation)):
        # print(len(polarizations_by_simulation[i]['polarization']))
        for j in range(len(polarizations_by_simulation[i]['polarization'])):
            avg = sum(polarizations_by_simulation[i]['polarization'][j][1200:]) / float(len(polarizations_by_simulation[i]['polarization'][j][1200:]))
            avg_polarizations_by_simulation.append(avg)
            densities.append(polarizations_by_simulation[i]['density'])

    print(avg_polarizations_by_simulation)

    fig = go.Figure()
    fig.add_trace(go.Box(
       x=densities, 
       y=avg_polarizations_by_simulation #[[1,2,3,4,5,6,7,8],[1,2,3,4,5,6,7,8]]
    ))
    #fig = px.box(df, x="Density", y="Polarization", title="Polarization By Density")

    fig.update_layout(
    title="Polarization By Density",
    xaxis_title="Density",
    yaxis_title="Polarization", 
    legend_title=f"References\nNoise: {noise}\nNumber of Particles: {n}\n",
    font=dict(
        #family="Courier New, monospace",
        #size=18,
        #color="RebeccaPurple"
        )
    )
    
    return fig



def plot_results(results):

    app = dash.Dash(__name__, external_stylesheets=external_stylesheets)
    app.title = "Off-Lattice"

    polarization_by_frame_fig = get_polarization_by_frame_figure(results, 0.4, 50)
    polarization_by_density_fig = plot_polarization_by_density_figure(results, 0.6, 50, [0.1, 1], 0.1)

    def serve_layout():
        return html.Div(
            children=[

                # Header
                html.Div( 
                    children=[
                        html.H1(
                            children='Off-Lattice',
                            className='header-title',
                        ),

                        html.P(
                            children='Simulation',
                            className='header-description'
                        ),
                    ],
                    className='header'
                ),

                # Graphs

                ## Polarization By Frame
                html.Div(
                    children=[
                        html.P(
                            children='',
                            className="figure-title"
                        ),
                        html.Div(
                            children=[
                                dcc.Graph(
                                    id='polarization-by-frame',
                                    figure=polarization_by_frame_fig
                                )
                            ],
                            className='card'
                        ),
                    ],
                    className='wrapper',
                ),

                ## Polarization by Density
                html.Div(
                    children=[
                        html.P(
                            children='',
                            className="figure-title"
                        ),
                        html.Span(
                            children=[
                                dcc.Graph(
                                    id='polarization-by-density',
                                    figure=polarization_by_density_fig
                                ), 
                                "Noise"
                            ],
                            className='card'
                        ),
                    ],
                    className='wrapper',
                ),
                
            ],
            style={
                "text-align": "center",
                "margin-left": "auto",
                "margin-right": "auto"
            }
        )

    app.layout = serve_layout

    port = 5000
    app.run_server(host='0.0.0.0', port=port, debug=True)