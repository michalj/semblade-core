package eu.semantiq.semblade.parsers.sparql

import eu.semantiq.semblade._
import scala.util.parsing.combinator.syntactical.StdTokenParsers
import scala.util.parsing.combinator.ImplicitConversions

class SparqlParser extends StdTokenParsers with ImplicitConversions {
  type Tokens = SparqlLexer
  val lexical = new Tokens
  lexical.delimiters += ("{", "}", ".", ",", ":")

  def sparqlContent = ((prefixDecl *) ~> select) ~> repsep(variable, ",") ~ (where ~> "{" ~> repsep(triple, ".") <~ "}") ^^ { case selection ~ triples => triples }
  def prefixDecl = prefix ~> accept("identifier", { case lexical.Identifier(i) => i }) ~ (":" ~> uri) ^^ {
    case prefix ~ UriNode(uri) => nsBindings.set(nsBindings.get + (prefix -> uri))
  }
  def triple = node ~ node ~ node ^^ {
    case subject ~ verb ~ obj => QueryTriple(subject, verb, obj, true)
  }
  def node = variable | uri | string | prefixedUri
  def variable = accept("variable", { case lexical.Variable(v) => VariableNode(v) })
  def prefixedUri = ident ~ (":" ~> ident) ^^ {
    case prefix ~ suffix => UriNode(nsBindings.get.get(prefix).get + suffix)
  }
  def uri = accept("uri", { case lexical.Uri(u) => UriNode(u) })
  def string = accept("string", { case lexical.StringLit(s) => ValueNode(s, "xsd:string") })
  def prefix = lexical.Identifier("PREFIX")
  def select = lexical.Identifier("SELECT")
  def where = lexical.Identifier("WHERE")

  val nsBindings = new ThreadLocal[Map[String, String]]

  def parse(text: String): Seq[QueryTriple] = {
    nsBindings.set(Map())
    phrase(sparqlContent)(new lexical.Scanner(text)) match {
      case Success(tree, _) => tree
      case e: NoSuccess => throw new Exception(e.toString)
    }
  }
}