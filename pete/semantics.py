import heapq
import time

from intermediate_representation import \
    TimeStamp, Start, Due, Done, Before, After


class TaskSemantics(object):
    """Semantics for the task management system."""

    def __init__(self, task_list):
        self.tasks = dict()
        for task in task_list:
            self.tasks[task.hash] = task

    def compute_next_due_tasks(self, num_tasks):
        """Compute the num_tasks tasks with the soonest due dates."""
        due_dates = dict()
        for task in self.tasks.values():
            due_date = self.compute_due_date(task)
            if due_date:
                due_dates[task.hash] = due_date
        return heapq.nsmallest(
            num_tasks, self.tasks.values(), lambda x: due_dates[x.hash])

    def compute_due_date(self, task):
        """Compute a timestamp for the due date of task."""
        return self.get_next_timestamp(task.due)

    def compute_start_date(self, task):
        """Compute a timestamp for the start date of task."""
        return self.get_next_timestamp(task.start)

    def get_next_timestamp(self, expr):
        """Return the next timestamp specified by expr."""
        if type(expr) is TimeStamp:
            return expr.timestamp if is_in_future(expr.timestamp) else None

        elif type(expr) is Start:
            start_date = self.tasks[expr.hash].start
            return start_date.get_next_timestamp() if start_date else None

        elif type(expr) is Due:
            due_date = self.tasks[expr.hash].due
            return due_date.get_next_timestamp() if due_date else None

        elif type(expr) is Done:
            done_date = self.tasks[expr.hash].done
            return done_date.get_next_timestamp() if done_date else None

        elif type(expr) is Before:
            offset_seconds = compute_offset_seconds(expr.offset)
            timestamp = self.get_next_timestamp(expr.expr)
            return timestamp - offset_seconds if timestamp else None

        elif type(expr) is After:
            offset_seconds = compute_offset_seconds(expr.offset)
            timestamp = self.get_next_timestamp(expr.expr)
            return timestamp + offset_seconds if timestamp else None


def is_in_future(timestamp):
    """Check whether the provided timestamp is in the future."""
    return timestamp > time.time()


def compute_offset_seconds(offset):
    """Convert an offset (quantity and unit) into a number of seconds."""
    seconds_per_minute = 60
    seconds_per_hour = seconds_per_minute * 60
    seconds_per_day = seconds_per_hour * 24
    seconds_per_week = seconds_per_day * 7

    # This is clearly an incorrect approximation
    seconds_per_month = seconds_per_day * 30

    # Again, an approximation (daylight savings, leap years make this fun)
    seconds_per_year = seconds_per_day * 365

    if offset.unit == 'minute':
        return seconds_per_minute * offset.quantity
    if offset.unit == 'hour':
        return seconds_per_hour * offset.quantity
    if offset.unit == 'day':
        return seconds_per_day * offset.quantity
    if offset.unit == 'week':
        return seconds_per_week * offset.quantity
    if offset.unit == 'month':
        return seconds_per_month * offset.quantity
    if offset.unit == 'year':
        return seconds_per_year * offset.quantity
