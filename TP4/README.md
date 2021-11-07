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
|"system"     |  Sistema a simular  | 'oscillator', 'mars', 'jupiter' |
|"integration"     |  Integrador  | 'verlet_original', 'beeman', 'gear' |
|"dt"     |  Paso temporal en segundos  | número entero |
|"launch_date"     |  Fecha de despegue de la nave   | "YYYY-MM-DD HH:MM:SS" |
|"save_factor"     |  Paso Temporal para guardar los datos en archivos usados en el postprocessing  | número entero |
|"spaceship_initial_speed"     |  Rapidez inicial de la nave espacial  | número decimal |
|"loading_bar"     |  mostrar barra de carga   | booleano  |
|   "multiple_dt"  |   Simular con distintos pasos temporales   |  |
|   "activated"  |   utilizar   | booleano |
|   "min_exp"  | mínimo exponente   | número decimal |
|   "max_exp"  | máximo exponente   | número decimal |
|   "increment"  | incremento del paso temporal | número entero |
|   "multiple_dates"  |   Simular con distintos valores de fecha de despegue de la nave   |  |
|   "activated"  |   utilizar   | booleano |
|   "min"  | mínima fecha | "YYYY-MM-DD HH:MM:SS" |
|   "max"  | máxima fecha | "YYYY-MM-DD HH:MM:SS" |
|   "increment"  | incremento de fechas | número decimal en segundos |
|   "multiple_velocities":   |   Hacer múltiples simulaciones con distintas velocidades   |  |
|   "activated"   |  utilizar   | booleano | 
|   "min"  | mínima velocidad | "YYYY-MM-DD HH:MM:SS" |
|   "max"  | máxima velocidad | "YYYY-MM-DD HH:MM:SS" |
|   "increment"  | incremento entre velocidades | número decimal |


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

Al configurar el `config.json` activando el postprocesamiento, se obtendrá como output el archivo `/TP4/src/main/resources/postprocessing/SdS_TP3_2021Q2G01_results.json` que luego será postprocesado:

```json
  ...
  "multiple_dt": {
    "activated": true,
    ...
  }
  "multiple_dates": {
    "activated": true,
    ...
  }
  "multiple_velocities": {
    "activated": true,
    ...
  }
``` 

```bash
$> python3 ./TP3/postprocessing/main.py
```

### 5. Animaciones 
Al configurar el `config.json` desactivando el postprocesamiento, se obtendrá como output el archivo `/TP4/src/main/resources/ovito/SdS_TP4_2021Q2G01_output.xyz` que luego podrá abrirse con Ovito seteando la configuración `/TP4/src/main/resources/config/ovitoconfig.ovito`:

```json
  ...
  "multiple_dt": {
    "activated": true,
    ...
  }
  "multiple_dates": {
    "activated": true,
    ...
  }
  "multiple_velocities": {
    "activated": true,
    ...
  }
``` 

Se pueden encontrar distintas animaciones en Youtube, cuyos links se encuentran en la presentación 
