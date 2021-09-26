import plotly.graph_objects as go


def plot_oscillator_position_by_time(integrations_positions, integrations_times, integrations_names):

    fig = go.Figure()

    for integration in range(len(integrations_names)): 
        fig.add_trace(go.Scatter( 
            x=integrations_times[integration], 
            y=integrations_positions[integration] , 
            mode='lines',
            name=f'{integrations_names[integration]}'           
        ))
    
    fig.update_layout(
        title="Posición en función del Tiempo",
        xaxis_title="Tiempo (s)",
        yaxis_title="Posición (m)",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=28, 
        )
    )