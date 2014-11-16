class Task(object):
    def __init__(self, start, end, recurrence, description, hash):
        self.start = start
        self.end = end
        self.recurrence = recurrence
        self.description = description
        self.hash = hash


class Expr(object):
    pass


class Before(Expr):
    def __init__(self, expr, offset):
        self.expr = expr
        self.offset = offset


class After(Expr):
    def __init__(self, expr, offset):
        self.expr = expr
        self.offset = offset
        

class Start(Expr):
    def __init__(self, hash):
        self.hash = hash
        

class Due(Expr):
    def __init__(self, hash):
        self.hash = hash
        

class Done(Expr):
    def __init__(self, hash):
        self.hash = hash


class TimeLiteral(Expr):
    pass


class TimeStamp(TimeLiteral):
    def __init__(self, timestamp):
        self.timestamp = timestamp
            

class Recurrence(object):
    def __init__(self, expr):
        self.expr = expr
    

class Offset(object):
    def __init__(self, quantity, unit):
        self.quantity = quantity
        self.unit = unit

    