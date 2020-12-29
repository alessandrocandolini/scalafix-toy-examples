package fix

import scalafix.v1._
import scala.meta._

class Scalafixtoyexamples extends SemanticRule("Scalafixtoyexamples") {

  def replace42(implicit doc: SemanticDocument): Patch = 
    doc.tree.collect {
      case t @ Lit.Int(42) => 
        Patch.removeTokens(t.tokens) + Patch.addRight(t, "43")
    }.asPatch
  
  def replaceCoproduct(implicit doc: SemanticDocument) : Patch = ???


  override def fix(implicit doc: SemanticDocument): Patch = {
    println("Tree.syntax: " + doc.tree.syntax)
    println("Tree.structure: " + doc.tree.structure)
    println("Tree.structureLabeled: " + doc.tree.structureLabeled)
    replace42 + replaceCoproduct
  }

}
