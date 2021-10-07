import plotly.graph_objects as go
import math
import numpy as np

def plot_oscillator_position_by_time(integrations_positions, analytical_y, integrations_times, integrations_names):

    fig = go.Figure()

    integrations_times = np.array(integrations_times)

    for integration in range(len(integrations_names)): 
        fig.add_trace(go.Scatter( 
            x=integrations_times, 
            y=integrations_positions[integration] , 
            mode='lines',
            name=f'{integrations_names[integration]}'           
        ))

    fig.add_trace(go.Scatter(
        x=integrations_times,
        y=analytical_y,
        mode='lines',
        name='Analytic solution',
    ))
    
    fig.update_layout(
        title="Posición en función del Tiempo",
        xaxis_title="Tiempo (s)",
        yaxis_title="Posición (m)",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=28, 
        )
    )

    fig.show()

def plot_oscillator_error_by_dt(dts, errors):
    fig = go.Figure()

    for intg in errors:
        fig.add_trace(go.Scatter( 
            x=dts, 
            y=errors[intg], 
            mode='lines',
            name=f'{intg}'
        ))

    fig.update_layout(
        title="MSE en función del dt",
        xaxis_title="dt",
        yaxis_title="mse",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=28, 
        )
    )

    fig.update_yaxes(type="log")

    fig.show()