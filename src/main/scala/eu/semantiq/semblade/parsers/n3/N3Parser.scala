package eu.semantiq.semblade.parsers.n3

import eu.semantiq.semblade._
import util.parsing.combinator.ImplicitConversions
import util.parsing.combinator.lexical.StdLexical
import util.parsing.combinator.syntactical.StdTokenParsers

object N3Parser extends StdTokenParsers with ImplicitConversions {

  type Tokens = N3Lexer
  val lexical = new Tokens

  lexical.delimiters += (".", ":", ",", "(", ")")

  def uri = uriLit ^^ { UriNode(_) } | ident ~ (":" ~> ident) ^^ {
      case prefix ~ value => UriNode(nsBindings.get.get(prefix).get + value)
    }
  def node = (uri
    | stringLit ^^ { ValueNode(_, "xsd:string") }
    | numericLit ^^ { ValueNode(_, "xsd:int") }
    | accept("boolean", {
      case lexical.Identifier(value) if (value == "true") => ValueNode("true", "xsd:boolean")
      case lexical.Identifier(value) if (value == "false") => ValueNode("false", "xsd:boolean")
    }))
  def listContent = (node *) ^^ { _.map(t => t) }
  def list = "(" ~> listContent <~ ")" 
  def triple = node ~ node ~ node <~ "." ^^ { case subj ~ verb ~ obj => Some(Triple(subj, verb, obj, true)) }
  def tripleList = node ~ node ~ list <~ "." ^^ { case subj ~ verb ~ list => Some(RichTriple(subj, verb, ListObject(list), true)) }
  def prefix = lexical.Keyword("prefix") ~ (ident <~ ":") ~ uriLit <~ "." ^^ {
    case prefix ~ ns ~ uri =>
      nsBindings.set(nsBindings.get + (ns.toString -> uri.toString))
      None
  }
  def n3content = ((prefix | triple | tripleList)*) ^^ { _.flatMap(t => t) }
  def uriLit = accept("URI", { case lexical.UriLit(n) => n })

  val nsBindings = new ThreadLocal[Map[String, String]]

  def parse(text: String, initialNamespaces: Map[String, String] = Map()): Seq[Statement] = {
    nsBindings.set(initialNamespaces)
    phrase(n3content)(new lexical.Scanner(text)) match {
      case Success(tree, _) => tree
      case e: NoSuccess => throw new Exception(e.toString)
    }
  }
}