import plotly.express as px
import plotly.graph_objects as go

def plot_times_pdf(times): 

    fig = go.Figure(data=[go.Histogram(x=times)])
    fig.update_traces(opacity=0.75)
    fig.show()


