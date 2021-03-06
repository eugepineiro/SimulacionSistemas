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
            name=f'dt = {dt}s'
        ))
        
        variation = energies_by_dt[dt][-1] - energies_by_dt[dt][0]
        perc_variation = variation / energies_by_dt[dt][0]
        print(f'dt: {dt} => Variación: {variation}, Variacion percentual: {perc_variation}')

    fig.update_layout(
        title="Energía del sistema en función del tiempo",
        xaxis_title="Tiempo (s)",
        yaxis_title="Energía (K)",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=28, 
        )
    )

    #fig.update_yaxes(type="log")

    fig.show()

def plot_vp_per_dt(dt_array, energies_by_dt): 

    fig = go.Figure()

    vp_array = []

    for dt in dt_array:
        variation = energies_by_dt[dt][-1] - energies_by_dt[dt][0]
        # energies_after  = np.array(energies_by_dt[dt][1:])
        # energies_before = np.array(energies_by_dt[dt][:-1])
        # variation = sum(abs(energies_after - energies_before))
        
        perc_variation = variation / energies_by_dt[dt][0]
        vp_array.append(abs(perc_variation))
        print(f'dt: {dt} => Variación: {variation}, Variacion percentual: {perc_variation}')
  
    fig.add_trace(go.Scatter(
        x=dt_array,
        y=vp_array,
        mode='lines', 
        name=f'dt = {dt}s'
    ))    
        
    fig.update_layout(
        title="Variación Porcentual de la energía en función del Paso Temporal",
        xaxis_title="dt",
        yaxis_title="Variación Porcentual de la Energía (%)",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=28, 
        )
    )

    fig.update_yaxes(type="log", exponentformat='power')
    fig.update_xaxes(type="log", exponentformat='power')

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