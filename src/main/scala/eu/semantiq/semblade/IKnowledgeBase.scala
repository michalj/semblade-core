package eu.semantiq.semblade

trait IKnowledgeSource {
  def makeAnyQuery(quer: Query): Iterable[Map[String, ConcreteNode]] = query(quer.queryTriples)
  def query(quer: Seq[QueryTriple]): Iterable[Map[String, ConcreteNode]]
  def ?(triples: QueryTriple*) = query(triples)
}
trait IKnowledgeBase extends IKnowledgeSource {
  def tell(knowledgeSet: KnowledgeSet): IKnowledgeBase
  def dump: Iterable[Triple]
  def infer: IKnowledgeBase
  def ! = infer
  def +(knowledgeSet: KnowledgeSet) = tell(knowledgeSet)
  def +(triple: Triple) = tell(KnowledgeSet("triple:" + triple.hashCode,
      List(triple), List(), List()))
}