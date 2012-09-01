package eu.semantiq.semblade.parsers.sparql

import eu.semantiq.semblade._
import org.scalatest.{ FlatSpec, BeforeAndAfterEach }
import org.scalatest.matchers.ShouldMatchers

class SparqlParserTest extends FlatSpec with ShouldMatchers with BeforeAndAfterEach {

  val parser = new SparqlParser()

  "SPARQL Parser" should "parse basic query with full URIs and variables" in {
    // given
    val sparql =
      "SELECT ?name WHERE { ?subject ?verb ?object.\n" +
        "?subject <isA> <Man> }"

    // when
    val actual = parser.parse(sparql)

    // then
    actual should be(List(
      QueryTriple(VariableNode("subject"), VariableNode("verb"), VariableNode("object"), true),
      QueryTriple(VariableNode("subject"), UriNode("isA"), UriNode("Man"), true)))
  }

  it should "handle string literals" in {
    // given
    val sparql =
      "SELECT ?name WHERE { ?person <name> \"Roman\" }"

    // when
    val actual = parser.parse(sparql)

    // then
    actual should be(List(
      QueryTriple(VariableNode("person"), UriNode("name"), ValueNode("Roman", "xsd:string"), true)))
  }

  it should "handle prefix keyword" in {
    // given
    val sparql =
      "PREFIX sq: <http://semantiq.eu/>\n" +
        "PREFIX ex: <http://example.org/>\n" +
        "SELECT ?x WHERE { ?x <isA> sq:Service.\n" +
        "?x <isA> ex:Example }"

    // when
    val actual = parser.parse(sparql);

    //then
    actual should be(List(
      QueryTriple(VariableNode("x"), UriNode("isA"), UriNode("http://semantiq.eu/Service"), true),
      QueryTriple(VariableNode("x"), UriNode("isA"), UriNode("http://example.org/Example"), true)))
  }
}
