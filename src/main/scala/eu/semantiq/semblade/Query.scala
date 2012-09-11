package eu.semantiq.semblade

/**
 * Represents any SPARQL Query.
 * @param method Type of query. Eg. "SELECT *", "ASK", "CONSTRUCT", "DESCRIBE"
 * @param queryTriples list of QueryTriples used in WHERE { ... } clausule
 * @param limit Maximum number of mappings. Used only when positive
 */
case class Query(method: String, queryTriples: Seq[QueryTriple], limit: Int) {
  def toSparqlString = { method + " WHERE {\n" + queryTriples.map(triple => "\t" + triple.toString + "\n").reduceLeft(_ + _) + "}" + (if(limit > 0) " LIMIT " + limit.toString else "") }
}
