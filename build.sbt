organization := "eu.semantiq"

name := "semblade-core"

publishTo := Some(Resolver.file("Semblade Repository", new File("./releases")))

libraryDependencies += "org.scalatest" %% "scalatest" % "1.8" % "test"