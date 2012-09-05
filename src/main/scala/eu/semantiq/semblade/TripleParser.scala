package eu.semantiq.semblade

trait TripleParser {
  def prefixStore: PrefixStore
  
  implicit def string2uriNode(s: String) = prefixStore(s)

  implicit def string2triple(s: String): Triple = s.split(" ").toSeq match {
    case Seq(obj, property, subject) => Triple(obj, property, subject)
    case Seq(obj, "not", property, subject) => Triple(obj, property, subject, false)
  }

  implicit def string2queryTriple(s: String): QueryTriple = {
    def processToken(token: String) =
      if (token.startsWith("?")) VariableNode(token.substring(1)) else prefixStore(token)
    s.split(" ").toSeq match {
        case Seq(obj, property, subject) => QueryTriple(
            processToken(obj), processToken(property), processToken(subject))
        case Seq(obj, "not", property, subject) => QueryTriple(
            processToken(obj), processToken(property), processToken(subject),
            false)
    }
  }
}

object DefaultTripleParser extends TripleParser {
  def prefixStore = DefaultPrefixStore
}