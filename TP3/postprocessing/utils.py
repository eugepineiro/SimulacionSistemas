def __str__(self):
    return '%s(%s)' % (
        type(self).__name__,
        ', '.join('%s=%s' % item for item in vars(self).items())
    )

def auto_str(cls):
    cls.__str__ = __str__
    return cls

def auto_repr(cls):
    cls.__repr__ = __str__
    return cls