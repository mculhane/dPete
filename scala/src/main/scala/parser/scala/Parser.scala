package pete.parser

import scala.util.parsing.combinator.Parsers 
import pete.ir._

object PeteParser extends JavaTokenParsers with PackratParsers {
	
	// parsing interface
	def apply(s: String): ParseResult[TaskList] = parseAll(TaskList, s)
	
	// expressions
	lazy val taskList: PackratParser[TaskList] = 
      (   rep1(task) ^^ {case tasks => TaskList(tasks)}
        | failure("Unable to parse TaskList")) 
	
	lazy val task: PackratParser[Task] = 
      (  """[a-zA-Z0-9\._]+""".r~"""[a-zA-Z0-9\._]+""".r~
         """[a-zA-Z0-9\._]+""".r~"""[a-zA-Z0-9\._]+""".r~
         """[a-zA-Z0-9\._]+""".r~"""[a-zA-Z0-9\._]+""".r~
         """[a-zA-Z0-9\._]+""".r 
         ^^ {case _~start~due~done~recurrence~description~hash => Task(start, due, done, recurrence, description, hash)}
        | failure("Unable to parse Task")) 

}