import plotly.graph_objects as go
import math
import numpy as np

def plot_evacuated_particles_by_time(evacuated_particles_by_simulation, times): 

    fig = go.Figure()

    for simulation_index in range(0, len(evacuated_particles_by_simulation)):
        fig.add_trace(go.Scatter(
            x=times,
            y=evacuated_particles_by_simulation[simulation_index],
            mode='lines+markers', 
            name=f'Simulación = {simulation_index}'
        ))    

    fig.update_layout(
        title="Curva de Descarga",
        xaxis_title="Tiempo (ms)",
        yaxis_title="Número de Partículas Salientes",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=28, 
        )
    )

    fig.show()

def plot_avg_times_by_evacuated_particles(evacuated_particles, avg_times): 

    fig = go.Figure()

     
    fig.add_trace(go.Scatter(
        x=evacuated_particles,
        y=avg_times,
        mode='lines+markers'
    ))    

    fig.update_layout(
        title="Comportamiento promedio del sistema ",
        xaxis_title="Número de Partículas Salientes",
        yaxis_title="Tiempo Promedio (ms)",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=28, 
        )
    )

    fig.show()