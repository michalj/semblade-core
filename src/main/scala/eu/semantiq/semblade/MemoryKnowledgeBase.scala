package eu.semantiq.semblade

import org.slf4j.LoggerFactory

class MemoryKnowledgeBase(
    database: Map[String, KnowledgeSet] = Map(), 
    inferred: Set[Triple] = Set()) extends KnowledgeBase {
  val log = LoggerFactory.getLogger(getClass)

  def tell(knowledgeSet: KnowledgeSet) =
    new MemoryKnowledgeBase(database + (knowledgeSet.uri -> knowledgeSet))

  private def query(triples: Iterable[Triple], currentQuery: Seq[QueryTriple], binding: Map[String, ConcreteNode]): Iterable[Map[String, ConcreteNode]] = {
    if (currentQuery.size == 0) return List(binding)
    val q = currentQuery.head.partialApply(binding)
    def getBinding(node: Node, concrete: ConcreteNode) = node match {
      case VariableNode(variable) => Some((variable, concrete))
      case _ => None
    }
    return triples.filter(t => q.matches(t))
      .map(t => binding ++
        getBinding(q.subject, t.subject) ++
        getBinding(q.verb, t.verb) ++
        getBinding(q.obj, t.obj))
      .flatMap(newBinding => query(triples, currentQuery.tail, newBinding))
  }

  def select(queryTriples: Seq[QueryTriple]) = query(dump, queryTriples, Map())

  def dump: Iterable[Triple] = database.values.flatMap(ks => ks.triples) ++ inferred

  def infer = {
    def infer(knowledge: Set[Triple], rules: Iterable[Rule]): Set[Triple] = {
      val newKnowledge = rules
        .flatMap(r => r.generateImplications(query(knowledge, r.getPreconditions, Map())))
        .filter(t => !knowledge.contains(t))
      if (newKnowledge.size == 0) return knowledge
      log.debug("inferred: " + newKnowledge)
      return infer(knowledge ++ newKnowledge, rules)
    }
    val newInferred = infer(dump.toSet, database.values.flatMap(ks => ks.rules))
    new MemoryKnowledgeBase(database, newInferred)
  }
}