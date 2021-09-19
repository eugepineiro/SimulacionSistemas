from numpy.lib.ufunclike import fix
import plotly.express as px
import plotly.graph_objects as go
import numpy as np
import math

BIG_PARTICLE_RADIUS = 0.7
SMALL_PARTICLE_RADIUS = 0.2

############## 3.1 & 3.2 PROBABILITIES ############## 

def plot_time_probability_distribution(times, n_array): 
  
    avg_times_by_n = []

    for n in range(len(times)): 
        avg_times = []
        for i in range(len(times[n])-1): 
            avg_times.append(times[n][i+1] - times[n][i])   
        avg_times_by_n.append(avg_times)
    
    avg_collision_time = list(map(lambda a: sum(a)/len(a), avg_times_by_n))
    print("average collison times:")
    for time in avg_collision_time:
        print(format(time, ".3e"))

    collision_freq = list(map(lambda a: int(len(a)/a[-1]), times))
    print(f"\ncollison frequency: {collision_freq}")

    plot_probability_distribution(avg_times_by_n, n_array, 'Tiempo', 'lines+markers', 'Tiempo (s)', bin_size=0.0001) # TODO: Plot bin size and average of collisions frequence

def plot_speed_probability_distribution(speeds_by_n, n_array): #[ [[ ]]] por cada N, 
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

        speeds_by_events_initial = speeds_by_n[n][0]      
        res.append(speeds_by_events_initial)
    
    plot_probability_distribution(res, n_array, 'Modulo de la Velocidad en t=0', 'lines+markers', 'Modulo de la Velocidad (m/s)', bin_size=0.1)
    

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
        legend_title=f"<b>Referencias</b> <br> Ancho de bin {bin_size}",
        font=dict( 
            size=28, 
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

PLOTLY_COLORS = [
    '#1f77b4',  # muted blue
    '#ff7f0e',  # safety orange
    '#2ca02c',  # cooked asparagus green
    '#d62728',  # brick red
    '#9467bd',  # muted purple
    '#8c564b',  # chestnut brown
    '#e377c2',  # raspberry yogurt pink
    '#7f7f7f',  # middle gray
    '#bcbd22',  # curry yellow-green
    '#17becf'   # blue-teal
]

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
            line=dict(color=PLOTLY_COLORS[t % len(PLOTLY_COLORS)]),
            name=f'|v| = {temperature_labels[t]}'           
        ))

        fig.add_shape(type="circle",
            xref="x", yref="y",
            fillcolor=PLOTLY_COLORS[t % len(PLOTLY_COLORS)],
            x0=trajectories_by_t[t][0][-1] - BIG_PARTICLE_RADIUS, 
            y0=trajectories_by_t[t][1][-1] - BIG_PARTICLE_RADIUS, 
            x1=trajectories_by_t[t][0][-1] + BIG_PARTICLE_RADIUS, 
            y1=trajectories_by_t[t][1][-1] + BIG_PARTICLE_RADIUS,
            line_color=PLOTLY_COLORS[t % len(PLOTLY_COLORS)],
            opacity=0.2
        )

    fig.update_layout(
        title="Trayectoria de la Partícula Grande por Temperatura",
        xaxis_title="Posición x (m)",
        yaxis_title="Posición y (m)",
        legend_title=f"<b>Referencias</b> <br> N="+str(number_of_particles),
        font=dict( 
            size=28, 
        )
    )

    fig.update_xaxes(range=[0, 6])
    fig.update_yaxes(range=[0, 6])
   
    fig.show()

############## 3.4 DCM ##############

