package eu.semantiq.semblade.ontologies

import eu.semantiq.semblade._
import DefaultImplicits._

object RDFS extends KnowledgeSet("http://www.w3.org/2000/01/rdf-schema#", List(
  "rdf:member rdf:domain rdf:List",
  "rdf:first rdf:domain rdf:List",
  "rdf:rest rdf:domain rdf:List",
  "rdf:rest rdf:range rdf:List"), List(
  "rdfs:inheritance" ~=
    "?a rdfs:subclassOf ?b" :: "?i rdf:type ?a" >>> "?i rdf:type ?b",
  "rdfs:subclassTransitivity" ~=
    "?a rdfs:subclassOf ?b" :: "?b rdfs:subclassOf ?c" >>> "?a rdfs:subclassOf ?c",
  "rdfs:domain" ~=
    "?property rdfs:domain ?class" :: "?object ?property ?anything" >>>
    "?object rdf:type ?class",
  SimpleRule("rdfs:range",
    List("?property rdfs:range ?class", "?anything ?property ?subject"),
    List("?subject rdf:type ?class")),
  SimpleRule("rdfs:propertyInheritance",
    List("?a ?property ?b", "?property rdfs:subPropertyOf ?superProperty"),
    List("?a ?superProperty ?b")),
  SimpleRule("rdf:ListFirtsMember",
    List("?list rdf:type rdf:List", "?list rdf:first ?a"),
    List("?list rdf:member ?a")),
  SimpleRule("rdf:ListRestMember",
    List("?list rdf:type rdf:List", "?list rdf:rest ?rest", "?rest rdf:member ?a"),
    List("?list rdf:member ?a")),
  SimpleRule("rdf:ListFirtsMember",
    List("?list rdf:type rdf:List", "?list rdf:first ?a"),
    List("?list rdf:member ?a")),
  SimpleRule("rdf:ListRestMember",
    List("?list rdf:type rdf:List", "?list rdf:rest ?rest", "?rest rdf:member ?a"),
    List("?list rdf:member ?a")),
  SimpleRule("rdf:ListMemberAlternativeToRest",
    List("?list rdf:member ?a", "?list rdf:rest ?rest", "?rest not rdf:member ?a"),
    List("?list rdf:first ?a")),
  SimpleRule("rdf:ListMemberAlternativeToFirst",
    List("?list rdf:member ?a", "?list not rdf:first ?a", "?list rdf:rest ?rest"),
    List("?rest rdf:member ?a")),
  SimpleRule("rdf:ListEmpty",
    List("?list rdf:first rdf:nil", "?a ?anyProperty ?anySubject"),
    List("?list not rdf:member ?a")),
  SimpleRule("rdf:NonMemberDifferentFromAll",
    List("?list not rdf:member ?element", "?list rdf:first ?first", "?list rdf:rest ?rest"),
    List("?element owl:differentFrom ?first", "?rest not rdf:member ?element"))), List())