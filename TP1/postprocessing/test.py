import numpy as np
import matplotlib.pyplot as plt
import mplcursors

figure, axes = plt.subplots()
plt.scatter([2, 3], [4,5],color='red', s=0.5, alpha=0.5)
tt = [1,2]
draw_particle_radius = plt.Circle((2, 4), 0.5, color='k') 
axes.add_artist(draw_particle_radius)
mplcursors.cursor()
def on_add(sel):
    sel.annotation.set(text=tt[sel.target.index])
plt.show()