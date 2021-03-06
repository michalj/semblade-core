package eu.semantiq.semblade

trait Object
abstract class Node
abstract class ConcreteNode extends Node with Object
case class VariableNode(variable: String) extends Node {
  override def toString = "?" + variable
}
case class UriNode(uri: String) extends ConcreteNode {
  override def toString = "<" + uri + ">"
}
case class ValueNode(value: String, typeUri: String) extends ConcreteNode {
  override def toString = "\"" + value + "\"^" + typeUri //TODO: encode string
}
case class ListObject(list: Seq[ConcreteNode]) extends Object {
  override def toString = "(" + list.mkString(", ") + ")"
}

abstract class Statement
/**
 * Represents RDF triple.
 *
 * @param subject URI of object described by the triple.
 * @param verb URI of object verb described by the triple.
 * @param `obj` URI or literal value of object verb.
 * @param positive is the Triple positive (true by default)
 */
case class Triple(subject: ConcreteNode, verb: ConcreteNode, obj: ConcreteNode,
  positive: Boolean = true) extends Statement {
  override def toString = subject.toString + " " +
    (if (positive) "" else "not ") + verb.toString + " " +
    obj.toString + "."
}

case class RichTriple(subject: ConcreteNode, verb: ConcreteNode, obj: Object,
  positive: Boolean = true) extends Statement {
  override def toString = subject.toString + " " +
    (if (positive) "" else "not ") + verb.toString + " " +
    obj.toString + "."
}

/**
 * All rules need to:
 * <ul>
 * <li>be deterministic</li>
 * <li>not create any new nodes</li>
 * <li>give no side effects</li>
 * </ul>
 */
abstract class Rule(uri: String, preconditions: Seq[QueryTriple])
  extends Statement {
  def getPreconditions = preconditions
  def generateImplications(inputs: Iterable[Map[String, ConcreteNode]]): Iterable[Triple]
}

/**
 * SPARQL based inference rule.
 *
 * Rules are applied by binding precondition queries and generating implications
 * by applying the bindings to implication templates.
 *
 * Example: given the following rule:
 *
 * {{{
 * rule rdfs:implcation
 * when ?a rdfs:isA ?subClass
 * and ?subClass rdfs:isSubclassOf ?superClass
 * then ?a rdfs:iA ?superClass.
 * }}}
 *
 * And knowing that:
 *
 * {{{
 * t:pankracy rdfs:isA t:Cat.
 * t:Cat rdfs:isSubclassOf t:Animal.
 * }}}
 *
 * Inference engine will find the following binding:
 *
 * {{{
 * a => t:pankracy
 * superClass => t:Animal
 * subClass => t:Cat
 * }}}
 *
 * And after applying this binding to implication template, will infer that:
 *
 * {{{
 * t:pankracy rdfs:isA t:Animal.
 * }}}
 *
 * @param uri URI to identify this rule.
 * @param preconditions Query to find if rule is applicable and bind rule
 * 		variables.
 * @param implications Template to generate the n3content implied by this rule.
 */
case class SimpleRule(
  uri: String,
  preconditions: Seq[QueryTriple],
  implications: Seq[QueryTriple]) extends Rule(uri, preconditions) {
  def generateImplications(inputs: Iterable[Map[String, ConcreteNode]]) =
    inputs.flatMap(b => implications.map(qt => qt.apply(b)))
}

case class FunctionBasedRule(
  uri: String,
  preconditions: Seq[QueryTriple],
  f: Iterable[Map[String, ConcreteNode]] => Iterable[Triple])
  extends Rule(uri, preconditions) {
  def generateImplications(inputs: Iterable[Map[String, ConcreteNode]]) = f(inputs)
}

/**
 * Encapsulation of knowledge sharing common meta-data, like origin, trust,
 * mode of invalidation.
 *
 * @param uri URI identifying knowledge set.
 * @param n3content Set of n3content containing the knowledge in this Knowledge
 * 		set.
 * @param rules Set of [[eu.semantiq.semblade.Rule]]s known by this Knowledge
 * 		set.
 * @param metadata Set of triples describing meta-data of this KnowledgeSet. It
 * 		should include trust & privacy information, origin and rules for
 * 		invalidation.
 */
case class KnowledgeSet(uri: String, triples: Seq[Triple], rules: Seq[Rule],
  metadata: Seq[Triple]) {
  def toN3String: String = triples.map(triple => triple.toString).reduceLeft(_ + _)
}

trait SelectableKnowledgeSource {
  def select(quer: Seq[QueryTriple]): Iterable[Map[String, ConcreteNode]]
  def ?(triples: QueryTriple*) = select(triples)
}

trait DumpableKnowledgeSource {
  def dump: Iterable[Triple]
}

trait DescriptiveKnowledgeSource {
  def describe(node: ConcreteNode, level: Int = 1): Seq[Triple]
}

trait KnowledgeBase extends SelectableKnowledgeSource
  with DumpableKnowledgeSource with DescriptiveKnowledgeSource {
  def tell(knowledgeSet: KnowledgeSet): KnowledgeBase
  def infer: KnowledgeBase
  def ! = infer
  def +(knowledgeSet: KnowledgeSet) = tell(knowledgeSet)
}