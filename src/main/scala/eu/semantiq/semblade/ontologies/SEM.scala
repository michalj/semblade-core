package eu.semantiq.semblade.ontologies

import eu.semantiq.semblade._
import DefaultImplicits._

object SEM extends KnowledgeSet("http://semantiq.eu/ontologies/sem/1.0/",
  List("sem:listSize rdf:domain rdf:List",
    "rdf:nil sem:listSize 0"),
  List(),
  List()) {
}