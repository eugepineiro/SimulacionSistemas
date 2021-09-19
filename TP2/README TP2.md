# SimulacionSistemas

## Autómata Off-Lattice 
### Bandadas de Agentes Autopropulsados 

Implementación en Java de un autómata celular Off-Lattice para analizar el comportamiento cooperativo entre partículas.

### 0. Integrantes 

- [Baiges, Matías](https://github.com/mbaiges)
- [Comerci, Nicolás](https://github.com/ncomerci)
- [Piñeiro, Eugenia](https://github.com/eugepineiro)

### 1. Configuración

Para configurar la simulación se utiliza un archivo de configuración (`/TP2/src/main/resources/config/config.json`). 

#### 1.1 Parámetros

| Parámetro| Descripción                    | Opciones|
| ------------- | ------------------------------ | ------------- |
|"seed"     |  Semilla para la generación random de partículas  | número entero |
|"m_grid_dimension"     |  Dimensión de la grilla MxM para la búsqueda de vecinos con el CellIndexMethod  | número entero |
|"r_interaction_radius"     |  Radio de interacción entre partículas  | número entero |
|"n_number_of_particles"     |  Cantidad de partículas a generar  | número entero |
|"frames"     |  Intervalor de tiempo (las animaciones se realizaron con 60 frames por segundo)  | número entero |
|"l_grid_side"     |  Longitud del lado de la grilla  | número entero |
|"speed"     |  Velocidad absoluta de las partículas  | número entero |
|"noise_amplitude"     |  Amplitud del ruido  | número decimal |
|"polarization"     | Parámetros para calcular la polarización en el postprocesamiento   |  |
|   "activated"   |   Activar postprocesamiento   | booleano |
|   "number_of_simulations"   |   Cantidad de Simulaciones   | número entero |
|   "number_of_particles_range"   |   Rango de partículas a generar   | [entero, entero] |
|   "number_of_particles_increase"   |   Incremento para el rango de partículas   | número de partículas |
|   "density_range"   |   Rango de densidad   | [decimal, decimal] |
|   "density_increase"   |   Incremento para el rango de densidad   | número decimal |
|   "noise_range"   |   Rango de ruido   | [decimal, decimal] |
|   "noise_increase"   |   Incremento para el rango de ruido   | número decimal |


#### 1.2 Ejemplo

```json
{
  "seed": 400,
  "m_grid_dimension": 10,
  "r_interaction_radius": 1,
  "n_number_of_particles": 300,
  "frames": 2000,
  "l_grid_side": 25,
  "speed": 0.03,
  "noise_amplitude": 0.1,
  "polarization": {
    "activated": false,
    "number_of_simulations": 1,
    "number_of_particles_range": [300, 301],
    "number_of_particles_increase": 10,
    "density_range": [0.25, 6.1],
    "density_increase": 0.25,
    "noise_range": [0.8, 0.9],
    "noise_increase": 0.3
  }
}
```

### 2. Instalación 

#### 2.1 Postprocesamiento (Python)

```bash
$> pip3 install -r requirements.txt
```

### 3. Compilación 

#### 3.1 Simulación (Maven)

```bash
$> mvn clean install
```

### 4. Postprocessing

Al configurar el `config.json` activando el postprocesamiento, se obtendrá como output el archivo `/TP2/src/main/resources/postprocessing/SdS_TP2_2021Q2G01_results.json` que luego será postprocesado:

```json
  ...
  "polarization": {
    "activated": true,
  ...
```

```bash
$> python3 ./TP2/postprocessing/main.py
```
#### Se abrirá un dashboard en //localhost:5000 que mostrará gráficos interactivos de: 
- Polarización en función del tiempo para múltiples ruidos 
- Polarización en función del ruido
- Polarización en función del ruido para múltiples cantidades de partículas 
- Polarización en función del tiempo para múltiples densidades
- Polarización en función de la densidad

Para todos estos gráficos se podrán variar los parámetros utilizando selectores.

### 5. Animaciones 
Al configurar el `config.json` desactivando el postprocesamiento, se obtendrá como output el archivo `/TP2/src/main/resources/ovito/SdS_TP2_2021Q2G01_output.xyz` que luego podrá abrirse con Ovito seteando la configuración `/TP2/src/main/resources/config/ovitoconfig.ovito`:

```json
  ...
  "polarization": {
    "activated": false,
  ...
```
Se pueden encontrar distintas animaciones en Youtube, cuyos links se encuentran en el `/TP2/docs/SdS_TP2_2021Q2G01_Informe.tex`
