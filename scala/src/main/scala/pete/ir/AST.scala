package pete.ir

import com.github.nscala_time.time.Imports._

sealed abstract class AST
sealed abstract class Expr extends AST
sealed abstract class TimeLiteral extends Expr

case class TaskList(tasks: Seq[Task]) extends AST

case class Task(start: Expr, due: Expr, done: Expr,
				recurrence: Recurrence, description: String,
				hash: String) extends AST

case class Before(expr: Expr, offset: Offset) extends Expr
case class After(expr: Expr, offset: Offset) extends Expr
case class Start(hash: String) extends Expr
case class Due(hash: String) extends Expr
case class Done(hash: String) extends Expr

case class TimeStamp(timestamp: DateTime) extends TimeLiteral

case class Recurrence(expr: Expr) extends AST

case class Offset(quantity: Int, unit: String) extends AST
