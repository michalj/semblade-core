package eu.semantiq.semblade

import org.scalatest.BeforeAndAfterEach
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import eu.semantiq.semblade.ontologies.RDFS

class MemoryKnowledgeBaseTest extends FlatSpec with ShouldMatchers
  with BeforeAndAfterEach with Implicits {
  def prefixStore = DefaultPrefixStore ++ Map("sample" -> "http://semantiq.eu/ontologies/sample#")
  var kb: KnowledgeBase = _

  override def beforeEach() {
    kb = new MemoryKnowledgeBase
    kb = kb.tell(new KnowledgeSet("#set0", List(
      "sample:Ala sample:owns sample:aCat",
      "sample:Ala sample:owns sample:aDog",
      "sample:aCat rdf:type sample:Cat",
      "sample:anotherCat rdf:type sample:Cat"), List(), List()))
    kb = kb.tell(RDFS)
  }

  it should "update knowledge" in {
    // when
    kb = kb.tell(new KnowledgeSet("#set1", List("sample:Ala sample:likes sample:aCat"), List(), List()))
    // then
    val actual = kb.select(Seq("?who sample:likes ?whom"))
    actual.toSet should be (Set(Map(
        "who" -> prefixStore("sample:Ala"),
        "whom" -> prefixStore("sample:aCat")
    )))
  }

  it should "run queries" in {
    // when
    val actual = kb.select(List("?who sample:owns ?pet", "?pet rdf:type sample:Cat"))
    // then
    actual should have size (1)
    actual.head("who") should be(prefixStore("sample:Ala"))
  }

  it should "run queries giving multiple results" in {
    // when
    val actual = kb.select(List("?pet rdf:type sample:Cat"))
    // then
    actual should have size (2)
    actual should contain(collection.Map[String, ConcreteNode]("pet" -> prefixStore("sample:aCat")))
    actual should contain(collection.Map[String, ConcreteNode]("pet" -> prefixStore("sample:anotherCat")))
  }

  it should "do rule based inference" in {
    // given
    kb = kb.tell(new KnowledgeSet("#set1", List("sample:Cat rdfs:subclassOf sample:Pet", "sample:Pet rdfs:subclassOf sample:Animal"), List(), List()))
    // when
    kb = kb.infer
    // then
    val animals = kb.select(List("?pet rdf:type sample:Animal"))
    animals should have size (2)
    animals should contain(collection.Map[String, ConcreteNode]("pet" -> prefixStore("sample:aCat")))
    animals should contain(collection.Map[String, ConcreteNode]("pet" -> prefixStore("sample:anotherCat")))
  }
  
  it should "do recursive describe" in {
    // when
    val aboutAla = kb describe "sample:Ala"
    // then
    aboutAla should contain(string2triple("sample:aCat rdf:type sample:Cat"))
  }
}