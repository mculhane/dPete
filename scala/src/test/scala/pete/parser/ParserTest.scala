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
    
    it("cannot contain letters outside of A-F") {
      program("#ABCDEFG") should not (parse)
    }  
  }

}
