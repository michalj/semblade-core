package eu.semantiq.semblade

import collection._

case class QueryTriple(subject: Node, verb: Node, obj: Node,
  positive: Boolean = true) {

  private def nodeMatches(node: Node, concrete: ConcreteNode) = node match {
    case node: ConcreteNode => node == concrete
    case any => true
  }

  def matches(t: Triple): Boolean = nodeMatches(subject, t.subject) && nodeMatches(verb, t.verb) &&
    nodeMatches(obj, t.obj) && positive == t.positive

  def apply(binding: Map[String, ConcreteNode]) = {
    def bind(node: Node) = node match {
      case concrete: ConcreteNode => concrete
      case VariableNode(variable) => binding(variable)
    }
    Triple(bind(subject), bind(verb), bind(obj), positive)
  }

  def partialApply(binding: Map[String, ConcreteNode]) = {
    def bind(node: Node) = node match {
      case VariableNode(variable) if (binding.contains(variable)) => binding(variable)
      case any => any
    }
    QueryTriple(bind(subject), bind(verb), bind(obj), positive)
  }
  def toTripleString: String = (if(positive) "" else "not ") + subject.toNodeString + " " + verb.toNodeString + " " + obj.toNodeString + "."
}
