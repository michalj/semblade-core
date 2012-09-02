package eu.semantiq.semblade

import org.scalatest.{BeforeAndAfterEach, FlatSpec}
import org.scalatest.matchers.ShouldMatchers

class PrefixStoreTest extends FlatSpec with ShouldMatchers with BeforeAndAfterEach {

  "UriNode" should "render itself using IPrefixStore" in {
    // given
    val urinode: UriNode = new UriNode("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")

    // when
    val actual = urinode.toNodeString(PrefixStore)
    val expected = PrefixedNode("rdf:type", Some(Prefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#")))

    // then
    actual should equal (expected)
  }

  "Triple" should "remove duplicated prefixes" in {
    // given
    val triple: Triple = new Triple(new UriNode("http://www.w3.org/1999/02/22-rdf-syntax-ns#a"), new UriNode("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), new UriNode("http://www.w3.org/1999/02/22-rdf-syntax-ns#b"))
    
    // when
    val actual = triple.toTripleString(PrefixStore)
    val expected = ("rdf:a rdf:type rdf:b.", Seq(Prefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#")))
    
    // then
    actual should equal (expected)
  }

  "KnowledgeSet" should "write prefixes" in {
    // given
    val triple: Triple = new Triple(new UriNode("http://www.w3.org/1999/02/22-rdf-syntax-ns#a"), new UriNode("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), new UriNode("http://www.w3.org/1999/02/22-rdf-syntax-ns#b"))
    val set: KnowledgeSet = KnowledgeSet("uri:test", Seq(triple), Seq(), Seq())
    
    // when
    val actual = set.toN3String(PrefixStore)
    val expected = "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>.rdf:a rdf:type rdf:b."
    
    // then
    actual should equal (expected)
  }
}
