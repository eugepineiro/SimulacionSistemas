# SimulacionSistemas

## Dinámica Peatonal Dirigida por Paso Temporal

Implementación en Java de un sistema de Dinámica Peatonal para simular el egreso de peatones por una puerta angosta según el Contractile Particle Model (CPM)

### 0. Integrantes 

- [Baiges, Matías](https://github.com/mbaiges)
- [Comerci, Nicolás](https://github.com/ncomerci)
- [Piñeiro, Eugenia](https://github.com/eugepineiro)

### 1. Configuración

Para configurar la simulación se utiliza un archivo de configuración (`/TP5/src/main/resources/config/config.json`). 

#### 1.1 Parámetros

| Parámetro| Descripción                    | Opciones|
| ------------- | ------------------------------ | ------------- |
|"seed"                 | Semilla del generador aleatorio                                                     | número entero |
|"number_of_particles"  | Número de partículas                                                                | número entero |
|"room_height"          | Altura del recinto                                                                  | número entero |
|"room_width"           | Ancho del recinto                                                                   | número entero |
|"target_width"         | Ancho de la puerta                                                                  | número entero |
|"outer_target_dist"    | Distancia del target exterior                                                       | número entero |
|"max_time"             | Tiempo máximo de simulación                                                         | número entero  |
|"save_factor"          | Factor de guardado                                                                  | número decimal  |
|"min_radius"           | mínimo radio de partícula                                                           | número decimal  |
|"max_radius"           | máximo radio de partícula                                                           | número decimal  |
|"tau"                  | parámetro para actualizar el paso temporal                                          | número decimal  |
|"escape_velocity"      | velocidad de escape                                                                 | número decimal  |
|"max_desired_velocity" | máxima velocidad deseada                                                            |número decimal |
|"loading_bar"          | mostrar barra de carga                                                              | booleano  |
|"multiple_simulations" | Simular con distintos pasos temporales                                              |         |
|"activated"            | activado                                                                            | booleano |
|"seeds"                | Semillas del generador aleatorio de partículas                                      | vector de números enteros |   
|"width_and_particles"  | Simular con distintos pares de ancho de salida y cantidad de particulas respectivos |  
|"activated"            | activado                                                                            | booleano |
|"target_width"         | ancho de salida                                                                     | número decimal | 
|"number_of_particles"  | número de partículas                                                                | número entero | 
|"seeds"                | Semilla del generador aleatorio                                                     | número entero | 


#### 1.2 Ejemplo

```json
{
  "seed": 904209900,
  "number_of_particles": 200,
  "room_height": 20,
  "room_width": 20,
  "target_width": 1.2,
  "outer_target_dist": 10,
  "outer_target_width": 3,
  "max_time": 1000,
  "save_factor": 1,
  "min_radius": 0.15,
  "max_radius": 0.32,
  "beta": 0.8,
  "tau": 0.5,
  "escape_velocity": 2,
  "max_desired_velocity": 2,
  "loading_bar": true,
  "multiple_simulations": {
    "activated": false,
    "seeds": [100, 200, 300, 400, 500]
  },
  "multiple_width_and_particles": {
    "activated": false,
    "target_width": [1.2, 1.8, 2.4, 3.0],
    "number_of_particles": [200, 260, 320, 380],
    "seeds": [100, 200, 300, 400, 500]
  }
}
```

### 3. Compilación 

#### 3.1 Simulación (Maven)

```bash
$> mvn clean install
```

### 4. ar.edu.itba.ss.Postprocessing

Al configurar el `config.json` activando el postprocesamiento, se obtendrá como output el archivo `/TP5/src/main/resources/postprocessing/SdS_TP5_2021Q2G01_results.json` que luego será postprocesado:

```json
  ...
  "multiple_simulations": {
    "activated": true,
    ...
  }
  "multiple_width_and_particles": {
    "activated": true,
    ...
  }
``` 

```bash
$> python3 ./TP3/postprocessing/main.py
```

### 5. Animaciones 
Al configurar el `config.json` desactivando el postprocesamiento, se obtendrá como output el archivo `/TP5/src/main/resources/ovito/SdS_TP5_2021Q2G01_output.xyz` que luego podrá abrirse con Ovito seteando la configuración `/TP5/src/main/resources/config/ovitoconfig.ovito`:

```json
  ...
  "multiple_simulations": {
    "activated": false,
    ...
  }
  "multiple_width_and_particles": {
    "activated": false,
    ...
  }
``` 

Se pueden encontrar distintas animaciones en Youtube, cuyos links se encuentran en la presentación 
