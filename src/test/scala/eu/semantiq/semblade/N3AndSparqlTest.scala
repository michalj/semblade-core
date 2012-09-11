package eu.semantiq.semblade

import org.scalatest.{BeforeAndAfterEach, FlatSpec}
import org.scalatest.matchers.ShouldMatchers

class N3AndSparqlTest extends FlatSpec with ShouldMatchers with BeforeAndAfterEach {

  "Node" should "generate own N3/SPARQL representation" in {
    // given
    val varnode: VariableNode = new VariableNode("a")
    val urinode: UriNode = new UriNode("http://google.com#")
    val valnode: ValueNode = new ValueNode("1996-09-17", "xsd:Date")

    // when
    val r_varnode = varnode.toString
    val r_urinode = urinode.toString
    val r_valnode = valnode.toString

    // then
    r_varnode should equal ("?a")
    r_urinode should equal ("<http://google.com#>")
    r_valnode should equal ("\"1996-09-17\"^xsd:Date")
  }

  "Triple and query triple" should "generate own N3/SPARQL representation" in {
    // given
    val triple: Triple = new Triple(new UriNode("uri:a"), new UriNode("uri:b"), new ValueNode("c", "d:e"), true)
    val qtriple: QueryTriple = new QueryTriple(new UriNode("uri:a"), new UriNode("uri:b"), new VariableNode("a"), true)

    // when
    val r_triple: String = triple.toString
    val r_qtriple: String = qtriple.toString

    // then
    r_triple should equal ("<uri:a> <uri:b> \"c\"^d:e.")
    r_qtriple should equal ("<uri:a> <uri:b> ?a.")
  }
  
  "Knowledge set" should "generate N3 representation" in {
    // given
    val t1: Triple = new Triple(new UriNode("uri:a"), new UriNode("uri:b"), new ValueNode("c", "d:e"), true)
    val t2: Triple = new Triple(new UriNode("uri:f"), new UriNode("uri:g"), new ValueNode("h", "i:j"), true)
    val ks: KnowledgeSet = KnowledgeSet("http://temporary.org/test", List(t1, t2), List(), List())

    // when
    val r_ks: String = ks.toN3String

    // then
    r_ks should equal ("<uri:a> <uri:b> \"c\"^d:e.<uri:f> <uri:g> \"h\"^i:j.")
  }

  "Query" should "generate SPARQL representation" in {
    // given
    val qt1: QueryTriple = new QueryTriple(new UriNode("uri:a"), new UriNode("uri:b"), new VariableNode("a"), true)
    val qt2: QueryTriple = new QueryTriple(new UriNode("uri:f"), new VariableNode("a"), new ValueNode("h", "i:j"), true)
    val qwl: Query = new Query("SELECT *", List(qt1, qt2), 10)
    val qwol: Query = new Query("ASK", List(qt1, qt2), 0)

    // when
    val r_sparqlwl: String = qwl.toSparqlString
    val r_sparqlwol: String = qwol.toSparqlString

    // then
    r_sparqlwl should equal ("SELECT * WHERE {\n\t<uri:a> <uri:b> ?a.\n\t<uri:f> ?a \"h\"^i:j.\n} LIMIT 10")
    r_sparqlwol should equal ("ASK WHERE {\n\t<uri:a> <uri:b> ?a.\n\t<uri:f> ?a \"h\"^i:j.\n}")
  }
}
