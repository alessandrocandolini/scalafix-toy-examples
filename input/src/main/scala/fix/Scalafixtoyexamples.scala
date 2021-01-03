/*
rule = Scalafixtoyexamples
 */
package fix

import cats.data.Coproduct

object Scalafixtoyexamples {
  val x = 42
  val y = "42"
  val z: Double = 42

  def addOffset(a: Double, b: Double): Double = a + 42 * b

  def something[A, F[_], G[_]](implicit Coproduct: Coproduct[F, G, A]) = ???
  def something2[A, F[_], G[_]](implicit Coproduct: Coproduct[F, G, A]) = ???
}

object Test {
  trait Useless
  trait Useless2 extends Useless
  trait Useless3
  trait Useless4 extends Useless3 with Useless2

  sealed trait Colour1
  final case class Red1(opacity: Int) extends Colour1
  final case object Green1 extends Colour1

  sealed abstract class Colour2
  final case class Red2(opacity: Int) extends Colour2
  final case object Green2 extends Colour2

  sealed abstract class Colour3 extends Product with Serializable
  case class Red3(opacity: Int) extends Colour3
  final case object Green3 extends Colour3

  sealed abstract class Colour4 extends Product
  case class Red4(opacity: Int) extends Colour4
  final case object Green4 extends Colour4

  sealed abstract class Colour5 extends Serializable
  case class Red5(opacity: Int) extends Colour5
  final case object Green5 extends Colour5

  sealed abstract class Colour6 extends Serializable with Useless4
  case class Red6(opacity: Int) extends Colour6
  final case object Green6 extends Colour6

  sealed abstract class Colour7 extends Useless4 with Serializable
  case class Red7(opacity: Int) extends Colour7
  final case object Green7 extends Colour7

  sealed abstract class Colour8 extends Product with Useless4 with Serializable
  case class Red8(opacity: Int) extends Colour8
  final case object Green8 extends Colour8
}
