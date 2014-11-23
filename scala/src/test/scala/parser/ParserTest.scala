package pete.parser

import org.scalatest._

import pete.ir._
import pete.parser._
import edu.hmc.langtools._

class TaskListParserTests extends FunSpec with LangParseMatchers[TaskList] {
  
  override val parser = PeteParser.apply _
  
  describe("A valid task list") {
    it("cannot be empty") {
      program("") should not (parse)
    }
  }
  
}

class HashParserTests extends FunSpec with LangParseMatchers[String] {
  override val parser = {(s:String) => PeteParser.parseAll(PeteParser.hash, s)}
  
  describe("A valid hash") {
    it("must start with a hash") {
      program("ABCDEF") should not (parse)
    }
  }

}