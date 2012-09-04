package eu.semantiq.semblade

import scala.collection.JavaConversions._

class PrefixStore(prefixes: Map[String, String]) {
  def apply(prefixedUri: String) = prefixedUri.split(":").toSeq match {
    case Seq(prefix, suffix) => UriNode(prefixes(prefix) + suffix)
  }
  def unapply(node: UriNode) = prefixes
    .find({ case (prefix, uri) => node.uri.startsWith(uri) })
    .map({ case (prefix, uri) => prefix + ":" + node.uri.substring(uri.length) })
  def ++(otherPrefixes: Map[String, String]) = new PrefixStore(prefixes ++ otherPrefixes)
}

object DefaultPrefixStore extends PrefixStore(Map(
  "rdf" -> "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
  "rdfs" -> "http://w3.org/2000/01/rdf-schema#",
  "foaf" -> "http://xmlns.com/foaf/0.1/",
  "owl" -> "http://www.w3.org/2002/07/owl#")) {}