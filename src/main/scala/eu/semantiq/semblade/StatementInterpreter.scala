package eu.semantiq.semblade

import DefaultImplicits._

object StatementInterpreter {
  def apply(uri: String, statements: Seq[Statement],
    metadata: Seq[Triple]) = {
    var counter = 0
    def autoUri = {
      counter += 1
      UriNode(uri + "auto" + counter)
    }
    def generateList(uri: UriNode, list: Seq[ConcreteNode]): Seq[Triple] = if (list.isEmpty) {
      Seq(Triple(uri, "owl:sameAs", "rdf:nil"))
    } else {
      val restUri = autoUri
      Triple(uri, "rdf:first", list.head) +:
        Triple(uri, "rdf:rest", restUri) +:
        generateList(restUri, list.tail)
    }
    val triples = statements.flatMap({
      case t: Triple => Some(t)
      case RichTriple(obj, verb, ListObject(list), true) => {
        val listUri = autoUri
        generateList(listUri, list) :+ Triple(obj, verb, listUri)
      }
    })
    KnowledgeSet(uri, triples, Seq(), metadata)
  }

}