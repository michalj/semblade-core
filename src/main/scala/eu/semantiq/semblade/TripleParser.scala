package eu.semantiq.semblade

object TripleParser {

  implicit def string2triple(s: String): Triple = {
    val allTokens = s.split(" ")
    val (tokens, positive) = if (allTokens(0) == "not") (allTokens.tail, false) else (allTokens, true)

    new Triple(
      UriNode(tokens(0)),
      UriNode(tokens(1)),
      UriNode(tokens(2)), positive)
  }

  implicit def string2queryTriple(s: String): QueryTriple = {
    def processToken(token: String) = if (token.startsWith("?")) VariableNode(token.substring(1)) else UriNode(token)
    val allTokens = s.split(" ")
    val (tokens, positive) = if (allTokens(0) == "not") (allTokens.tail, false) else (allTokens, true)
    QueryTriple(processToken(tokens(0)), processToken(tokens(1)), processToken(tokens(2)), positive)
  }

}