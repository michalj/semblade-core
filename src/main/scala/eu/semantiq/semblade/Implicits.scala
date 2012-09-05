package eu.semantiq.semblade

trait Implicits {
  def prefixStore: PrefixStore

  implicit def string2uriNode(s: String) = prefixStore(s)

  implicit def string2triple(s: String): Triple = s.split(" ").toSeq match {
    case Seq(obj, property, subject) => Triple(obj, property, subject)
    case Seq(obj, "not", property, subject) => Triple(obj, property, subject, false)
  }

  implicit def string2queryTriple(s: String): QueryTriple = {
    def processToken(token: String) =
      if (token.startsWith("?")) VariableNode(token.substring(1)) else prefixStore(token)
    s.split(" ").toSeq match {
      case Seq(obj, property, subject) => QueryTriple(
        processToken(obj), processToken(property), processToken(subject))
      case Seq(obj, "not", property, subject) => QueryTriple(
        processToken(obj), processToken(property), processToken(subject),
        false)
    }
  }

  implicit def string2ruleName(name: String) = RuleName(name)

  implicit def string2queryTripleList(string: String) = QueryTripleList(List(string))

  case class RuleName(name: String) {
    def ~=(implication: Implication) = SimpleRule(name, implication.pre, implication.post)
    def ~=(pf: PreconditionsAndFunction) = FunctionBasedRule(name, pf.pre, pf.f)
  }

  case class QueryTripleList(triples: List[QueryTriple]) {
    def ::(triple: QueryTriple) = QueryTripleList(triples :+ triple)
    def >>>(postconditions: QueryTripleList) =
      Implication(triples, postconditions.triples)
    def >>!(f: Iterable[Map[String, ConcreteNode]] => Iterable[Triple]) =
      PreconditionsAndFunction(triples, f)
  }

  case class Implication(pre: List[QueryTriple], post: List[QueryTriple])

  case class PreconditionsAndFunction(
    pre: List[QueryTriple],
    f: Iterable[Map[String, ConcreteNode]] => Iterable[Triple])
}

object DefaultImplicits extends Implicits {
  def prefixStore = DefaultPrefixStore
}