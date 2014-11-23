package pete

import scala.io.Source
import parser.PeteParser

object pete extends App {
	var lines = new collection.mutable.MutableList[String]()
	for (ln <- io.Source.stdin.getLines) {
	  lines += ln
	}
	
	val result = PeteParser(lines.foldLeft("")((a:String, b:String) => a + b))
	val output = semantics.semantics.compute_next_due_tasks(result.get, 10)
	
	for (ln <- output) {
	  println(ln)
	}
}