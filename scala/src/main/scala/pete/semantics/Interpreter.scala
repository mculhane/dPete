package pete.semantics

import pete.ir._

package object semantics {

  def compute_next_due_tasks(task_list: TaskList, num_tasks: Int): List[Task] = {
    task_list.tasks.map((x: Task) => (x, compute_due_date(x)))
                   .filterNot(_._2.isEmpty)
    			   .sortBy(_._2.get.timestamp)
                   .take(num_tasks)
                   .map(_._1)
                   .toList
  }
  
  def compute_due_date(task: Task): Option[TimeStamp] = {
	get_next_timestamp(task.due)
  }
  
  def compute_start_date(task: Task): Option[TimeStamp] = {
    get_next_timestamp(task.start)
  }
  
  def compute_done_date(task: Task): Option[TimeStamp] = {
    get_next_timestamp(task.done)
  } 
  
  def get_next_timestamp(expr: Expr): Option[TimeStamp] = expr match {
    case TimeStamp(timestamp) => check_for_future(timestamp)
    case Before(expr, offset) => get_next_timestamp(expr) - compute_seconds(offset) 
    case After(expr, offset) => get_next_timestamp(expr) + compute_seconds(offset)
  }
  
  def check_for_future(timestamp: String): Option[TimeStamp] = {
    // TODO: CHECK THAT TIMESTAMP IS IN FUTURE
    return Some(TimeStamp("asdf"))
  }

}