from models import ExtendedEvent, Event, WallCollisionEvent, ParticleCollisionEvent, Wall, VelocityParticle, ParticleType

def parse(extended_events):
    res = []

    for ev in extended_events:
        
        # Event
        event = ev['event']
        new_event = Event(event['time'])

        if event['type'] == 'WALL_COLLISION':
            wall = Wall.VERTICAL if event['wall'] == 'VERTICAL' else Wall.HORIZONTAL
            
            particle_raw = event['particle']
            particle_type = ParticleType.BIG if particle_raw['type'] == 'BIG' else ParticleType.SMALL
            particle = VelocityParticle(particle_raw['id'], particle_raw['x'], particle_raw['y'], particle_raw['radius'], particle_type, particle_raw['vx'], particle_raw['vy'], particle_raw['mass'], particle_raw['collisions'])
            
            wall_collision_event = WallCollisionEvent(event['time'], particle, wall)
            new_event = wall_collision_event

        elif event['type'] == 'PARTICLE_COLLISION':
            particle1_raw = event['particle1']
            particle1_type = ParticleType.BIG if particle1_raw['type'] == 'BIG' else ParticleType.SMALL
            particle1 = VelocityParticle(particle1_raw['id'], particle1_raw['x'], particle1_raw['y'], particle1_raw['radius'], particle1_type, particle1_raw['vx'], particle1_raw['vy'], particle1_raw['mass'], particle1_raw['collisions'])
            
            particle2_raw = event['particle2']
            particle2_type = ParticleType.BIG if particle2_raw['type'] == 'BIG' else ParticleType.SMALL
            particle2 = VelocityParticle(particle2_raw['id'], particle2_raw['x'], particle2_raw['y'], particle2_raw['radius'], particle2_type, particle2_raw['vx'], particle2_raw['vy'], particle2_raw['mass'], particle2_raw['collisions'])
            
            particle_collision_event = ParticleCollisionEvent(event['time'], particle1, particle2)
            new_event = particle_collision_event

        # Frame
        new_frame = []
        
        for p in ev['frame']:
            p_type = ParticleType.BIG if p['type'] == 'BIG' else ParticleType.SMALL
            particle = VelocityParticle(p['id'], p['x'], p['y'], p['radius'], p_type, p['vx'], p['vy'], p['mass'], p['collisions'])
            
            new_frame.append(particle)

        # Extended event
        new_extended_event = ExtendedEvent(new_event, new_frame)
        res.append(new_extended_event)

    return res