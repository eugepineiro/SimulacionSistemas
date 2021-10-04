import plotly.graph_objects as go
import math
import numpy as np

def plot_distance_per_date(min_distances): 

    fig = go.Figure()

    fig.add_trace(go.Scatter(
        x=list(min_distances.keys()),
        y=list(min_distances.values()),
        mode='lines', 
    ))
    
    fig.update_layout(
        title="Distancia Nave-Marte en función de la fecha de salida",
        xaxis_title="Fecha",
        yaxis_title="Distancia (km)",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=28, 
        )
    )

    fig.show()

def plot_spaceship_velocity_per_frame(velocities, times): 

    fig = go.Figure()

    fig.add_trace(go.Scatter(
        x=times,
        y=velocities,
        mode='lines', 
    ))
    
    fig.update_layout(
        title="Evolución temporal del módulo de la velocidad de la nave",
        xaxis_title="Tiempo (s)",
        yaxis_title="Módulo de la velocidad (km/s)",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=28, 
        )
    )

    fig.show()