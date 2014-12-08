package pete.parser

import org.joda.time.DateTime
import org.scalatest.FunSpec

import edu.hmc.langtools.LangParseMatchers
import pete.ir.Expr
import pete.ir.Task
import pete.ir.TaskList
import pete.ir.TimeStamp
import pete.ir.Offset
import pete.ir.Recurrence

class TaskListParserTests extends FunSpec with LangParseMatchers[TaskList] {
  
  override val parser = PeteParser.apply _
  
  describe("A valid task list") {
    it("cannot be empty") {
      program("") should not (parse)
    }
  }
  
}

class TaskParserTests extends FunSpec with LangParseMatchers[Task] {
  override val parser = {(s:String) => PeteParser.parseAll(PeteParser.task, s)}
  
  describe("A valid task") {
    it("works in the general case") {
      program("#ABCDEF Take out the trash @ 2012-10-06 - 2013-10-04 % every 2 weeks") should parseAs(Task(Some(TimeStamp(DateTime.parse("2012-10-06"))), Some(TimeStamp(DateTime.parse("2013-10-04"))), None, Some(Recurrence(Offset(2, "week"))), "Take out the trash", "#ABCDEF"))
    }
  }  
}

class ExprParserTests extends FunSpec with LangParseMatchers[Option[Expr]] {
  override val parser = {(s:String) => PeteParser.parseAll(PeteParser.expr, s)}
  
  describe("A valid expr") {
    it("is an iso 8601 formatted date") {
      program("2012-10-06") should parseAs(Some(TimeStamp(DateTime.parse("2012-10-06"))))
    }

    it("is an iso 8601 formatted datetime") {
      program("2012-10-06T04:13:00") should parseAs(Some(TimeStamp(DateTime.parse("2012-10-06T04:13:00"))))
    }
  }  
}
    

class HashParserTests extends FunSpec with LangParseMatchers[String] {
  override val parser = {(s:String) => PeteParser.parseAll(PeteParser.hash, s)}
  
  describe("A valid hash") {
    it("must start with a hash") {
      program("ABCDEF") should not (parse)
    }
    
    it("can have letters") {
      program("#ABCDEF") should parseAs("#ABCDEF")
    }
    
    it("can have numbers") {
      program("#AB01AB") should parseAs("#AB01AB")
    }
    
    it("cannot have no letters/numbers") {
      program("#") should not (parse)
    }
    
    it("can have a single letter/number") {
      program("#A") should parseAs("#A")
    }  
    
    it("can be reasonably long") {
      program("#ABC0139283ABCDEF013") should parseAs("#ABC0139283ABCDEF013")
    }
  }

}
