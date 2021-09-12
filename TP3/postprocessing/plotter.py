import plotly.express as px
import plotly.graph_objects as go
import numpy as np


def plot_time_probability_distribution(events): 
 
    avg_times = []
    for i in range(len(events)-1): 
        avg_times.append( events[i+1].time - events[i].time)

    plot_probability_distribution(avg_times)

def plot_speed_probability_distribution(events): 

    # pd de la rapidez de las particulas en el ultimo tercio de la simulacion 
        
    last_third_events = events[len(events)/3]
           


def plot_probability_distribution(data): 
    
    #data = [ {time: [1,2,3], n: 100}, {time: [1,2,3], n: 125}] o directamente pasarle un array solo con los times y otro solo con los N

    fig = go.Figure()

    #for i in range(len(data)): # por cada valor de N
#
    #    #esto deberia ser un scatter 
    #    fig.add_trace(go.Histogram(
    #        x=data, histnorm='probability',
    #        xbins=dict(start=np.min(data), 
    #                end=np.max(data)),
    #        marker=dict(color='rgb(5, 25, 100)'))
    #    )

    fig.add_trace(go.Scatter( 
        x=list( dict.fromkeys(data)), 
        y=get_probability_distribution(data), 
        mode='markers',           
    ))
    
    get_probability_distribution([1,1,2,2,4, 0])
    
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
    data_set = list( dict.fromkeys(data) )

    print(data_set)

    for n in data_set: 
        probabilities.append(data.count(n)/len(data))
 
    return probabilities
         