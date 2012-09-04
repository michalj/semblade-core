package eu.semantiq.semblade

import org.scalatest.{BeforeAndAfterEach, FlatSpec}
import org.scalatest.matchers.ShouldMatchers

class QueryTripleTest extends FlatSpec with ShouldMatchers
with BeforeAndAfterEach
with TripleParser {
  def prefixStore = DefaultPrefixStore ++ Map("sample" -> "http://semantiq.eu/ontologies/sample#")

  "Query triple" should "apply partial binding" in {
    // given
    val query: QueryTriple = "?who rdf:type ?type"
    val binding = Map("type" -> prefixStore("sample:SomeType"))

    // when
    val actual = query.partialApply(binding)

    // then
    val expected: QueryTriple = "?who rdf:type sample:SomeType"
    actual should equal (expected)
  }

}