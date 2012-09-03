package eu.semantiq.semblade

import org.scalatest.{BeforeAndAfterEach, FlatSpec}
import org.scalatest.matchers.ShouldMatchers

class PrefixStoreTest extends FlatSpec with ShouldMatchers with BeforeAndAfterEach {

  "UriNode" should "be createble from prefixed string" in {
    // given
    val prefixedUri = "rdf:type"
    // when
    val actual = DefaultPrefixStore(prefixedUri)
    // then
    actual should be (UriNode("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"))
  }

  "UriNode" should "be renderable to prefixed string" in {
    // given
    val uriNode = UriNode("http://www.w3.org/1999/02/22-rdf-syntax-ns#a")
    // when
    val DefaultPrefixStore(actual) = uriNode
    // then
    actual should be ("rdf:a")
  }
}