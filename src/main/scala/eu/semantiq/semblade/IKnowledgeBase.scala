package eu.semantiq.semblade

import collection._
trait IKnowledgeSource {
  def makeAnyQuery(quer: Query): Iterable[Map[String, ConcreteNode]] = query(quer.queryTriples)
  def query(quer: Seq[QueryTriple]): Iterable[Map[String, ConcreteNode]]
}
trait IKnowledgeBase extends IKnowledgeSource {
  def tell(knowledgeSet: KnowledgeSet)
  def dump: Iterable[Triple]
  def infer
}
