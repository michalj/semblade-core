package eu.semantiq.semblade

import org.scalatest.{BeforeAndAfterEach, FlatSpec}
import org.scalatest.matchers.ShouldMatchers

class SparqlEndpointTest extends FlatSpec with ShouldMatchers with BeforeAndAfterEach {

  "Sparql endpoint" should "make a query to dbpedia.org/sparql" in {
    // given
    val ks: SparqlEndpoint = new SparqlEndpoint("http://dbpedia.org/sparql")
    val query: Seq[QueryTriple] = List(
      new QueryTriple(new UriNode("http://dbpedia.org/resource/Wejherowo"), new UriNode("http://dbpedia.org/property/postalCode"), new VariableNode("postalcode"), true)
    )
    val expected: Iterable[Map[String, ConcreteNode]] = List(Map("postalcode" -> new ValueNode("84", "http://www.w3.org/2001/XMLSchema#int")))

    // when
    val result = ks.query(query)

    // then
    result should equal (expected)
  }
}
