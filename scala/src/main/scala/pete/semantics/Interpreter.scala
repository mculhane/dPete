package pete.semantics

import pete.ir._
import com.github.nscala_time.time.Imports._

package object semantics {

  def construct_task_dictionary(task_list: TaskList): Map[String, Task] = {
    task_list.tasks map ((x: Task) => (x.hash, x)) toMap
  }
  
  def isValidTask(task_dictionary: Map[String, Task])(task: Task): Boolean = {
    val recurrence = get_recurrence(task, task_dictionary)
    val start = get_datetime(task.start, task_dictionary, task)
    val due = get_datetime(task.due, task_dictionary, task)
    
    // Make sure the due date falls after the start date
    if (start.isDefined && due.isDefined && due.get < start.get) {
      println("Ignoring task " + task.hash + ": A task's due date must come after its start date.")
      return false
    }
    
    // Make sure recurring tasks have start and due dates defined
    if (recurrence.isDefined && !(start.isDefined && due.isDefined)) {
      println("Ignoring task " + task.hash + ": A recurring task must have defined start and due dates.")
      return false
    }
    
    // Make sure recurring tasks' start and due dates encompass a smaller interval
    // than the recurrence interval.
    if (recurrence.isDefined && (start.get to due.get).millis > get_period(recurrence.get.offset).toDurationFrom(start.get).millis) {
      println("Ignoring task " + task.hash + ": A recurring task cannot be active for longer than its recurrence interval.")
      return false
    }

    return true
  }
  
  // Return a Task object representing the current instance of a task. If no instance
  // of the task is currently active, return None.
  def getCurrentInstance(task_dictionary: Map[String, Task])(task: Task): Option[Task] = {
    val recurrence = get_recurrence(task, task_dictionary)
    var start: Option[DateTime] = get_datetime(task.start, task_dictionary, task)
    var due: Option[DateTime] = get_datetime(task.due, task_dictionary, task)
    
    if (recurrence.isEmpty) {
      return if(is_current(start, due)) Some(task) else None
    }
    
    val interval = get_period(recurrence.get.offset)
  
    while (is_in_past(due)) {
	  start = start map (_ + interval)
	  due = due map (_ + interval)
    }
 
    if (is_current(start, due)) {
      val instance_start = Some(TimeStamp(start.get))
      val instance_due = Some(TimeStamp(due.get))
	  return Some(Task(instance_start, instance_due, task.dependence,
	                   task.recurrence, task.description, task.hash))
    } else {
      return None
    }

  }
  
  def is_current(start: Option[DateTime], end:Option[DateTime]): Boolean = {
    return !(start.isDefined && start.get > DateTime.now)  && !(end.isDefined && end.get < DateTime.now)
  }

  def is_in_past(end:Option[DateTime]): Boolean = {
    return end.isDefined && end.get < DateTime.now
  }
  
  def formatPretty(task_dictionary: Map[String, Task])(task:Task): String = {
    val due = get_datetime(task.due, task_dictionary, task)
    val formatted_due = DateTimeFormat.forPattern("EEE, MMM d hh:mm a").print(due.get)
    "Due: " + formatted_due + " - " + task.description + " (" + task.hash + ")" 
  }
  
  def compute_next_due_tasks(task_list: TaskList, num_tasks: Int): List[String] = {
    val task_dictionary = construct_task_dictionary(task_list)
    task_list.tasks.filter(isValidTask(task_dictionary))
    			   .flatMap(getCurrentInstance(task_dictionary))
    			   .map((x: Task) => (x, compute_due_date(x, task_dictionary)))
                   .filterNot(_._2.isEmpty)
    			   .sortBy(_._2.get)
                   .take(num_tasks)
                   .map(_._1)
                   .map(formatPretty(task_dictionary))
                   .toList
  }
  
  def compute_due_date(task: Task, task_dictionary: Map[String, Task]): Option[DateTime] = {
	get_datetime(task.due, task_dictionary, task)
  }
  
  def compute_start_date(task: Task, task_dictionary: Map[String, Task]): Option[DateTime] = {
    get_datetime(task.start, task_dictionary, task)
  }
  
  def get_recurrence(task: Task, task_dictionary: Map[String, Task]): Option[Recurrence] = {
    if (task.dependence.isDefined) {
      return get_recurrence(task_dictionary.get(task.dependence.get).get, task_dictionary)
    } else {
      return task.recurrence
    }
  }
  
  // This currently gets the first datetime for a task, but does not examine any sort of recurrence relationship.
  def get_datetime(expr: Option[Expr], task_dictionary: Map[String, Task], task: Task): Option[DateTime] = {
	expr map (_ match {
        case TimeStamp(datetime) => Some(datetime)
        case Before(expr, offset) => get_datetime(Some(expr), task_dictionary, task) map (_ - get_period(offset))
        case After(expr, offset) => get_datetime(Some(expr), task_dictionary, task) map (_ + get_period(offset))
        case DependenceStart => get_datetime(task_dictionary.get(task.dependence.get).flatMap(_.start), task_dictionary, task_dictionary.get(task.dependence.get).get)
        case DependenceDue => get_datetime(task_dictionary.get(task.dependence.get).flatMap(_.due), task_dictionary, task_dictionary.get(task.dependence.get).get)
    }) get
  }
  
  def get_period(offset: Offset): Period = offset.unit match {
	  case "minute" => offset.quantity.minutes
	  case "hour" => offset.quantity.hours
	  case "day" => offset.quantity.days
	  case "week" => offset.quantity.weeks
	  case "month" => offset.quantity.months
	  case "year" => offset.quantity.years
	  case _ => throw new Exception("Unknown unit encountered")
  }
}