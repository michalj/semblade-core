package eu.semantiq.semblade

case class Prefix(prefix: String, uri: String)
case class PrefixedNode(shortcut: String, prefix: Option[Prefix])

trait IPrefixStore {
  var prefixes: Map[String, String]
  def apply(node: UriNode): PrefixedNode
}

object PrefixStore extends IPrefixStore {
  var prefixes = Map(
      "rdf" -> "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
      "rdfs" -> "http://w3.org/2000/01/rdf-schema#",
      "foaf" -> "http://xmlns.com/foaf/0.1/"
  )
  
  def apply(node: UriNode) = {
    var longestMatchingPrefix: Option[Prefix] = None // (prefix, uri)
    prefixes.map { case(prefix, uri) =>
      if(node.uri.startsWith(uri)) {
        longestMatchingPrefix match {
          case Some(Prefix(p, u)) => {
            if(u.size < uri.size)
              longestMatchingPrefix = Some(Prefix(prefix, uri))
          }
          case None => {
            longestMatchingPrefix = Some(Prefix(prefix, uri))
          }
        }
      }
    }
    
    longestMatchingPrefix match {
      case Some(Prefix(p, u)) => PrefixedNode(p + ":" + node.uri.replace(u, ""), Some(Prefix(p, u)))
      case None => PrefixedNode(node.uri, None)
    }
  }
}