package eu.semantiq.semblade.ontologies

import org.scalatest.FeatureSpec
import org.scalatest.matchers.ShouldMatchers

import eu.semantiq.semblade._

class RDFSTest extends FeatureSpec with ShouldMatchers with TripleParser {
  def prefixStore = DefaultPrefixStore ++ Map(
    "sample" -> "http://semantiq.eu/ontologies/sample#")
  val base = new KnowledgeBase() + RDFS + OWL

  feature("RDF List semantics") {
    scenario("rdf:member if not rdf:rest then rdf:first") {
      // given
      val kb = base +
        "sample:ABC rdf:member sample:A" +
        "sample:ABC rdf:rest sample:BC" +
        "sample:BC not rdf:member sample:A" !
      // when
      val actual = kb ? "sample:ABC rdf:first ?x"
      // then
      actual.toSet should be(Set(Map("x" -> prefixStore("sample:A"))))
    }
    scenario("rdf:member if not rdf:first then rdf:rest") {
      // given
      val kb = base +
        "sample:ABC rdf:member sample:C" +
        "sample:ABC not rdf:first sample:C" +
        "sample:ABC rdf:rest sample:BC" !
      // when
      val actual = kb ? "?list rdf:member sample:C"
      // then
      actual.toSet should be(Set(
        Map("list" -> prefixStore("sample:ABC")),
        Map("list" -> prefixStore("sample:BC"))))
    }
    scenario("nothing is rdf:member of empty list") {
      // given
      val kb = base +
        "sample:EmptyBasket rdf:first rdf:nil" +
        "sample:Carrot rdf:type sample:Grocery" !
      // when
      val actual = kb ? "?places not rdf:member sample:Carrot"
      // then
      actual.toSet should be(Set(
        Map("places" -> prefixStore("sample:EmptyBasket"))))
    }
  }
}