def plot_dcm(simulations_trajectories, particle_type, number_of_particles, times_gap=1):

    max_time = min(
        list(
            map(
                lambda a: a[-1]['time'],
                simulations_trajectories
            )
        )
    )

    max_time = int(max_time / times_gap) * times_gap

    fixed_times = np.arange(0, max_time+0.0001, times_gap)

    def get_nearest(array, time):
        before = None
        after = None

        for e in array:
            if time >= e['time']:
                before = e
            else: # time < e['time']:
                if after is None:
                    after = e
                else:
                    continue

        if before is None:
            return after
        if after is None:
            return before
        
        return before if (time - before['time']) <= (after['time'] - time) else after

    simulations_positions_at_times = list(
        map(
            lambda positions: list(
                map(
                    lambda time: get_nearest(positions, time),
                    fixed_times
                )
            ),
            simulations_trajectories
        )
    )

    dcs = []

    for sim in simulations_positions_at_times:
        init_pos = {'x': 3,'y': 3} if particle_type == 'BIG' else {'x': sim[0]['x'], 'y': sim[0]['y']}
        dcs_by_sim = []
        for e in sim:
            dcs_by_sim.append((e['x'] - init_pos['x']) ** 2 + (e['y'] - init_pos['y']) ** 2)
        dcs.append(dcs_by_sim)

    dcs = np.array(dcs)

    mean = np.mean(dcs, axis=0)
    std = np.std(dcs, axis=0)

    fig = go.Figure()

    fig.add_trace(go.Scatter(
        x=fixed_times, 
        y=mean,
        mode='lines+markers',
        name=f'DCM',
        error_y=dict(
            type='data',
            symmetric=True,
            array=std
        )
    )) 

    particle_title = 'Particula Grande' if particle_type == 'BIG' else 'Particula Pequeña'
    
    # # option 1
    # last_time = 0
    # last_mean = 0
    # m = 0
    # for idx in range(1, len(fixed_times)):
    #     m += (mean[idx] - last_mean)/(fixed_times[idx] - last_time)
    #     last_time = fixed_times[idx]
    #     last_mean = mean[idx]
    # m /= len(fixed_times)
    # cd = m/2

    # print(f'M1 - Coeficiente de difusión de {particle_title}: {m/2}')

    # # option 2
    # m = 0
    # for idx in range(1, len(fixed_times)):
    #     m += mean[idx]/fixed_times[idx]
    # m /= len(fixed_times)
    # cd = m/2

    # print(f'M2 - Coeficiente de difusión de {particle_title}: {cd}')

    # option 3 https://www.varsitytutors.com/hotmath/hotmath_help/spanish/topics/line-of-best-fit
    # s_xy = sum([fixed_times[i] * mean[i] for i in range(len(fixed_times))])
    # s_x = sum(fixed_times)
    # s_x2 = sum([time ** 2 for time in fixed_times])
    # s_y = sum(mean)
    # n = len(fixed_times)
    # m = ( s_xy - ( (s_x * s_y) / n ) ) / ( s_x2 - ( ( s_x ** 2 ) / n ) )
    # cd = m/2

    # print(f'M3 - Coeficiente de difusión de {particle_title}: {cd}')

    # option 4 - teórica 0

    x = fixed_times
    y = mean

    c = np.arange(0, 0.2, 0.00001)
    
    min_f = (-1, float("inf"))
    
    ec = []
    for value in c:
        ec_value = sum(np.power((y - value*x), 2))
        if ec_value < min_f[1]:
            min_f = (value, ec_value)
        ec.append(ec_value)
    ec = np.array(ec)

    line_x = np.arange(fixed_times[0], fixed_times[-1]+0.001)
    line_y = min_f[0]*line_x

    cd = min_f[0]/2

    fig.add_trace(go.Scatter(
        x=line_x, 
        y=line_y,
        mode='lines+markers',
        name=f'Ajuste modelo lineal (D = {cd:.4f} m<sup>2</sup>/s)'
    ))

    fig.update_layout(
        title=f"Desplazamiento Cuadrático Medio de {particle_title}",
        xaxis_title="Tiempo (s)",
        yaxis_title="DCM (m<sup>2</sup>)",
        legend_title=f"<b>Referencias</b> <br> N={number_of_particles}",
        font=dict( 
            size=28, 
        )
    )
   
    fig.show()

    # E(c)

    fig2 = go.Figure()

    fig2.add_trace(go.Scatter(
        x=c, 
        y=ec,
        mode='lines+markers',
        # name=f'D = {cd:.3f} m^2/s'
    ))

    fig2.add_trace(go.Scatter(
        x=np.array([min_f[0]]), 
        y=np.array([min_f[1]]),
        mode='lines+markers'
    ))

    fig2.update_layout(
        title=f"Ajusto del error para la regresión lineal del Desplazamiento Cuadrático Medio de {particle_title}",
        xaxis_title="c (m<sup>2</sup>/s)",
        yaxis_title="E(c) (m<sup>4</sup>)",
        legend_title=f"<b>Referencias</b>",
        font=dict( 
            size=28, 
        )
    )

    fig2.show()