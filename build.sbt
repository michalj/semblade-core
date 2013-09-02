organization := "eu.semantiq"

name := "semblade-core"

version := "1.4-SNAPSHOT"

scalaVersion := "2.10.1"

publishTo := Some(Resolver.file("Semblade Repository", new File("./releases")))

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.6.6"
