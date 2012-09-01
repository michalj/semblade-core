organization := "eu.semantiq"

name := "semblade-core"

version := "1.0"

publishTo := Some(Resolver.file("Semblade Repository", new File("./releases")))

libraryDependencies += "org.scalatest" %% "scalatest" % "1.8" % "test"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.6.6"