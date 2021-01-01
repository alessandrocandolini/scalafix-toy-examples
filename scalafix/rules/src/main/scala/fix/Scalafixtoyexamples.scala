package fix

import scalafix.v1._
import scala.meta._

class Scalafixtoyexamples extends SemanticRule("Scalafixtoyexamples") {

  def replace42(implicit doc: SemanticDocument): Patch =
    doc.tree.collect { case t @ Lit.Int(42) =>
      Patch.removeTokens(t.tokens) + Patch.addRight(t, "43")
    }.asPatch

  def replaceCoproduct(implicit doc: SemanticDocument): Patch = {
    import Scalafixtoyexamples._
    doc.tree.collect {
      case c: Defn.Class if c.isSealed && c.isAbstract => Patch.empty
      case t: Defn.Trait if t.isSealed                 => Patch.empty
    }.asPatch
  }

  override def fix(implicit doc: SemanticDocument): Patch = {
    // print code has is
    println("Tree.syntax: " + doc.tree.syntax)
    println("Tree.structure: " + doc.tree.structure)
    println("Tree.structureLabeled: " + doc.tree.structureLabeled)
    replace42 + replaceCoproduct
  }

}

object Scalafixtoyexamples {

  trait HasMods[T] {
    def mods(t: T): List[Mod]
  }

  object HasMods {
    def mods[T](t: T)(implicit hasMods: HasMods[T]): List[Mod] = hasMods.mods(t)
  }

  final implicit class HasModOps[T: HasMods](private val t: T) {
    private def tmods = HasMods.mods(t)
    def isSealed = tmods.exists(_.is[Mod.Sealed])
    def isAbstract = tmods.exists(_.is[Mod.Abstract])
  }

  implicit val classHasMods: HasMods[Defn.Class] = new HasMods[Defn.Class] {

    override def mods(t: Defn.Class): List[Mod] = t.mods

  }

  implicit val traitHasMods: HasMods[Defn.Trait] = new HasMods[Defn.Trait] {

    override def mods(t: Defn.Trait): List[Mod] = t.mods

  }

}
