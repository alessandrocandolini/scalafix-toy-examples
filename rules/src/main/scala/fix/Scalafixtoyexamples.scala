package fix

import scalafix.v1._
import scala.meta._
import metaconfig.Conf.Bool

import Scalafixtoyexamples._
import scala.collection.immutable.Nil
import scala.meta.internal.semanticdb.Scala.Names.TermName

class Scalafixtoyexamples extends SemanticRule("Scalafixtoyexamples") {

  def replaceLiterals(implicit doc: SemanticDocument): Patch =
    doc.tree.collect { case t @ Lit.Int(42) =>
      Patch.replaceTree(t, "43")
    }.asPatch

  def addProductWithSerializable(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case c: Defn.Class if c.isSealed && c.isAbstract =>
        addProductWithSerializableToTemplate(c)
      case t: Defn.Trait if t.isSealed =>
        addProductWithSerializableToTemplate(t)
    }.asPatch
  }

  private def addProductWithSerializableToTemplate[T <: Tree](
      t: T
  )(implicit hasTemplate: HasTemplate[T]): Patch = {
    val template = t.template
    if (template.isEmpty) {
      Patch.addRight(t, s" extends Product with Serializable")
    } else {
      val tpes = template.inits.map(_.tpe.syntax)

      val oldTokens = template.tokens
        .map(_.text)
        .filter(p => p != "with" && p != "Product" && p != "Serializable")
        .map(_.trim())
        .filter(_.nonEmpty)

      val newTokens = oldTokens ++ List("Product", "Serializable")

      val removeTokensPatch = Patch.removeTokens(template.tokens)
      val addTokenPatch =
        Patch.addRight(template, newTokens.mkString(" with "))

      addTokenPatch + removeTokensPatch

    }
  }

  def finalCaseClass(implicit doc: SemanticDocument): Patch = doc.tree.collect {
    case c: Defn.Class if c.isCase && !c.isFinal => Patch.addLeft(c, "final ")
  }.asPatch

  def replaceCoproduct(implicit doc: SemanticDocument): Patch = {
    // todo remove (for debugging)
    val coproductSymbolMatcher = SymbolMatcher.exact("cats/data/Coproduct#")
    doc.tree.collect {
      case t @ coproductSymbolMatcher(name) => {
        println(name.pos.formatMessage("info", s"COPRODUCT FOUND: ${t.syntax}"))
      }
    }

    Patch.replaceSymbols("cats/data/Coproduct" -> "cats/data/EitherK")
  }

  override def fix(implicit doc: SemanticDocument): Patch = {
    // print code has is
    println("Tree.syntax: " + doc.tree.syntax)
    println("Tree.structure: " + doc.tree.structure)
    println("Tree.structureLabeled: " + doc.tree.structureLabeled)
    replaceLiterals + replaceCoproduct + finalCaseClass + addProductWithSerializable
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
    def isFinal = tmods.exists(_.is[Mod.Final])
    def isCase = tmods.exists(_.is[Mod.Case])
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

  implicit val classHasTemplate: HasTemplate[Defn.Class] =
    new HasTemplate[Defn.Class] {

      override def template(t: Defn.Class): Template = t.templ

    }

  implicit val traitHasTemplate: HasTemplate[Defn.Trait] =
    new HasTemplate[Defn.Trait] {

      override def template(t: Defn.Trait): Template = t.templ

    }

  final implicit class HasTemplateOps[T: HasTemplate](private val t: T) {

    def template: Template = HasTemplate.template(t)

  }

  final implicit class TemplateOps(private val t: Template) {

    def isEmpty: Boolean = t.inits.isEmpty

    def extendsType: String => Boolean = t.inits.map(_.tpe.syntax).contains
  }

}
