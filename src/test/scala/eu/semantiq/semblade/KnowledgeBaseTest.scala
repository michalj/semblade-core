package eu.semantiq.semblade

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterEach, FlatSpec}
import collection._
import TripleParser._

class KnowledgeBaseTest extends FlatSpec with ShouldMatchers with BeforeAndAfterEach {

  var kb: IKnowledgeBase = _

  override def beforeEach() {
    kb = new KnowledgeBase
    kb.tell(new KnowledgeSet("#set0", List(
        "Ala owns aCat",
        "not Ala owns aDog",
        "aCat rdfs:isA Cat",
        "anotherCat rdfs:isA Cat"
    ), List(), List()))
    kb.tell(RDFS)
  }

	"Knowledge base" should "know what told" in {
		kb.dump should have size(4)
	}

	it should "shouldUpdateKnowledge" in {
		// when
		kb.tell(new KnowledgeSet("#set1", List("Ala likes aCat"), List(), List()))
		
		// then
		kb.dump should have size(5)
	}

  it should "run queries" in {
    // when
    val actual = kb.query(List("?who owns ?pet", "?pet rdfs:isA Cat"))

    // then
    actual should have size(1)
    actual.head("who") should be (UriNode("Ala"))
  }

  it should "run queries giving multiple results" in {
    // when
    val actual = kb.query(List("?pet rdfs:isA Cat"))

    // then
    actual should have size(2)
    actual should contain (Map[String, Node]("pet" -> UriNode("aCat")))
    actual should contain (Map[String, Node]("pet" -> UriNode("anotherCat")))
  }

  it should "do rule based inference" in {
    // given
    kb.tell(new KnowledgeSet("#set1", List("Cat rdfs:isSubclassOf Pet", "Pet rdfs:isSubclassOf Animal"), List(), List()))

    // when
    kb.infer

    // then
    val animals = kb.query(List("?pet rdfs:isA Animal"))
    animals should have size(2)
    animals should contain (Map[String, Node]("pet" -> UriNode("aCat")))
    animals should contain (Map[String, Node]("pet" -> UriNode("anotherCat")))
  }

}