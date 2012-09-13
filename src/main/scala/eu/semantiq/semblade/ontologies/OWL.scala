package eu.semantiq.semblade.ontologies

import eu.semantiq.semblade._
import DefaultImplicits._

object OWL extends KnowledgeSet("http://www.w3.org/2002/07/owl#", List(
  "owl:sameAs rdf:type owl:TransitiveProperty",
  "owl:sameAs rdf:type owl:SymmetricProperty",
  "rdf:first rdf:type owl:FunctionalProperty",
  "owl:disjointWith rdf:type owl:SummetricProperty"), List(
  SimpleRule("owl:sameAsRefexivity",
    List("?a ?anyProperty ?anyObject"),
    List("?a owl:sameAs ?a")),
  SimpleRule("owl:sameAsSymmetry",
    List("?a owl:sameAs ?b"),
    List("?b owl:sameAs ?a")),
  SimpleRule("owl:sameAsSemantics",
    List("?a owl:sameAs ?b", "?a ?verb ?object"),
    List("?b ?verb ?object")),
  SimpleRule("owl:TransitiveProperty",
    List("?property rdf:type owl:TransitiveProperty", "?a ?property ?b",
      "?b ?property ?c"),
    List("?a ?property ?c")),
  SimpleRule("owl:SymmetricProperty",
    List("?property rdf:type owl:SymmetricProperty", "?a ?property ?b"),
    List("?b ?property ?a")),
  SimpleRule("owl:FunctionalProperty",
    List("?property rdf:type owl:FunctionalProperty", "?a ?property ?b", "?a ?property ?c"),
    List("?b owl:sameAs ?c")),
  SimpleRule("owl:Thing",
    List("?class rdfs:subclassOf ?otherClass"),
    List("?class rdfs:subclassOf owl:Thing")),
  SimpleRule("owl:differentFromNotSameAs",
    List("?a not owl:sameAs ?b"),
    List("?a owl:differentFrom ?b")),
  SimpleRule("owl:differentFromReflexibility",
    List("?a owl:differentFrom ?b"),
    List("?b owl:differentFrom ?a")),
  SimpleRule("owl:notDifferentFromSameAs",
    List("?a owl:sameAs ?b"),
    List("?a not owl:differentFrom ?b")),
  SimpleRule("owl:AllDifferent",
    List("?list rdf:type owl:AllDifferent", "?list rdf:first ?first", "?list rdf:rest ?rest"),
    List("?rest not rdf:member ?first", "?rest rdf:type owl:AllDifferent")),
  "owl:disjointWith" ~=
    "?a rdf:type ?classA" :: "?b rdf:type ?classB" :: "?classA owl:disjointWith ?classB" >>>
    "?a owl:differentFrom ?b"),
  List())