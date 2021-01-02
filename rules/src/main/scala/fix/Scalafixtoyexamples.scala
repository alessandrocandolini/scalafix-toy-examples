package fix

import scalafix.v1._
import scala.meta._
import metaconfig.Conf.Bool

import Scalafixtoyexamples._

class Scalafixtoyexamples extends SemanticRule("Scalafixtoyexamples") {

  def replace42(implicit doc: SemanticDocument): Patch =
    doc.tree.collect { case t @ Lit.Int(42) =>
      Patch.removeTokens(t.tokens) + Patch.addRight(t, "43")
    }.asPatch

  def replaceCoproduct(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case c: Defn.Class if c.isSealed && c.isAbstract => patch(c)
      case t: Defn.Trait if t.isSealed                 => patch(t)
    }.asPatch
  }

  def patch[T <: Tree](t: T)(implicit hasTemplate: HasTemplate[T]): Patch = {
    val template = t.template
    if (template.isEmpty) {
      Patch.addRight(t, s" extends Product with Serializable")
    } else {

      val productType = "Product"
      val serializableType = "Serializable"
      if (template.extendsType(productType) && template.extendsType(serializableType)) {
        Patch.empty
      } else if (template.extendsType(productType) && !template.extendsType(serializableType)) {
        Patch.addRight(template, s" with Serializable")
      } else if (!template.extendsType(productType) && template.extendsType(serializableType)) {
        Patch.addRight(template, s" with Product")
      } else {
        Patch.addRight(template, s" with Product with Serializable")
      }
    }
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

  trait HasTemplate[T] {
    def template(t: T): Template
  }

  object HasTemplate {
    def template[T](t: T)(implicit hasTemplate: HasTemplate[T]): Template =
      hasTemplate.template(t)
  }

  implicit val classHasTemplate: HasTemplate[Defn.Class] = new HasTemplate[Defn.Class] {

    override def template(t: Defn.Class): Template = t.templ

  }

  implicit val traitHasTemplate: HasTemplate[Defn.Trait] = new HasTemplate[Defn.Trait] {

    override def template(t: Defn.Trait): Template = t.templ

  }

  final implicit class HasTemplateOps[T: HasTemplate](private val t: T) {

    def template: Template = HasTemplate.template(t)

  }

  final implicit class TemplateOps(private val t: Template) {

    def isEmpty: Boolean = t.inits.isEmpty

    def extendsType: String => Boolean = t.inits.map(_.tpe.toString()).contains
  }
}
