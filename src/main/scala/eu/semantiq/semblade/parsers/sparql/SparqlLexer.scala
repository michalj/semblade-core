package eu.semantiq.semblade.parsers.sparql

import scala.util.parsing.combinator.RegexParsers
import scala.util.matching.Regex
import scala.util.parsing.combinator.token.StdTokens
import scala.util.parsing.combinator.lexical.StdLexical
import scala.util.parsing.combinator.ImplicitConversions

class SparqlLexer extends StdLexical with ImplicitConversions {
  case class Variable(chars: String) extends Token {
	  override def toString = "?" + chars
  }
  case class Uri(chars: String) extends Token

  override def token: Parser[Token] =
    (delim
      | variable ^^ Variable
      | identifier ^^ Identifier
      | uri ^^ Uri
      | string ^^ StringLit)

  def identifier = identChar ~ rep(identChar | digit) ^^ { case first ~ rest => first :: rest mkString "" }
  def variable = '?' ~> identifier
  def uri = '<' ~> rep(letter | digit | '.' | '/' | '#' | ':') <~ '>' ^^ { _ mkString "" }
  def string = '\"' ~> rep(chrExcept('\"', '\n')) <~ '\"' ^^ { _ mkString "" }
}