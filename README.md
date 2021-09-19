# SimulacionSistemas

## Dinámica Molecular Dirigida por Eventos
### Movimiento Browniano

Implementación en Java de Movimiento Browniano utilizado para entender y predecir  las propiedades de los sistemas físicos como la dinámica microscópica de gases, medios granulares, entre otros.


### 0. Integrantes 

- [Baiges, Matías](https://github.com/mbaiges)
- [Comerci, Nicolás](https://github.com/ncomerci)
- [Piñeiro, Eugenia](https://github.com/eugepineiro)

### 1. Configuración

Para configurar la simulación se utiliza un archivo de configuración (`/TP3/src/main/resources/config/config.json`). 

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
  "seed": 100,
  "n_number_of_particles": 140,
  "l_grid_side": 6,
  "max_events": 200000,
  "min_speed": 0,
  "max_speed": 2,
  "multiple_n": {
    "activated": true,
    "values": [110, 125, 140]
  },
  "multiple_temperatures": {
    "activated": false,
    "speeds_ranges": [
      [0, 1],
      [1, 2],
      [2, 3]
    ]
  },
  "multiple_simulations": {
    "activated": false,
    "seeds": [100, 200, 300, 400, 500]
  }
}
```

### 3. Compilación 

#### 3.1 Simulación (Maven)

```bash
$> mvn clean install
```

### 4. Postprocessing

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
