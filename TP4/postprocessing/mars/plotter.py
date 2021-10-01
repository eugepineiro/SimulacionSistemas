import plotly.graph_objects as go
import math
import numpy as np

def plot_distance_per_date(distances, dates): 

    fig = go.Figure()

    fig.add_trace(go.Scatter(
        x=dates,
        y=distances,
        mode='lines', 
    ))
    
    fig.update_layout(
        title="Distancia Nave-Marte en función de la fecha de salida",
        xaxis_title="Tiempo (s)",
        yaxis_title="Posición (m)",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=28, 
        )
    )