package pete.parser

import scala.util.parsing.combinator._
import pete.ir._
import com.github.nscala_time.time.Imports._
import org.joda.time.DateTime

object PeteParser extends JavaTokenParsers with PackratParsers {
	
	// parsing interface
	def apply(s: String): ParseResult[TaskList] = parseAll(taskList, s)
	
	// expressions
	lazy val taskList: PackratParser[TaskList] = 
      (   (rep1(task) ^^ {case tasks => TaskList(tasks)})
        | failure("Unable to parse TaskList")) 
	
	lazy val task: PackratParser[Task] = 
      (  (hash ~ """[a-zA-Z0-9\s]*[a-zA-Z0-9]""".r ~ "@" ~ expr ~ "-" ~ expr ~ "%" ~ """[a-zA-Z0-9\s]+""".r
         ^^ {case hash~description~_~start~_~due~_~recurrence => Task(start, due, None, None, description, hash)})
        | failure("Unable to parse Task")) 

    lazy val expr: PackratParser[Option[Expr]] =
      ( (("""\d{4}-\d{2}-\d{2}(T\d{2}:\d{2}(:\d{2})?)?""".r)
         ^^ {case datetime => Some(TimeStamp(DateTime.parse(datetime)))})
        | failure("Unable to parse a datetime expression"))
    
    lazy val hash: PackratParser[String] = 
      ( ("""#[a-fA-F0-9]+""".r ^^ {case hash => hash}) 
       | failure("Unable to parse task's hash") )
}
