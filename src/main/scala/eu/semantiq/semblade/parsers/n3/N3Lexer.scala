package eu.semantiq.semblade.parsers.n3

import util.parsing.combinator.ImplicitConversions
import util.parsing.combinator.lexical.StdLexical

class N3Lexer extends StdLexical with ImplicitConversions {
  case class UriLit(chars: String) extends Token {
    override def toString = "<" + chars + ">"
  }

  override def token: Parser[Token] =
    (delim
      | '@' ~ rep(letter) ^^ { case '@' ~ rest => new Keyword(rest mkString "") }
      | identChar ~ rep(identChar | digit) ^^ { case first ~ rest => new Identifier(first :: rest mkString "") }
      | uriLiteral ^^ UriLit
      | string ^^ StringLit
      | integer ^^ NumericLit)

  override def whitespace = rep(whitespaceChar)
  def uriLiteral = '<' ~> rep(letter | digit | '.' | '/' | '#' | ':') <~ '>' ^^ { _ mkString "" }
  def string = '\"' ~> rep(chrExcept('\"', '\n')) <~ '\"' ^^ { _ mkString "" }
  def integer = rep1(digit) ^^ { _ mkString "" }
}