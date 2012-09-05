package eu.semantiq.semblade.ontologies

import eu.semantiq.semblade._
import DefaultImplicits._
import scala.util.matching.Regex._

object SEM extends KnowledgeSet("http://semantiq.eu/ontologies/sem/1.0/",
  List("sem:listSize rdf:domain rdf:List",
    "rdf:nil sem:listSize sem:0"),
  List(
    "sem:reqex" ~= "?a sem:mayRegexMatch ?b" >>! (_
      map (m => (m("a"), m("b")))
      flatMap {
        case (a: ValueNode, b: ValueNode) if (b.value.r.findFirstIn(a.value).isDefined) =>
          Some(Triple(a, "sem:regexMatch", b))
        case _ => None
      })),
  List()) {
}