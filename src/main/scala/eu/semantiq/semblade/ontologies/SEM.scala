package eu.semantiq.semblade.ontologies

import eu.semantiq.semblade._
import DefaultTripleParser._

object SEM extends KnowledgeSet("http://semantiq.eu/ontologies/sem/1.0/",
  List("sem:listSize rdf:domain rdf:List"),
  List(
    SimpleRule("sem:ListSize",
      List("?list rdf:first rdf:nil"),
      List("?list sem:size 0"))),
  List()) {
}