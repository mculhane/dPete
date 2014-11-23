name := "Pete"

version := "0.1"

scalaVersion := "2.11.2"

libraryDependencies ++= 
    Seq( "com.github.nscala-time" %% "nscala-time" % "1.4.0",
         "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.1",
         "org.scalatest" % "scalatest_2.11" % "2.1.7",
         "org.scala-lang" % "scala-compiler" % scalaVersion.value)
