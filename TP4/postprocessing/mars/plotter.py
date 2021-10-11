import plotly.graph_objects as go
import math
import numpy as np

def plot_energies_per_time(times_by_dt, energies_by_dt): 

    fig = go.Figure()

    for dt in times_by_dt:
        fig.add_trace(go.Scatter(
            x=times_by_dt[dt],
            y=energies_by_dt[dt],
            mode='lines', 
            name=f'dt = {dt}'
        ))
    
    fig.update_layout(
        title="Energía del sistema en función del tiempo",
        xaxis_title="Tiempo (s)",
        yaxis_title="Energía (K)",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=28, 
        )
    )

    fig.show()

def plot_distance_per_date(min_distances, dt): 

    fig = go.Figure()

    fig.add_trace(go.Scatter(
        x=list(min_distances.keys()),
        y=list(min_distances.values()),
        mode='lines', 
    ))
    
    fig.update_layout(
        title="Distancia Nave-Marte en función de la fecha de salida",
        xaxis_title="Fecha de Despegue",
        yaxis_title="Distancia (km)",
        legend_title=f"<b>Referencias</b> <br> Paso Temporal: {dt}s",
        font=dict( 
            size=28, 
        )
    )

    fig['data'][0]['showlegend']=True

    fig.show()

def plot_spaceship_velocity_per_frame(velocities, times, launch_date, dt): 

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
        legend_title=f"<b>Referencias</b><br> Fecha de salida {launch_date} <br> Paso Temporal {dt}s",
        font=dict( 
            size=28, 
        )
    )

    fig['data'][0]['showlegend']=True
    
    fig.show()

def plot_spaceship_arrival_time_per_velocity(velocities, times, launch_date, dt, save_dt): 

    fig = go.Figure()

    fig.add_trace(go.Scatter(
        x=velocities,
        y=times,
        mode='markers', 
    ))
    
    fig.update_layout(
        title="Variación de V0 ",
        xaxis_title="Velocidad Inicial de la Nave (km/s)",
        yaxis_title="Tiempo de llegada (s)",
        legend_title=f"<b>Referencias</b><br> Fecha de salida {launch_date} <br> Paso Temporal {dt}s <br> Paso Temporal Guardado {save_dt}s",
        font=dict( 
            size=28, 
        )
    )

    fig['data'][0]['showlegend']=True
    
    fig.show()