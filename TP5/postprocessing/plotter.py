import plotly.graph_objects as go
from plotly.subplots import make_subplots
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

def plot_avg_flow_rate_by_target_width(flow_rates_by_pairs, number_of_particles, target_widths): 

    flow_rates_np = np.array(flow_rates_by_pairs)
    mean = np.mean(flow_rates_np, axis=1)
    std = np.std(flow_rates_np, axis=1)
    
    fig = go.Figure()

    #fig = make_subplots(specs=[[{"secondary_y": True}]])

    #fig.update_layout(xaxis2= {'anchor': 'y', 'overlaying': 'x', 'side': 'top'},yaxis_domain=[0, 0.94]) 
    
    fig.add_trace(go.Scatter(
        x=target_widths, 
        y=mean,
        mode='lines+markers', 
        error_y=dict(
            type='data',
            symmetric=True,
            array=std
        )
    )) 

   #fig.add_trace(go.Scatter(
   #    x=number_of_particles, 
   #    y=mean,
   #    mode='lines+markers', 
   #    error_y=dict(
   #        type='data',
   #        symmetric=True,
   #        array=std
   #    )
   #), secondary_y=False)

    fig.data[0].update(xaxis='x2')

    # https://plotly.com/python/reference/layout/
    fig.update_layout(
        title="Caudal Medio en función del número de particulas y el ancho de salida",
        xaxis_title="Ancho de Salida (m)",
        yaxis_title="Caudal Medio (1/m/s)",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=25, 
        )
    )

    fig.show()