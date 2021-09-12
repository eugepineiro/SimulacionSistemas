import plotly.express as px
import plotly.graph_objects as go
import numpy as np

def plot_probability_distribution(data, var): 
    
    #data = [ {time: [1,2,3], n: 100}, {time: [1,2,3], n: 125}] o directamente pasarle un array solo con los times y otro solo con los N

    fig = go.Figure()

    for i in range(len(data)): # por cada valor de N

        #esto deberia ser un scatter 
        fig.add_trace(go.Histogram(
            x=data[var], histnorm='probability',
            xbins=dict(start=np.min(data), 
                    end=np.max(data)),
            marker=dict(color='rgb(5, 25, 100)'))
        )

    #fig.add_trace(go.Scatter( 
    #    x=list( dict.fromkeys(times )), 
    #    y=get_probability_distribution(times), 
    #    mode='markers',           
    #)
    #)
#
    #get_probability_distribution([1,1,2,2,4, 0])
#
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

    for n in data_set: 
        probabilities.append(data.count(n)/len(data))
 
    return probabilities
         