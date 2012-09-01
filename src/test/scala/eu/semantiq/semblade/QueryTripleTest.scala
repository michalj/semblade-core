package eu.semantiq.semblade

import org.scalatest.{BeforeAndAfterEach, FlatSpec}
import org.scalatest.matchers.ShouldMatchers
import TripleParser._

class QueryTripleTest extends FlatSpec with ShouldMatchers with BeforeAndAfterEach {

  "Query triple" should "apply partial binding" in {
    // given
    val query: QueryTriple = "?who rdfs:isA ?type"
    val binding = Map("type" -> UriNode("SomeType"))

    // when
    val actual = query.partialApply(binding)

    // then
    val expected: QueryTriple = "?who rdfs:isA SomeType"
    actual should equal (expected)
  }

}