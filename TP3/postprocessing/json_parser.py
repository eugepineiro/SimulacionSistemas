from models import ar.edu.itba.ss.ExtendedEvent, ar.edu.itba.ss.Event, ar.edu.itba.ss.WallCollisionEvent, ar.edu.itba.ss.ParticleCollisionEvent, ar.edu.itba.ss.Wall, ar.edu.itba.ss.ar.edu.itba.ss.ar.edu.itba.ss.models.AcceleratedParticle, ar.edu.itba.ss.ar.edu.itba.ss.models.ParticleType

def parse_extended_events(extended_events):
    res = []

    for ev in extended_events:
        
        # ar.edu.itba.ss.Event
        event = ev['event']
        new_event = ar.edu.itba.ss.Event(event['time'])

        if event['type'] == 'WALL_COLLISION':
            wall = ar.edu.itba.ss.Wall.VERTICAL if event['wall'] == 'VERTICAL' else ar.edu.itba.ss.Wall.HORIZONTAL
            
            particle_raw = event['particle']
            particle_type = ar.edu.itba.ss.ar.edu.itba.ss.models.ParticleType.BIG if particle_raw['type'] == 'BIG' else ar.edu.itba.ss.ar.edu.itba.ss.models.ParticleType.SMALL
            particle = ar.edu.itba.ss.ar.edu.itba.ss.ar.edu.itba.ss.models.AcceleratedParticle(particle_raw['id'], particle_raw['x'], particle_raw['y'], particle_raw['radius'], particle_type, particle_raw['vx'], particle_raw['vy'], particle_raw['mass'], particle_raw['collisions'])
            
            wall_collision_event = ar.edu.itba.ss.WallCollisionEvent(event['time'], particle, wall)
            new_event = wall_collision_event

        elif event['type'] == 'PARTICLE_COLLISION':
            particle1_raw = event['particle1']
            particle1_type = ar.edu.itba.ss.ar.edu.itba.ss.models.ParticleType.BIG if particle1_raw['type'] == 'BIG' else ar.edu.itba.ss.ar.edu.itba.ss.models.ParticleType.SMALL
            particle1 = ar.edu.itba.ss.ar.edu.itba.ss.ar.edu.itba.ss.models.AcceleratedParticle(particle1_raw['id'], particle1_raw['x'], particle1_raw['y'], particle1_raw['radius'], particle1_type, particle1_raw['vx'], particle1_raw['vy'], particle1_raw['mass'], particle1_raw['collisions'])
            
            particle2_raw = event['particle2']
            particle2_type = ar.edu.itba.ss.ar.edu.itba.ss.models.ParticleType.BIG if particle2_raw['type'] == 'BIG' else ar.edu.itba.ss.ar.edu.itba.ss.models.ParticleType.SMALL
            particle2 = ar.edu.itba.ss.ar.edu.itba.ss.ar.edu.itba.ss.models.AcceleratedParticle(particle2_raw['id'], particle2_raw['x'], particle2_raw['y'], particle2_raw['radius'], particle2_type, particle2_raw['vx'], particle2_raw['vy'], particle2_raw['mass'], particle2_raw['collisions'])
            
            particle_collision_event = ar.edu.itba.ss.ParticleCollisionEvent(event['time'], particle1, particle2)
            new_event = particle_collision_event

        # ar.edu.itba.ss.models.Frame
        new_frame = []
        
        for p in ev['frame']:
            p_type = ar.edu.itba.ss.ar.edu.itba.ss.models.ParticleType.BIG if p['type'] == 'BIG' else ar.edu.itba.ss.ar.edu.itba.ss.models.ParticleType.SMALL
            particle = ar.edu.itba.ss.ar.edu.itba.ss.ar.edu.itba.ss.models.AcceleratedParticle(p['id'], p['x'], p['y'], p['radius'], p_type, p['vx'], p['vy'], p['mass'], p['collisions'])
            
            new_frame.append(particle)

        # Extended event
        new_extended_event = ar.edu.itba.ss.ExtendedEvent(new_event, new_frame)
        res.append(new_extended_event)

    return res