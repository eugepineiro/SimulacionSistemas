import dash
import dash_core_components as dcc
import dash_html_components as html
from dash.dependencies import Input, Output, State
from dash_html_components.P import P
from numpy.core.fromnumeric import sort
import plotly.express as px
import plotly.graph_objects as go
import dash_daq as daq

import pandas as pd
import numpy as np

from functools import cmp_to_key
from ast import literal_eval as make_tuple

# Constants

STARTING_AVERAGING_SINCE = 1200

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
    title="Polarización en función del Tiempo para múltiples Ruidos",
    xaxis_title="Tiempo",
    yaxis_title="Polarización", 
    legend_title={"text": f"<b>Referencias</b><br>Densidad: {density:.2f}<br>Número de Partículas: {n}<br>"},
    font=dict(
        #family="Courier New, monospace",
        #size=18,
        #color="RebeccaPurple"
        )
    )

    return fig

def get_polarization_by_frame_density_figure(results, noise, n):
    polarization_array, noise_array = get_polarization_by_frame('noise', noise, 'n', n, results, 'noise')
    if len(polarization_array) > 0:
        frames = np.arange(len(polarization_array[0]))
    else: 
        raise ValueError(f"There is no polarization with noise {noise:.2f} and number of particles {n}\n")
    
    fig = go.Figure() #px.line(df,x="Frame", y="Polarization", title="Polarization By Frame", markers=True)
    for i in range(len(polarization_array)): 
        
        fig.add_trace(go.Scatter(
            x=frames, 
            y=polarization_array[i], 
            mode='lines+markers', 
            name=f'density = {noise_array[i]:.2f}'
            )
        )

    fig.update_layout(
    title="Polarización en función del Tiempo para múltiples Densidades)",
    xaxis_title="Tiempo",
    yaxis_title="Polarización", 
    legend_title={"text": f"<b>Referencias</b><br>Ruido: {noise:.2f}<br>Número de Partículas: {n}<br>"},
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

    pnp = np.array(polarizations)
    mean = np.mean(pnp, axis=1)
    std = np.std(pnp, axis=1)

    fig.add_trace(go.Scatter(
        x=densities, 
        y=mean,
        mode='lines+markers',
        name=f'Density',
        error_y=dict(
            type='data',
            symmetric=True,
            array=std
        )
    )) 

    # for i in range(len(densities)):

    #     fig.add_trace(go.Box(
    #         name=f'{densities[i]:.2f}', 
    #         y=polarizations[i] #[[1,2,3,4,5,6,7,8],[1,2,3,4,5,6,7,8]]
    #     )) 

    fig.update_layout(
        title="Polarización en función de la Densidad",
        xaxis_title="Densidad",
        yaxis_title="Polarización", 
        legend_title=f"<b>References</b><br>Ruido: {noise:.2f}<br>Número de Partículas: {n}<br>Reǵimen Estacionario: {steady_state}<br>",
    )

    fig['data'][0]['showlegend']=True
    
    return fig

def get_polarization_by_noise_figure(results, density, n, steady_state):

    valid_polarizations = get_polarization_by('density', density, 'n', n, results)  
    polarizations = []
    noises = []
    
    polarizations, noises = get_polarization_with_steady_state(valid_polarizations, steady_state, 'noise')

    fig = go.Figure() 

    pnp = np.array(polarizations)
    mean = np.mean(pnp, axis=1)
    std = np.std(pnp, axis=1)

    fig.add_trace(go.Scatter(
        x=noises, 
        y=mean,
        mode='lines+markers',
        name='Noise',
        error_y=dict(
            type='data',
            symmetric=True,
            array=std
        )
    )) 

    # for i in range(len(noises)):

    #     fig.add_trace(go.Box(
    #         name=f'{noises[i]:.2f}', 
    #         y=polarizations[i] #[[1,2,3,4,5,6,7,8],[1,2,3,4,5,6,7,8]]
    #     )) 

    fig.update_layout(
    title="Polarización en función del Ruido",
    xaxis_title="Ruido",
    yaxis_title="Polarización", 
    legend_title=f"<b>Referencias</b><br>Densidad: {density:.2f}<br>Número de Partículas: {n}<br>Reǵimen Estacionario: {steady_state}<br>",
    )

    fig['data'][0]['showlegend']=True

    return fig 

def get_polarization_by_noise_with_multiple_n_figure(results, n_array, density, steady_state):

    figure_data = []

    for n in n_array: 
        
        valid_polarizations = get_polarization_by('density', density, 'n', n, results)  
        polarizations = []
        noises = []
        
        polarizations, noises = get_polarization_with_steady_state(valid_polarizations, steady_state, 'noise')

        figure_data.append((polarizations, noises))
    
    fig = go.Figure() 

    colors = ['#636EFA', '#EF553B', '#00CC96', '#AB63FA', '#FFA15A', '#19D3F3', '#FF6692', '#B6E880', '#FF97FF', '#FECB52']

    for fig_idx in range(len(figure_data)): 
        
        polarizations, noises = figure_data[fig_idx]
        # for i in range(len(noises)):

        #     fig.add_trace(go.Box(
        #        name=f'{noises[i]:.2f}', 
        #        y=polarizations[i],
        #        fillcolor=colors[fig_idx % len(colors)]
        #     )) 

        pnp = np.array(polarizations)
        mean = np.mean(pnp, axis=1)
        std = np.std(pnp, axis=1)

        fig.add_trace(go.Scatter(
            x=noises, 
            y=mean,
            name=f'N = {n_array[fig_idx]}',
            fillcolor=colors[fig_idx % len(colors)],
            error_y=dict(
                type='data',
                symmetric=True,
                array=std
            )
        )) 

    fig.update_layout(
        title="Polarización en función del Ruido para múltiples N",
        xaxis_title="Ruido",
        yaxis_title="Polarización", 
        legend_title=f"<b>Referencias</b><br>Densidad: {density:.2f}<br>Régimen Estacionario: {steady_state}<br>",
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
    # polarization_by_noise_with_multiple_n_fig = get_polarization_by_noise_with_multiple_n_figure(results, [100, 200, 300, 400], 0.5, 1200)

    density_n_combinations_options = get_all_combinations(results, ['density', 'n'])
    noise_n_combinations_options = get_all_combinations(results, ['noise', 'n'])
    density_options = get_all_combinations(results, ['density'])
    frames_per_simulation = len(results[0]['polarization'][0])

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

                ## Polarization By Frame (Noise)

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

                ## Polarization By Density

                html.Div(
                    children=[

                        ### Dropdown

                        html.Div(
                            children=[
                                dcc.Dropdown(
                                    id='polarization_by_frame_density_dropdown', 
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
                                html.Div(
                                    children=[
                                        dcc.Graph(
                                            id='polarization_by_frame_density',
                                            # figure=polarization_by_frame_density_fig
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

                        ### Options

                        html.Div(
                            children=[
                                html.Div(
                                    children=[
                                        html.P(
                                            children='Averaging since Frame Nº'
                                        ),
                                        daq.NumericInput(
                                            id='polarization_by_density_averaging_since',
                                            min=0,
                                            max=frames_per_simulation,
                                            value=STARTING_AVERAGING_SINCE
                                        )
                                    ],
                                    style={
                                        "margin-bottom": "20px"
                                    }
                                ),
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

                        ### Options

                        html.Div(
                            children=[
                                html.Div(
                                    children=[
                                        html.P(
                                            children='Averaging since Frame Nº'
                                        ),
                                        daq.NumericInput(
                                            id='polarization_by_noise_averaging_since',
                                            min=0,
                                            max=frames_per_simulation,
                                            value=STARTING_AVERAGING_SINCE
                                        )
                                    ],
                                    style={
                                        "margin-bottom": "20px"
                                    }
                                ),
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
                ), 
                html.Div(
                    children=[

                        ### Options

                        html.Div(
                            children=[
                                html.Div(
                                    children=[
                                        html.P(
                                            children='Averaging since Frame Nº'
                                        ),
                                        daq.NumericInput(
                                            id='polarization_by_noise_with_multiple_n_averaging_since',
                                            min=0,
                                            max=frames_per_simulation,
                                            value=STARTING_AVERAGING_SINCE
                                        ),
                                    ],
                                    style={
                                        "margin-bottom": "20px"
                                    }
                                ),
                                dcc.Dropdown(
                                    id='polarization_by_noise_with_multiple_n_dropdown', 
                                    options=density_options,
                                    value = density_options[0]['value']
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
                                            id='polarization_by_noise_with_multiple_n',
                                            # figure=polarization_by_noise_with_multiple_n_fig
                                        ),  
                                    ],
                                    className='card'
                                ),
                            ],
                            className='wrapper',
                        )
                    ]
                ),
                
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
        Output('polarization_by_frame_density', 'figure'), 
        [Input('polarization_by_frame_density_dropdown', 'value')]
    )
    def update_polarization_by_frame_graph(selected_value):
        (noise, n) = make_tuple(selected_value)

        return get_polarization_by_frame_density_figure(results, noise, n)

    @app.callback(
        Output('polarization_by_density', 'figure'), 
        [
            Input('polarization_by_density_dropdown', 'value'),
            Input('polarization_by_density_averaging_since', 'value')
        ]
    )
    def update_polarization_by_density_graph(selected_value, averaging_since_value):
        (noise, n) = make_tuple(selected_value)

        return get_polarization_by_density_figure(results, noise, n, averaging_since_value)

    @app.callback(
        Output('polarization_by_noise', 'figure'), 
        [
            Input('polarization_by_noise_dropdown', 'value'),
            Input('polarization_by_noise_averaging_since', 'value')
        ]
    )
    def update_polarization_by_noise_graph(selected_value, averaging_since_value):
        (density, n) = make_tuple(selected_value)

        return get_polarization_by_noise_figure(results, density, n, averaging_since_value)

    @app.callback(
        Output('polarization_by_noise_with_multiple_n', 'figure'), 
        [
            Input('polarization_by_noise_with_multiple_n_dropdown', 'value'),
            Input('polarization_by_noise_with_multiple_n_averaging_since', 'value')
        ]
    )
    def update_polarization_by_noise_with_multiple_n_graph(selected_value, averaging_since_value):
        (density, ) = make_tuple(selected_value)
        print(averaging_since_value)
        Ns = list(filter(lambda a: make_tuple(a['value'])[0] == density, density_n_combinations_options))
        Ns = list(map(lambda a: make_tuple(a['value'])[1], Ns))

        return get_polarization_by_noise_with_multiple_n_figure(results, Ns, density, averaging_since_value)

    app.layout = serve_layout

    port = 5000
    app.run_server(host='0.0.0.0', port=port, debug=True)