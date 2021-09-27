import plotly.graph_objects as go
import math
import numpy as np

def plot_oscillator_position_by_time(integrations_positions, integrations_times, integrations_names):

    fig = go.Figure()

    integrations_times = np.array(integrations_times)

    for integration in range(len(integrations_names)): 
        fig.add_trace(go.Scatter( 
            x=integrations_times, 
            y=integrations_positions[integration] , 
            mode='lines',
            name=f'{integrations_names[integration]}'           
        ))

    # Analytic Solution 

    m = 70
    k = 10**4
    gamma = 100

    r = lambda t: math.exp(-(gamma/(2*m))*t) * np.cos(((k/m) - (gamma**2)/(4*m**2))**(0.5) * t)
    y = [r(t) for t in integrations_times]

    fig.add_trace(go.Scatter(
        x=integrations_times,
        y=y,
        mode='lines',
        name='Analytic solution'
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