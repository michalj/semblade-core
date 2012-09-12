package eu.semantiq.semblade.parsers.n3

import eu.semantiq.semblade._
import org.scalatest.{ FlatSpec, BeforeAndAfterEach }
import org.scalatest.matchers.ShouldMatchers

class N3ParserTest extends FlatSpec with ShouldMatchers with BeforeAndAfterEach {

  val parser = N3Parser

  "N3 Parser" should "parse basic set of triples with full URIs" in {
    // given
    val n3 =
      "<abc> <abc> <http://vocabulary.semantiq.eu/services/1.0/Service>.\n" +
        "<abc> <cba> <bac>."

    // when
    val actual = parser.parse(n3)

    // then
    actual should be(List(
      Triple(UriNode("abc"), UriNode("abc"), UriNode("http://vocabulary.semantiq.eu/services/1.0/Service"), true),
      Triple(UriNode("abc"), UriNode("cba"), UriNode("bac"), true)))
  }

  it should "handle prefix declarations" in {
    // given
    val n3 =
      "@prefix sq : <http://vocabulary.semantiq.eu/services/1.0/>.\n" +
        "sq:Service <is> <here>.\n"

    // when
    val actual = parser.parse(n3)

    // then
    actual should be(List(
      Triple(UriNode("http://vocabulary.semantiq.eu/services/1.0/Service"), UriNode("is"), UriNode("here"), true)))
  }

  it should "handle string literals" in {
    // given
    val n3 =
      "<this> <title> \"Sample N3 input\".\n" +
        "\"table\" <isA> <Noun>."

    // when
    val actual = parser.parse(n3)

    // then
    actual should be(List(
      Triple(UriNode("this"), UriNode("title"), ValueNode("Sample N3 input", "xsd:string"), true),
      Triple(ValueNode("table", "xsd:string"), UriNode("isA"), UriNode("Noun"), true)))
  }

  it should "handle integer literals" in {
    // given
    val n3 = "<me> <age> 25.\n"

    // when
    val actual = parser.parse(n3)

    // then
    actual should be(List(
      Triple(UriNode("me"), UriNode("age"), ValueNode("25", "xsd:int"), true)))
  }

  it should "handle boolean literals" in {
    // given
    val n3 = "<me> <know> true.\n" +
      "<me> <donnow> false."

    // when
    val actual = parser.parse(n3)

    // then
    actual should be(List(
      Triple(UriNode("me"), UriNode("know"), ValueNode("true", "xsd:boolean"), true),
      Triple(UriNode("me"), UriNode("donnow"), ValueNode("false", "xsd:boolean"), true)))
  }

  it should "handle lists of nodes" in {
    // given
    val n3 = "<me> <likes> (<candy> <cookie>)."
      
    // when
    val actual = parser.parse(n3)
    
    // then
    actual should be (List(
        RichTriple(UriNode("me"), UriNode("likes"), ListObject(Seq(
            UriNode("candy"),
            UriNode("cookie"))))
    ))
  }
}