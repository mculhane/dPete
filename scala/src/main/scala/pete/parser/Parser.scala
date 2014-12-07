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
	  ( dependencyTask | recurringTask | nonrecurringTask | failure("Unable to parse Task") )
	
	  
	lazy val recurringTask: PackratParser[Task] = 
      ( hash ~ taskDescription ~ "@" ~ expr ~ "-" ~ expr ~ "%" ~ recurrence
        ^^ {case hash~description~_~start~_~due~_~recur => Task(start, due, None, recur, description, hash)})
     
    lazy val nonrecurringTask: PackratParser[Task] = 
      ( hash ~ taskDescription ~ "@" ~ expr ~ "-" ~ expr
        ^^ {case hash~description~_~start~_~due => Task(start, due, None, None, description, hash)}) 
     
    lazy val dependencyTask: PackratParser[Task] =  
      ( hash ~ taskDescription ~ "^" ~ hash ~ relativeDate ~ "," ~ relativeDate
        ^^ {case hash~description~_~dependenceHash~start~_~due => Task(Some(start), Some(due), None, None, description, hash)})
    
    lazy val relativeDate: PackratParser[Expr] = 
      ( ("Start" | "Due") ~ ("+" | "-") ~ wholeNumber ~ unit
        ^^ {case "Start" ~ "+" ~ quantity ~ unit => After(DependenceStart, Offset(quantity.toInt, unit))
            case "Due" ~ "+" ~ quantity ~ unit => After(DependenceDue, Offset(quantity.toInt, unit))
            case "Start" ~ "-" ~ quantity ~ unit => Before(DependenceStart, Offset(quantity.toInt, unit))
            case "Due" ~ "-" ~ quantity ~ unit => Before(DependenceDue, Offset(quantity.toInt, unit))} )
        
    lazy val taskDescription: PackratParser[String] = regex("""[a-zA-Z0-9\s]*[a-zA-Z0-9]""".r)

    lazy val expr: PackratParser[Option[Expr]] =
      ( (("""\d{4}-\d{2}-\d{2}(T\d{2}:\d{2}(:\d{2})?)?""".r)
         ^^ {case datetime => Some(TimeStamp(DateTime.parse(datetime)))})
        | failure("Unable to parse a datetime expression"))
    
    lazy val hash: PackratParser[String] = 
      ( ("""#[a-zA-Z0-9]+""".r ^^ {case hash => hash}) 
       | failure("Unable to parse task's hash") )
       
    lazy val recurrence: PackratParser[Option[Recurrence]] = 
       ( "every" ~ wholeNumber ~ unit
           ^^ {case _~quantity~unit => Some(Recurrence(Offset(quantity.toInt, unit))) }
       )

    lazy val unit: PackratParser[String] = 
      (
    	("""minutes?""".r ^^ {case _ => "minute"})
    	| ("""hours?""".r ^^ {case _ => "hour"})
    	| ("""days?""".r ^^ {case _ => "days"})
    	| ("""weeks?""".r ^^ {case _ => "week"})
    	| ("""months?""".r ^^ {case _ => "month"})
    	| ("""years?""".r ^^ {case _ =>"year"})
      )
}
