package eu.semantiq.semblade

import java.net.URLEncoder

import scala.xml.NodeSeq.seqToNodeSeq
import scala.xml.XML

object SparqlQueryResultsXmlParser {
  def parse(xml: scala.xml.Elem): Iterable[Map[String, ConcreteNode]] = {
    def bindingToNode(root: scala.xml.Node) = {
      // Generates Node
      if((root\\"uri").size > 0)
        new UriNode(root\\"uri" text)
      else
        new ValueNode(root\\"literal" text, (root\\"literal").head.attribute("datatype").get.text)
    }
    xml\\"sparql"\\"results"\\"result" map(r => {
      // Result
      var result = Map[String, ConcreteNode]()
      (r\\"binding").foreach(binding => result += binding.attribute("name").get.text -> bindingToNode(binding)) // Bindings
      result
    })
  }
}

object ResultsPrinter {
  def printSelectResults(q: Iterable[Map[String, ConcreteNode]]) = {
    var to_print = ""
    q.headOption.getOrElse(Map()).foreach(item => to_print += item._1 + "\t")
    to_print += "\n"
    q.foreach(map => {
      map.foreach(item => to_print += item._2.toNodeString + "\t")
      to_print += "\n"
    })
    println(to_print)
  }
}

case class SparqlEndpoint(url: String) extends IKnowledgeSource {
  def query(queryTriples: Seq[QueryTriple]): Iterable[Map[String, ConcreteNode]] = makeAnyQuery(new Query("SELECT *", queryTriples, -1))
  override def makeAnyQuery(query: Query): Iterable[Map[String, ConcreteNode]] = {
    SparqlQueryResultsXmlParser.parse(
      XML.load(url + "/?format=application/sparql-results+xml&query=" + URLEncoder.encode(query.toSparqlString, "UTF-8"))
    )
  }
}

