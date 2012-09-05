package eu.semantiq.semblade.ontologies

import eu.semantiq.semblade._
import DefaultTripleParser._

object OWL extends KnowledgeSet("http://www.w3.org/2002/07/owl#", List(
  "owl:sameAs rdf:type owl:TransitiveProperty",
  "owl:sameAs rdf:type owl:SymmetricProperty",
  "rdf:first rdf:type owl:FunctionalProperty"), List(
  Rule("owl:sameAsRefexivity",
    List("?a ?anyProperty ?anyObject"),
    List("?a owl:sameAs ?a")),
  Rule("owl:sameAsSummetry",
    List("?a owl:sameAs ?b"),
    List("?b owl:sameAs ?a")),
  Rule("owl:TransitiveProperty",
    List("?property rdf:type owl:TransitiveProperty", "?a ?property ?b",
      "?b ?property ?c"),
    List("?a ?property ?c")),
  Rule("owl:SymmetricProperty",
    List("?property rdf:type owl:SymmetricProperty", "?a ?property ?b"),
    List("?b ?property ?a")),
  Rule("owl:FunctionalProperty",
    List("?property rdf:type owl:FunctionalProperty", "?a ?property ?b", "?a ?property ?c"),
    List("?b owl:sameAs ?c")),
  Rule("owl:Thing",
    List("?class rdfs:subclassOf ?otherClass"),
    List("?class rdfs:subclassOf owl:Thing")),
  Rule("owl:differentFromNotSameAs",
    List("?a not owl:sameAs ?b"),
    List("?a owl:differentFrom ?b")),
  Rule("owl:notDifferentFromSameAs",
    List("?a owl:sameAs ?b"),
    List("?a not owl:differentFrom ?b"))), List())