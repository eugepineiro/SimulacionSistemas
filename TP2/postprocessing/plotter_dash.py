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

def get_polarization_by(param1, value1, param2, value2, other_param, results): 
    
    polarizations_array=[] 
    other_param_array = []

    for i in range(len(results)):          
        if(results[i][param1] == value1 and results[i][param2] == value2): 
            polarizations_array.append(results[i]["polarization"]) 
            other_param_array.append(results[i][other_param]) 
            
    return polarizations_array, other_param_array


def plot_polarization_by_density_figure(results, noise, n, density_range, density_increase):
 
    valid_polarizations = list(filter(lambda p: p['noise'] == noise and p['n'] == n, results)) # VALID POLARIZATIONS
    avg_polarizations_by_simulation = []
    polarizations = []
    densities = []

    for i in range(len(valid_polarizations)):
        # print(len(polarizations_by_simulation[i]['polarization']))
        for j in range(len(valid_polarizations[i]['polarization'])): #simulations
            avg = sum(valid_polarizations[i]['polarization'][j][1200:]) / float(len(valid_polarizations[i]['polarization'][j][1200:]))
            avg_polarizations_by_simulation.append(avg)
        
        polarizations.append(avg_polarizations_by_simulation)
        densities.append(valid_polarizations[i]['density'])
 
    fig = go.Figure()

    for i in range(len(densities)):

        fig.add_trace(go.Box(
            name=densities[i], 
            y=polarizations[i] #[[1,2,3,4,5,6,7,8],[1,2,3,4,5,6,7,8]]
        )) 

    fig.update_layout(
    title="Polarization By Density",
    xaxis_title="Density",
    yaxis_title="Polarization", 
    legend_title=f"<b>References</b><br>Noise: {noise}<br>Number of Particles: {n}<br>",
    )
    
    return fig

def plot_polarization_by_noise_figure(results, density, n):

    valid_polarizations = list(filter(lambda p: p['density'] == density and p['n'] == n, results)) # VALID POLARIZATIONS
    avg_polarizations_by_simulation = []
    polarizations = []
    noises = []

    for i in range(len(valid_polarizations)): 

        for j in range(len(valid_polarizations[i]['polarization'])): #simulations
            avg = sum(valid_polarizations[i]['polarization'][j][1200:]) / float(len(valid_polarizations[i]['polarization'][j][1200:]))
            avg_polarizations_by_simulation.append(avg)
        
        polarizations.append(avg_polarizations_by_simulation)
        noises.append(valid_polarizations[i]['noise'])
 
    fig = go.Figure()

    for i in range(len(noises)):

        fig.add_trace(go.Box(
            name=noises[i], 
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

    polarization_by_frame_fig = get_polarization_by_frame_figure(results, 0.4, 50)
    polarization_by_density_fig = plot_polarization_by_density_figure(results, 0.6, 50, [0.1, 1], 0.1)
    polarization_by_noise_fig = plot_polarization_by_noise_figure(results, 0.1, 30  )

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