import plotly.express as px
import plotly.graph_objects as go
import numpy as np
import math


def plot_time_probability_distribution(times, n_array): 
 
    
    avg_times_by_n = []

    for n in range(len(times)): 
        avg_times = []
        for i in range(len(times[n])-1): 
            avg_times.append(times[n][i+1] - times[n][i])   
        avg_times_by_n.append(avg_times)
    

    plot_probability_distribution(avg_times_by_n, n_array, bin_size=0.001)

def plot_speed_probability_distribution(speeds, n_array): 

    # pd de la rapidez de las particulas en el ultimo tercio de la simulacion 
    last_third_events_by_n = []

    for n in range(len(speeds)): 

        last_third_events_by_n.append(speeds[-int(math.floor(len(n_array)/3)):]) 

    plot_probability_distribution(last_third_events_by_n, n_array, bin_size=0.0005)
           


def plot_probability_distribution(data, n_array, bin_size=0.01): 

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
            mode='lines+markers',
            name=f'N = {n_array[n]}'           
        ))
    
    fig.update_layout(
    title="Times Probability Distribution",
    xaxis_title="Time",
    yaxis_title="Probability",
    legend_title=f"References\n",
    font=dict( 
        size=20, 
        )
    )
   
    fig.show()

def get_probability_distribution(data): #tiene que haber ya una funcion de python que lo haga solo.. y se le pasa al eje y del scatter
    
    probabilities = [] 
    data_set = list(dict.fromkeys(data))
    len_data = len(data)
    
    for n in data_set: 
        probabilities.append(data.count(n)/len_data)
 
    return probabilities
         