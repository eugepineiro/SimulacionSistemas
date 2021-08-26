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

def get_polarization_by_frame(param1, value1, param2, value2, results, other_param): 
    
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


def get_polarization_by(param1, value1, param2, value2, results): 
            
    return list(filter(lambda p: p[param1] == value1 and p[param2] == value2, results))

def get_polarization_by_frame_figure(results, density, n):
    polarization_array, noise_array = get_polarization_by_frame('density', density, 'n', n, results, 'noise')
    if len(polarization_array) > 0:
        frames = np.arange(len(polarization_array[0]))
    else: 
        raise ValueError(f"There is no polarization with density {density} and number of particles {n}\n")
    
    fig = go.Figure() #px.line(df,x="Frame", y="Polarization", title="Polarization By Frame", markers=True)
    for i in range(len(polarization_array)): 
        
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
    legend_title={"text": f"<b>References</b><br>Density: {density}<br>Number of Particles: {n}<br>"},
    font=dict(
        #family="Courier New, monospace",
        #size=18,
        #color="RebeccaPurple"
        )
    )

    return fig

def get_polarization_with_steady_state(valid_polarizations, steady_state, other_param): 
    avg_polarizations_by_simulation = []
    polarizations = []
    other_param_array = [] 

    for i in range(len(valid_polarizations)): 
        
        for j in range(len(valid_polarizations[i]['polarization'])): #simulations
            avg = sum(valid_polarizations[i]['polarization'][j][steady_state:]) / float(len(valid_polarizations[i]['polarization'][j][steady_state:]))
            avg_polarizations_by_simulation.append(avg)
        
        polarizations.append(avg_polarizations_by_simulation)
        avg_polarizations_by_simulation = []
        other_param_array.append(valid_polarizations[i][other_param])
 
    
    return polarizations, other_param_array
    

def plot_polarization_by_density_figure(results, noise, n, steady_state):
 
    valid_polarizations = get_polarization_by('noise', noise, 'n', n, results) # VALID POLARIZATIONS
    polarizations = []
    densities = []
    
    polarizations, densities = get_polarization_with_steady_state(valid_polarizations, steady_state, 'density')

    fig = go.Figure()

    for i in range(len(densities)):

        fig.add_trace(go.Box(
            name=f'{densities[i]:.2f}', 
            y=polarizations[i] #[[1,2,3,4,5,6,7,8],[1,2,3,4,5,6,7,8]]
        )) 

    fig.update_layout(
    title="Polarization By Density",
    xaxis_title="Density",
    yaxis_title="Polarization", 
    legend_title=f"<b>References</b><br>Noise: {noise}<br>Number of Particles: {n}<br>",
    )
    
    return fig

def plot_polarization_by_noise_figure(results, density, n, steady_state):

    valid_polarizations = get_polarization_by('density', density, 'n', n, results)  
    polarizations = []
    noises = []
    
    polarizations, noises = get_polarization_with_steady_state(valid_polarizations, steady_state, 'noise')

    fig = go.Figure() 
    for i in range(len(noises)):

        fig.add_trace(go.Box(
            name=f'{noises[i]:.2f}', 
            y=polarizations[i] #[[1,2,3,4,5,6,7,8],[1,2,3,4,5,6,7,8]]
        )) 

    fig.update_layout(
    title="Polarization By Noise",
    xaxis_title="Noise",
    yaxis_title="Polarization", 
    legend_title=f"<b>References</b><br>Density: {density}<br>Number of Particles: {n}<br>",
    )

    return fig 

def plot_results(results):

    app = dash.Dash(__name__, external_stylesheets=external_stylesheets)
    app.title = "Off-Lattice"

    polarization_by_frame_fig = get_polarization_by_frame_figure(results,0.1, 30)             # DENSITY - NUMBER OF PARTICLES 
    polarization_by_density_fig = plot_polarization_by_density_figure(results, 0.3, 30, 100) # NOISE - NUMBER OF PARTICLES - STEADY SATATE
    polarization_by_noise_fig = plot_polarization_by_noise_figure(results,  0.1, 30, 100)    # DENSITY - NUMBER OF PARTCILES - STEADY SATATE

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
                            ],
                            className='card'
                        ),
                    ],
                    className='wrapper',
                ),
                ## Polarization by Noise
                html.Div(
                    children=[
                        html.P(
                            children='',
                            className="figure-title"
                        ),
                        html.Span(
                            children=[
                                dcc.Graph(
                                    id='polarization-by-noise',
                                    figure=polarization_by_noise_fig
                                ),  
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