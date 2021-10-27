import plotly.graph_objects as go
import math
import numpy as np

def plot_evacuated_particles_by_time(evacuated_particles_by_simulation, times): 

    fig = go.Figure()

    for simulation in evacuated_particles_by_simulation:
        fig.add_trace(go.Scatter(
            x=times,
            y=evacuated_particles_by_simulation[simulation],
            mode='lines+markers', 
            name=f'Simulación = {simulation}'
        ))    

    fig.update_layout(
        title="Curva de Descarga",
        xaxis_title="Tiempo (s)",
        yaxis_title="Número de Partículas Salientes",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=28, 
        )
    )

    fig.show()