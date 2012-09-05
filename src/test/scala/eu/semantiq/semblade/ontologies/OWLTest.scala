package eu.semantiq.semblade.ontologies

import org.scalatest.FeatureSpec
import org.scalatest.matchers.ShouldMatchers
import eu.semantiq.semblade._
import org.scalatest.BeforeAndAfterEach

class OWLTest extends FeatureSpec with ShouldMatchers with TripleParser {
  def prefixStore = DefaultPrefixStore ++ Map(
    "sample" -> "http://semantiq.eu/ontologies/sample#")
  val base = new KnowledgeBase() + RDFS + OWL

  feature("OWL defines owl:sameAs") {
    scenario("owl:sameAs is reflexive") {
      // given
      val kb = base + "sample:Fafik rdf:type sample:Dog" !
      // when
      val actual = kb.query(Seq("?x owl:sameAs sample:Fafik"))
      // then
      actual should be(Seq(Map("x" -> prefixStore("sample:Fafik"))))
    }
    scenario("owl:sameAs is transitive") {
      // given
      val kb = base +
        "sample:Fafik owl:sameAs sample:DogOfAla" +
        "sample:DogOfAla owl:sameAs sample:FavPetOfAla" !
      // when
      val actual = kb.query(Seq("sample:Fafik owl:sameAs ?x"))
      // then
      actual.toSet should be(Set(
        Map("x" -> prefixStore("sample:Fafik")),
        Map("x" -> prefixStore("sample:DogOfAla")),
        Map("x" -> prefixStore("sample:FavPetOfAla"))))
    }
    scenario("owl:sameAs is symmetric") {
      // given
      val kb = base + "sample:Germany owl:sameAs sample:Alemania" !
      // when
      val actual = kb.query(Seq("sample:Alemania owl:sameAs ?what"))
      actual.toSet should be(Set(
        Map(
          "what" -> prefixStore("sample:Germany")),
        Map(
          "what" -> prefixStore("sample:Alemania"))))
    }
  }
  feature("OWL defines owl:SymmetricProperty") {
    scenario("owl:SymmetricProperty implies symmetric relationship") {
      // given
      val kb = base +
        "sample:closeTo rdf:type owl:SymmetricProperty" +
        "sample:London sample:closeTo sample:Paris" !
      // when
      val actual = kb.query(Seq("sample:Paris sample:closeTo ?where"))
      // then
      actual.toSet should be(Set(
        Map("where" -> prefixStore("sample:London"))))
    }
  }
  feature("OWL defines owl:TransitiveProperty") {
    scenario("TODO") { pending }
  }
  feature("OWL defines owl:FunctionalProperty") {
    scenario("TODO") { pending }
  }
  feature("OWL defines owl:inverseOf") {
    scenario("TODO") { pending }
  }
  feature("OWL defines owl:InverseFunctionalProperty") {
    scenario("TODO") { pending }
  }
  feature("OWL defines owl:differentFrom") {
    scenario("TODO") { pending }
  }
  feature("OWL defines owl:Restriction") {
    scenario("owl:Restriction on owl:cardinality") { pending }
    scenario("owl:Restriction on owl:someValuesFrom") { pending }
    scenario("owl:Restriction on owl:allValuesFrom") { pending }
    scenario("owl:Restriction on owl:hasValue") { pending }
  }
  feature("OWL defines owl:disjointWith") {
    scenario("TODO") { pending }
  }
  feature("OWL defines owl:complementOf") {
    scenario("TODO") { pending }
  }
  feature("OWL defines owl:equivalentClass") {
    scenario("TODO") { pending }
  }
  feature("OWL defines owl:equivalentProperty") {
    scenario("TODO") { pending }
  }
}