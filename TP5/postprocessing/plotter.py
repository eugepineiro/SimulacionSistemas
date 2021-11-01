import plotly.graph_objects as go
from plotly.subplots import make_subplots
import numpy as np

def plot_evacuated_particles_by_time(evacuated_particles, times_by_simulation): 

    fig = go.Figure()

    for simulation_index in range(0, len(times_by_simulation)):
        fig.add_trace(go.Scatter(
            x=times_by_simulation[simulation_index],
            y=evacuated_particles,
            mode='lines+markers', 
            name=f'Simulación {simulation_index}'
        ))    

    fig.update_layout(
        title="Curva de Descarga",
        xaxis_title="Tiempo (s)",
        yaxis_title="Número de Partículas Salientes",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=28, 
        )
    )

    fig.show()

def plot_avg_times_by_evacuated_particles(evacuated_particles, times_by_simulation): 

    times_by_simulation_np = np.array(times_by_simulation)
    mean = np.mean(times_by_simulation_np, axis=0)
    std = np.std(times_by_simulation_np, axis=0)

    fig = go.Figure()
     
    fig.add_trace(go.Scatter(
        x=evacuated_particles,
        y=mean,
        error_y=dict(
            type='data',
            symmetric=True,
            array=std
        ),
        mode='markers'
    ))    

    fig.update_layout(
        title="Comportamiento promedio del sistema ",
        xaxis_title="Número de Partículas Salientes",
        yaxis_title="Tiempo Promedio (s)",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=28, 
        )
    )

    fig.show()

def plot_avg_times_by_evacuated_particles_inverted(evacuated_particles, times_by_simulation): 

    times_by_simulation_np = np.array(times_by_simulation)
    mean = np.mean(times_by_simulation_np, axis=0)
    std = np.std(times_by_simulation_np, axis=0)

    fig = go.Figure()
     
    fig.add_trace(go.Scatter(
        x=mean,
        y=evacuated_particles,
        error_x=dict(
            type='data',
            symmetric=True,
            array=std
        ),
        mode='markers'
    ))    

    fig.update_layout(
        title="Curva de Descarga",
        xaxis_title="Tiempo Promedio (s)",
        yaxis_title="Número de Partículas Salientes",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=28, 
        )
    )

    fig.show()


def plot_flow_rate_by_time_with_multiple_simulations(flow_rates_by_pairs, number_of_particles, target_widths):
    
    fig = go.Figure()

    for i, (escape_times, flow_rates) in enumerate(flow_rates_by_pairs):

        fig.add_trace(go.Scatter(
            x=escape_times,
            y=flow_rates,
            mode='lines+markers', 
            name=f'd: {target_widths[i]}, N: {number_of_particles[i]}'
        ))    

    fig.update_layout(
        title="Caudal en función del tiempo",
        xaxis_title="Tiempo (s)",
        yaxis_title="Q (1/s)",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=28, 
        )
    )

    fig.show() 

def plot_avg_flow_rate_by_target_width(flow_rates_by_pairs, averaging_limits_by_n, number_of_particles, target_widths): 

    mean = []
    std = []

    for i, (escape_times, flow_rates) in enumerate(flow_rates_by_pairs):

        k = str(number_of_particles[i])
        left, right = averaging_limits_by_n[k] if k in averaging_limits_by_n else [-float('inf'), float('inf')]
        # print(f'Averaging from {left} to {right} at {k} particles')
        limited_flow_rates = []

        for j in range(len(flow_rates)):
            et = escape_times[j]
            fr = flow_rates[j]

            if et >= left and et <= right:
                limited_flow_rates.append(fr)

        limited_flow_rates_np = np.array(limited_flow_rates)
        m = np.mean(limited_flow_rates_np, axis=0)
        s = np.std(limited_flow_rates_np, axis=0)
        mean.append(m)
        std.append(s)

    # print(mean)
    # print(std)
    
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

    # fig.data[0].update(xaxis='x2')

    # https://plotly.com/python/reference/layout/
    fig.update_layout(
        title="Caudal Medio en función del número de particulas y el ancho de salida",
        xaxis_title="d (m)",
        yaxis_title="<Q> (1/s)",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=25, 
        )
    )

    fig.show()

def plot_beverloo_adjustment_using_avg_flow_rate_by_target_width(flow_rates_by_pairs, averaging_limits_by_n, number_of_particles, target_widths):
    mean = []
    std = []

    for i, (escape_times, flow_rates) in enumerate(flow_rates_by_pairs):

        k = str(number_of_particles[i])
        left, right = averaging_limits_by_n[k] if k in averaging_limits_by_n else [-float('inf'), float('inf')]
        # print(f'Averaging from {left} to {right} at {k} particles')
        limited_flow_rates = []

        for j in range(len(flow_rates)):
            et = escape_times[j]
            fr = flow_rates[j]

            if et >= left and et <= right:
                limited_flow_rates.append(fr)

        limited_flow_rates_np = np.array(limited_flow_rates)
        m = np.mean(limited_flow_rates_np, axis=0)
        s = np.std(limited_flow_rates_np, axis=0)
        mean.append(m)
        std.append(s)

    fig = go.Figure()

    # queremos ver si ajusta a Q = B d^(3/2)
    # entonces la curva de ajuste es: (y(t) - c*t)) = (B d^(3/2) - c * d)
    c = np.arange(0, 3, 0.0001)

    fbp = np.array(mean)
    tw = np.array(target_widths)

    min_f = (-1, float("inf"))

    ec = []
    for value in c:
        ec_value = sum(np.power((fbp - value*np.power(tw, 3/2)), 2))
        if ec_value < min_f[1]:
            min_f = (value, ec_value)
        ec.append(ec_value)
    ec = np.array(ec)

    print(min_f)
    B = min_f[0]
    print(f"B: {B}")

    fig.add_trace(go.Scatter(
        x=c, 
        y=ec,
        mode='lines+markers', 
        error_y=dict(
            type='data',
            symmetric=True,
            array=std
        )
    ))

    fig.update_layout(
        title=f"Ajusto del error para la regresión lineal sobre el Caudal promedio en función del Ancho de salida (Beverloo)",
        xaxis_title="c (1/(m<sup>3/2</sup> . s))",
        yaxis_title="E(c) (1/(m<sup>3</sup> . s<sup>2</sup>))",
        legend_title=f"<b>Referencias</b>",
        font=dict( 
            size=28, 
        )
    )

    fig.show()

    fig2 = go.Figure()

    fig2.add_trace(go.Scatter(
        x=target_widths, 
        y=mean,
        mode='lines+markers', 
        error_y=dict(
            type='data',
            symmetric=True,
            array=std
        ),
        name='Caudal Medio en función del número de partícula y el ancho de salida'
    ))

    fig2.add_trace(go.Scatter(
        x=target_widths, 
        y=B*np.power(tw, 3/2),
        mode='lines+markers',
        name=f"Ajuste modelo lineal (B = {B:.3f} 1/(m<sup>3/2</sup> . s)) (Q = B.(d)<sup>3/2</sup>)"
    ))

    fig2.update_layout(
        title="Caudal Medio en función del número de particulas y el ancho de salida",
        xaxis_title="d (m)",
        yaxis_title="<Q> (1/s)",
        legend_title=f"<b>Referencias</b> <br>",
        font=dict( 
            size=25, 
        )
    )

    fig2.show()