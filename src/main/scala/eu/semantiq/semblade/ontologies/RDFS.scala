package eu.semantiq.semblade.ontologies

import eu.semantiq.semblade._
import DefaultTripleParser._

object RDFS extends KnowledgeSet("http://www.w3.org/2000/01/rdf-schema#", List(), List(
  Rule("rdfs:inheritance",
    List("?a rdfs:subclassOf ?b", "?i rdf:type ?a"),
    List("?i rdf:type ?b")),
  Rule("rdfs:subclassTransitivity",
    List("?a rdfs:subclassOf ?b", "?b rdfs:subclassOf ?c"),
    List("?a rdfs:subclassOf ?c")),
  Rule("rdfs:domain",
    List("?property rdfs:domain ?class", "?object ?property ?anything"),
    List("?object rdf:type ?class")),
  Rule("rdfs:range",
    List("?property rdfs:range ?class", "?anything ?property ?subject"),
    List("?subject rdf:type ?class")),
  Rule("rdfs:propertyInheritance",
    List("?a ?property ?b", "?property rdfs:subPropertyOf ?superProperty"),
    List("?a ?superProperty ?b"))), List())