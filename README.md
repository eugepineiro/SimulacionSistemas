# SimulacionSistemas

## Dinámica Molecular Dirigida por Paso Temporal
### Oscilador Puntual Amortiguado y Viaje a otro Planeta

Implementación en Java de un oscilador puntual amortiguado utilizando distintos métodos de integración (Verlet, Beeman y Gear Predictor-Corrector de orden 5).

Luego, se simuló un sistema gravitatorio para evaluar la interacción de distintas partículas que interactúan mediante fuerzas que dependen de la distancia. El mismo consiste en el despegue de una nave espacial con destino a Marte o a Júpiter, observando la fecha óptima de salida, el tiempo óptimo de llegada y la rapidez inicial de la nave para impactar en cada planeta.    


### 0. Integrantes 

- [Baiges, Matías](https://github.com/mbaiges)
- [Comerci, Nicolás](https://github.com/ncomerci)
- [Piñeiro, Eugenia](https://github.com/eugepineiro)

### 1. Configuración

Para configurar la simulación se utiliza un archivo de configuración (`/TP4/src/main/resources/config/config.json`). 

#### 1.1 Parámetros

| Parámetro| Descripción                    | Opciones|
| ------------- | ------------------------------ | ------------- |
|"seed"     |  Semilla para la generación random de partículas  | número entero |
|"n_number_of_particles"     |  Cantidad de partículas a generar  | número entero |
|"l_grid_side"     |  Longitud del lado de la grilla  | número entero |
|"max_events"     |  Cantidad de eventos máxima para finalizar la ejecución del programa en caso de que la partícula grande aún no haya colisionado con una pared   | número entero |
|"min_speed"     |  Mínima velocidad absoluta de las partículas  | número entero |
|"max_speed"     |  Màxima velocidad absoluta de las partículas  | número entero |
|"record_small_particles_positions"     |  postprocesamiento   |  |
|   "multiple_n"  |   Simular con distintos números de partículas   |  |
|   "activated"  |   utilizar   | booleano |
|   "values"  |  números de partículas   | [número entero, número entero, ...] |
|   "multiple_temperatures"  |   Simular con distintos valores de temperatura   |  |
|   "activated"  |   utilizar   | booleano |
|   "speeds_ranges"   |   Rango de velocidades absolutas a utilizar   | [[entero, entero]] |
|   "multiple_simulations":   |   Hacer múltiples simulaciones   |  |
|   "activated"   |  utilizar   | booleano |
|   "seeds"   |  Semillas a utilizar   | [número entero, número entero, ...] | 


#### 1.2 Ejemplo

```json
{
  "system": "mars",
  "integration": "verlet_original",
  "dt": 600,
  "launch_date": "2021-09-24 00:00:00",
  "max_time": 31536000,
  "save_factor": 1,
  "spaceship_initial_speed": 8,
  "loading_bar": true,
  "multiple_dt": {
    "activated": true,
    "min_exp": 600,
    "max_exp": 601,
    "increment": 20
  },
  "multiple_dates": {
    "activated": false,
    "min": "2022-09-15 00:00:00",
    "max": "2022-09-15 02:00:00",
    "increment": 600
  },
  "multiple_velocities": {
    "activated": false,
    "min": 8,
    "max": 8.01,
    "increment": 0.001
  }
}
```

### 3. Compilación 

#### 3.1 Simulación (Maven)

```bash
$> mvn clean install
```

### 4. ar.edu.itba.ss.Postprocessing

Al configurar el `config.json` activando el postprocesamiento, se obtendrá como output el archivo `/TP3/src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results.json` que luego será postprocesado:

```json
  ...
  "multiple_n": {
    "activated": true,
    ...
  }
  "multiple_temperatures": {
    "activated": true,
    ...
  }
  "multiple_simulations": {
    "activated": true,
    ...
  }
``` 

```bash
$> python3 ./TP3/postprocessing/main.py
```

### 5. Animaciones 
Al configurar el `config.json` desactivando el postprocesamiento, se obtendrá como output el archivo `/TP3/src/main/resources/ovito/SdS_TP3_2021Q2G01_output.xyz` que luego podrá abrirse con Ovito seteando la configuración `/TP3/src/main/resources/config/ovitoconfig.ovito`:

```json
  ...
  "multiple_n": {
    "activated": false,
    ...
  }
  "multiple_temperatures": {
    "activated": false,
    ...
  }
  "multiple_simulations": {
    "activated": false,
    ...
  }
``` 

Se pueden encontrar distintas animaciones en Youtube, cuyos links se encuentran en la presentación 
