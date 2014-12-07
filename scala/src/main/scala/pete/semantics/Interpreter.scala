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
    			   .sortBy(_._2.get)
                   .take(num_tasks)
                   .map(_._1)
                   .toList
  }
  
  def compute_due_date(task: Task, task_dictionary: Map[String, Task]): Option[DateTime] = {
	get_next_datetime(task.due, task_dictionary)
  }
  
  def compute_start_date(task: Task, task_dictionary: Map[String, Task]): Option[DateTime] = {
    get_next_datetime(task.start, task_dictionary)
  }
  
  
  // This currently gets the first datetime for a task, but does not examine any sort of recurrence relationship.
  def get_next_datetime(expr: Option[Expr], task_dictionary: Map[String, Task]): Option[DateTime] = {
	expr map (_ match {
        case TimeStamp(datetime) => Some(datetime) filter is_in_future
        case Before(expr, offset) => get_next_datetime(Some(expr), task_dictionary) map (_ - get_period(offset)) filter is_in_future
        case After(expr, offset) => get_next_datetime(Some(expr), task_dictionary) map (_ + get_period(offset))  filter is_in_future
        case Start(hash) => get_next_datetime(task_dictionary.get( ).flatMap(_.start), task_dictionary)
        case Due(hash) => get_next_datetime(task_dictionary.get(hash).flatMap(_.due), task_dictionary)
    }) get
  }

  
  def is_in_future(datetime: DateTime): Boolean = {
    return datetime > DateTime.now
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