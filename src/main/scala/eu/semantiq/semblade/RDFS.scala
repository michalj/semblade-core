package eu.semantiq.semblade

import TripleParser._

object RDFS extends KnowledgeSet("http://www.w3.org/2000/01/rdf-schema#", List(), List(
    new Rule("rdfs:inheritance",
      List("?a rdfs:isSubclassOf ?b", "?i rdfs:isA ?a"),
      List("?i rdfs:isA ?b")),
    new Rule("rdfs:subclassTransitivity",
      List("?a rdfs:isSubclassOf ?b", "?b rdfs:isSubclassOf ?c"),
      List("?a rdfs:isSubclassOf ?c"))
  ), List())
