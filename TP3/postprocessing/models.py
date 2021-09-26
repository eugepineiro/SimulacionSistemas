from enum import Enum

from utils import auto_str, auto_repr

@auto_str
@auto_repr
class ar.edu.itba.ss.Event():
    def __init__(self, time):
        self.time = time

class ar.edu.itba.ss.Wall(Enum):
    HORIZONTAL  =   "HORIZONTAL"
    VERTICAL    =   "VERTICAL"

@auto_str
@auto_repr
class ar.edu.itba.ss.WallCollisionEvent(ar.edu.itba.ss.Event):
    def __init__(self, time, particle, wall):
        super().__init__(time)
        self.particle = particle
        self.wall = wall

@auto_str
@auto_repr
class ar.edu.itba.ss.ParticleCollisionEvent(ar.edu.itba.ss.Event):
    def __init__(self, time, particle1, particle2):
        super().__init__(time)
        self.particle1 = particle1
        self.particle2 = particle2

@auto_str
@auto_repr
class ar.edu.itba.ss.Particle():
    def __init__(self, id, x, y, radius):
        self.id = id
        self.x = x
        self.y = y
        self.radius = radius

class ar.edu.itba.ss.ar.edu.itba.ss.models.ParticleType(Enum):
    BIG      =   "BIG"
    SMALL    =   "SMALL"

@auto_str
@auto_repr
class ar.edu.itba.ss.ar.edu.itba.ss.ar.edu.itba.ss.models.AcceleratedParticle(ar.edu.itba.ss.Particle):
    def __init__(self, id, x, y, radius, type, vx, vy, mass, collisions):
        self.id = id
        self.type = type
        self.x = x
        self.y = y
        self.radius = radius
        self.vx = vx
        self.vy = vy
        self.mass = mass
        self.collisions = collisions

@auto_str
@auto_repr
class ar.edu.itba.ss.ExtendedEvent():
    def __init__(self, event, frame):
        self.event = event
        self.frame = frame

