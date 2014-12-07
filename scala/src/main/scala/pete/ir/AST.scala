package pete.ir

import com.github.nscala_time.time.Imports._

sealed abstract class AST
sealed abstract class Expr extends AST
sealed abstract class TimeLiteral extends Expr

case class TaskList(tasks: Seq[Task]) extends AST

case class Task(start: Option[Expr], due: Option[Expr],
				dependence: Option[String], recurrence: Option[Recurrence],
				description: String, hash: String) extends AST

case class Before(expr: Expr, offset: Offset) extends Expr
case class After(expr: Expr, offset: Offset) extends Expr
case object DependenceStart extends Expr
case object DependenceDue extends Expr

case class TimeStamp(datetime: DateTime) extends TimeLiteral

case class Recurrence(offset: Offset) extends AST

case class Offset(quantity: Int, unit: String) extends AST
