import plotly.express as px
import plotly.graph_objects as go
import numpy as np
import math

############## 3.1 & 3.2 PROBABILITIES ############## 

def plot_time_probability_distribution(times, n_array): 
  
    avg_times_by_n = []

    for n in range(len(times)): 
        avg_times = []
        for i in range(len(times[n])-1): 
            avg_times.append(times[n][i+1] - times[n][i])   
        avg_times_by_n.append(avg_times)
    

    plot_probability_distribution(avg_times_by_n, n_array, 'Tiempo', 'lines+markers', 'Tiempo (s)', bin_size=0.0001) # TODO: Plot bin size and average of collisions frequence

def plot_speed_probability_distribution(speeds_by_n, n_array): #[ [[ ]]]
    # pd de la rapidez de las particulas en el ultimo tercio de la simulacion 

    res = []
  
    for n in range(len(speeds_by_n)): 
    
        speeds_by_events = speeds_by_n[n]
        speeds_by_events_final_third = speeds_by_events[-int(math.floor(len(speeds_by_events)/3)):] # last third 

        speeds = []

        for event_speeds in speeds_by_events_final_third:
  
            for s in event_speeds:
                speeds.append(s)  
     
        res.append(speeds)
    
    plot_probability_distribution(res, n_array, 'Modulo de la Velocidad en el último tercio', 'lines+markers', 'Modulo de la Velocidad (m/s)', bin_size=0.1)

def plot_speed_probability_distribution_initial_time(speeds_by_n, n_array): #[ [[ ]]]
    # pd de la rapidez de las particulas en t = 0

    res = []
  
    for n in range(len(speeds_by_n)): 
    
        speeds_by_events = speeds_by_n[n]
        speeds_by_events_final_third = speeds_by_events[0] 

        speeds = []
 
  
        for s in speeds_by_events_final_third:
            speeds.append(s)  
     
        res.append(speeds)
    
    plot_probability_distribution(res, n_array, 'Modulo de la Velocidad en t=0', 'markers', 'Modulo de la Velocidad (m/s)', bin_size=0.001)
    

def plot_probability_distribution(data, n_array, fig_title, mode, x_axis_legend, bin_size=0.01): 

    new_data_by_n = []

    for n in range(len(data)): 
        d = {}
        for value in data[n]:
            idx = int(value/bin_size)
            if not idx in d:
                d[idx] = 0
            d[idx] += 1

        new_data = []

        for k in sorted(d.keys()):
            for i in range(0, d[k]):
                new_data.append(k*bin_size + bin_size/2)
            
        new_data_by_n.append(new_data)

    fig = go.Figure()
    
 
    for n in range(len(data)): 
        fig.add_trace(go.Scatter( 
            x=list(dict.fromkeys(new_data_by_n[n])), 
            y=get_probability_distribution(new_data_by_n[n]), 
            mode=mode,
            name=f'N = {n_array[n]}'           
        ))
    
    fig.update_layout(
    title="Distribución de la probabilidad del " + fig_title,
    xaxis_title=x_axis_legend,
    yaxis_title="Probabilidad",
    legend_title=f"Referencias\n",
    font=dict( 
        size=20, 
        )
    )

    if(fig_title == 'Tiempo'):
        fig.update_yaxes(type="log")
   
    fig.show()

def get_probability_distribution(data): #tiene que haber ya una funcion de python que lo haga solo.. y se le pasa al eje y del scatter
    
    probabilities = [] 
    data_set = list(dict.fromkeys(data))
    len_data = len(data)
    
    for n in data_set: 
        probabilities.append(data.count(n)/len_data)
 
    return probabilities

############## 3.3 TEMPERATURE ############## 

def plot_big_particle_trajectories(trajectories_by_t, temperature_array, number_of_particles):

    temperature_labels = list(map(
        lambda a: f"[{a[0]:.2f}, {a[1]:.2f})",
        temperature_array
    ))

    fig = go.Figure()

    for t in range(len(trajectories_by_t)): 
        
        fig.add_trace(go.Scatter( 
            x=trajectories_by_t[t][0] , 
            y=trajectories_by_t[t][1], 
            mode='lines',
            name=f'|v| = {temperature_labels[t]}'           
        ))

    fig.update_layout(
    title="Trayectoria de la Partícula Grande por Temperatura",
    xaxis_title="Posición x",
    yaxis_title="Posición y",
    legend_title=f"Referencias<br> N="+str(number_of_particles),
    font=dict( 
        size=20, 
        )
    )
   
    fig.show()

############## 3.4 DCM ##############

def plot_dcm(dcm, times): 

    fig = go.Figure()

    pnp = np.array(dcm)
    mean = np.mean(pnp, axis=1)
    std = np.std(pnp, axis=1)

    fig.add_trace(go.Scatter(
        x=times, 
        y=mean,
        mode='lines+markers',
        name=f'Densidad',
        error_y=dict(
            type='data',
            symmetric=True,
            array=std
        )
    )) 
    
    fig.update_layout(
    title="Desplazamiento Cuadrático Medio",
    xaxis_title="Tiempo",
    yaxis_title="DCM",
    legend_title=f"Referencias\n",
    font=dict( 
        size=20, 
        )
    )
   
    fig.show()