import dash
import dash_core_components as dcc
import dash_html_components as html
from dash.dependencies import Input, Output, State
from numpy.core.fromnumeric import sort
import plotly.express as px
import plotly.graph_objects as go

import pandas as pd
import numpy as np

from functools import cmp_to_key
from ast import literal_eval as make_tuple

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
        raise ValueError(f"There is no polarization with density {density:.2f} and number of particles {n}\n")
    
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
    legend_title={"text": f"<b>References</b><br>Density: {density:.2f}<br>Number of Particles: {n}<br>"},
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
    

def get_polarization_by_density_figure(results, noise, n, steady_state):
 
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
    legend_title=f"<b>References</b><br>Noise: {noise:.2f}<br>Number of Particles: {n}<br>",
    )
    
    return fig

def get_polarization_by_noise_figure(results, density, n, steady_state):

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
    legend_title=f"<b>References</b><br>Density: {density:.2f}<br>Number of Particles: {n}<br>",
    )

    return fig 

def get_all_combinations(results, attrs):
    attributes = set(attrs)
    combinations = set()

    for res in results:
        t = []
        for key, value in res.items():
            if key in attributes:
                t.append((key, value))
        combinations.add(tuple(t))

    def convert_to_option(s):

        l = list(s)

        label = ''

        for idx, (key, value) in enumerate(l):

            label += f"{key.capitalize()}: "

            if isinstance(value, int):
                label += f"{value}"
            else:
                label += f"{value:.2f}"
        
            if idx < len(l) - 2:
                label += ', '
            elif idx == len(l) - 2:
                label += ' and '

        value = tuple(list(map(lambda i: i[1], l)))

        option = {'label': label, 'value': str(value)}

        return option

    combinations = list(combinations)

    def comparator(c1, c2):
        l1 = list(c1)
        l2 = list(c2)

        for idx in range(0, len(l1)):
            dif = l1[idx][1] - l2[idx][1]

            if dif != 0:
                return dif

        return 0

    combinations = sorted(combinations, key=cmp_to_key(comparator))

    return list(map(convert_to_option, combinations))

def plot_results(results):

    app = dash.Dash(__name__, external_stylesheets=external_stylesheets)
    app.title = "Off-Lattice"

    # polarization_by_frame_fig = get_polarization_by_frame_figure(results, 0.1, 30)              # DENSITY - NUMBER OF PARTICLES 
    # polarization_by_density_fig = get_polarization_by_density_figure(results, 0.3, 30, 100)     # NOISE - NUMBER OF PARTICLES - STEADY STATE
    # polarization_by_noise_fig = get_polarization_by_noise_figure(results,  0.1, 30, 100)        # DENSITY - NUMBER OF PARTCILES - STEADY STATE

    density_n_combinations_options = get_all_combinations(results, ['density', 'n'])
    noise_n_combinations_options = get_all_combinations(results, ['noise', 'n'])

    print(density_n_combinations_options)

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

                        # html.P(
                        #     children='Simulation',
                        #     className='header-description'
                        # ),
                    ],
                    className='header'
                ),

                # Graphs

                ## Polarization By Frame

                html.Div(
                    children=[

                        ### Dropdown

                        html.Div(
                            children=[
                                dcc.Dropdown(
                                    id='polarization_by_frame_dropdown', 
                                    options=density_n_combinations_options,
                                    value = density_n_combinations_options[0]['value']
                                ),
                            ],
                            style={
                                "width": "300px",
                                "text-align": "center",
                                "margin-left": "auto",
                                "margin-right": "100px"
                            }
                        ),

                        ### Graph

                        html.Div(
                            children=[
                                html.P(
                                    children='',
                                    className="figure-title"
                                ),
                                html.Div(
                                    children=[
                                        dcc.Graph(
                                            id='polarization_by_frame',
                                            # figure=polarization_by_frame_fig
                                        )
                                    ],
                                    className='card'
                                ),
                            ],
                            className='wrapper',
                        ),
                    ]
                ),

                ## Polarization by Density

                html.Div(
                    children=[

                        ### Dropdown

                        html.Div(
                            children=[
                                dcc.Dropdown(
                                    id='polarization_by_density_dropdown', 
                                    options=noise_n_combinations_options,
                                    value = noise_n_combinations_options[0]['value']
                                ),
                            ],
                            style={
                                "width": "300px",
                                "text-align": "center",
                                "margin-left": "auto",
                                "margin-right": "100px"
                            }
                        ),

                        ### Graph

                        html.Div(
                            children=[
                                html.P(
                                    children='',
                                    className="figure-title"
                                ),
                                html.Span(
                                    children=[
                                        dcc.Graph(
                                            id='polarization_by_density',
                                            # figure=polarization_by_density_fig
                                        ),  
                                    ],
                                    className='card'
                                ),
                            ],
                            className='wrapper',
                        )
                    ]
                ),

                ## Polarization by Noise

                html.Div(
                    children=[

                        ### Dropdown

                        html.Div(
                            children=[
                                dcc.Dropdown(
                                    id='polarization_by_noise_dropdown', 
                                    options=density_n_combinations_options,
                                    value = density_n_combinations_options[0]['value']
                                ),
                            ],
                            style={
                                "width": "300px",
                                "text-align": "center",
                                "margin-left": "auto",
                                "margin-right": "100px"
                            }
                        ),

                        ### Graph

                        html.Div(
                            children=[
                                html.P(
                                    children='',
                                    className="figure-title"
                                ),
                                html.Span(
                                    children=[
                                        dcc.Graph(
                                            id='polarization_by_noise',
                                            # figure=polarization_by_noise_fig
                                        ),  
                                    ],
                                    className='card'
                                ),
                            ],
                            className='wrapper',
                        ),

                    ]
                )
                
            ],
            style={
                "text-align": "center",
                "margin-left": "auto",
                "margin-right": "auto"
            }
        )

    @app.callback(
        Output('polarization_by_frame', 'figure'), 
        [Input('polarization_by_frame_dropdown', 'value')]
    )
    def update_polarization_by_frame_graph(selected_value):
        (density, n) = make_tuple(selected_value)

        return get_polarization_by_frame_figure(results, density, n)

    @app.callback(
        Output('polarization_by_density', 'figure'), 
        [Input('polarization_by_density_dropdown', 'value')]
    )
    def update_polarization_by_density_graph(selected_value):
        (noise, n) = make_tuple(selected_value)

        return get_polarization_by_density_figure(results, noise, n, 100)

    @app.callback(
        Output('polarization_by_noise', 'figure'), 
        [Input('polarization_by_noise_dropdown', 'value')]
    )
    def update_polarization_by_noise_graph(selected_value):
        (density, n) = make_tuple(selected_value)

        return get_polarization_by_noise_figure(results, density, n, 100)

    app.layout = serve_layout

    port = 5000
    app.run_server(host='0.0.0.0', port=port, debug=True)