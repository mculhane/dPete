package pete.semantics

import pete.ir._
import com.github.nscala_time.time.Imports._

package object semantics {

  def construct_task_dictionary(task_list: TaskList): Map[String, Task] = {
    task_list.tasks map ((x: Task) => (x.hash, x)) toMap
  }
  
  def compute_next_due_tasks(task_list: TaskList, num_tasks: Int): List[Task] = {
    val task_dictionary = construct_task_dictionary(task_list)
    task_list.tasks.map((x: Task) => (x, compute_due_date(x, task_dictionary)))
                   .filterNot(_._2.isEmpty)
    			   .sortBy(_._2.get.datetime)
                   .take(num_tasks)
                   .map(_._1)
                   .toList
  }
  
  def compute_due_date(task: Task, task_dictionary: Map[String, Task]): Option[TimeStamp] = {
	get_next_datetime(task.due, task_dictionary)
  }
  
  def compute_start_date(task: Task, task_dictionary: Map[String, Task]): Option[TimeStamp] = {
    get_next_datetime(task.start, task_dictionary)
  }
  
  def compute_done_date(task: Task, task_dictionary: Map[String, Task]): Option[TimeStamp] = {
    get_next_datetime(task.done, task_dictionary)
  } 
  
  def get_next_datetime(expr: Expr, task_dictionary: Map[String, Task]): Option[TimeStamp] = expr match {
    case TimeStamp(timestamp) => check_for_future(timestamp)
    case Before(expr, offset) => check_for_future(get_next_datetime(expr, task_dictionary).get.datetime - compute_seconds(offset)) 
    case After(expr, offset) => check_for_future(get_next_datetime(expr, task_dictionary).get.datetime + compute_seconds(offset))
    case Start(hash) => get_next_datetime(task_dictionary.get(hash).get.start, task_dictionary)
    case Due(hash) => get_next_datetime(task_dictionary.get(hash).get.due, task_dictionary)
    case Done(hash) => get_next_datetime(task_dictionary.get(hash).get.done, task_dictionary)
  }
  
  def check_for_future(timestamp: DateTime): Option[TimeStamp] = {
	if (timestamp > DateTime.now) {
	  return Some(TimeStamp(timestamp))
	} else {
	  return None
	}
  }
  
  def compute_seconds(offset: Offset): Period = offset.unit match {
	  case "minute" => offset.quantity.minutes
	  case "hour" => offset.quantity.hours
	  case "day" => offset.quantity.days
	  case "week" => offset.quantity.weeks
	  case "month" => offset.quantity.months
	  case "year" => offset.quantity.years
	  case _ => throw new Exception("Unknown unit encountered")
  }
}