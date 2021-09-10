import plotly.express as px
import plotly.graph_objects as go

def plot_times_pdf(times): 

    fig = go.Figure(data=[go.Histogram(x=times)]) #TODO esto tiene que ser scatter
    fig.update_traces(opacity=0.75)
    fig.update_layout(
        title="Times PDF",
        xaxis_title="Time",
        yaxis_title="Number of Times Between Collisions"
    )
   
    fig.show()