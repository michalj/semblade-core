package eu.semantiq.semblade

import org.scalatest.BeforeAndAfterEach
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import eu.semantiq.semblade.ontologies.RDFS

class KnowledgeBaseTest extends FlatSpec with ShouldMatchers
  with BeforeAndAfterEach with TripleParser {
  def prefixStore = DefaultPrefixStore ++ Map("sample" -> "http://semantiq.eu/ontologies/sample#")
  var kb: IKnowledgeBase = _

  override def beforeEach() {
    kb = new KnowledgeBase
    kb.tell(new KnowledgeSet("#set0", List(
      "sample:Ala sample:owns sample:aCat",
      "sample:Ala sample:owns sample:aDog",
      "sample:aCat rdf:type sample:Cat",
      "sample:anotherCat rdf:type sample:Cat"), List(), List()))
    kb.tell(RDFS)
  }

  "Knowledge base" should "know what told" in {
    kb.dump should have size (4)
  }

  it should "shouldUpdateKnowledge" in {
    // when
    kb.tell(new KnowledgeSet("#set1", List("sample:Ala sample:likes sample:aCat"), List(), List()))
    // then
    kb.dump should have size (5)
  }

  it should "run queries" in {
    // when
    val actual = kb.query(List("?who sample:owns ?pet", "?pet rdf:type sample:Cat"))
    // then
    actual should have size (1)
    actual.head("who") should be(prefixStore("sample:Ala"))
  }

  it should "run queries giving multiple results" in {
    // when
    val actual = kb.query(List("?pet rdf:type sample:Cat"))
    // then
    actual should have size (2)
    actual should contain(collection.Map[String, ConcreteNode]("pet" -> prefixStore("sample:aCat")))
    actual should contain(collection.Map[String, ConcreteNode]("pet" -> prefixStore("sample:anotherCat")))
  }

  it should "do rule based inference" in {
    // given
    kb.tell(new KnowledgeSet("#set1", List("sample:Cat rdfs:subclassOf sample:Pet", "sample:Pet rdfs:subclassOf sample:Animal"), List(), List()))
    // when
    kb.infer
    // then
    val animals = kb.query(List("?pet rdf:type sample:Animal"))
    animals should have size (2)
    animals should contain(collection.Map[String, ConcreteNode]("pet" -> prefixStore("sample:aCat")))
    animals should contain(collection.Map[String, ConcreteNode]("pet" -> prefixStore("sample:anotherCat")))
  }
}