/*
rule = Scalafixtoyexamples
 */
package fix

import cats.data.Coproduct
object Scalafixtoyexamples {
  val x = 42

  def something[A, F[_], G[_]](implicit c: Coproduct[F, G, A]) = ???
}

object Test {
  sealed trait Colour1
  final case class Red1(opacity: Int) extends Colour1
  final case object Green1 extends Colour1

  sealed abstract class Colour2
  final case class Red2(opacity: Int) extends Colour2
  final case object Green2 extends Colour2

  sealed abstract class Colour3 extends Product with Serializable
  final case class Red3(opacity: Int) extends Colour3
  final case object Green3 extends Colour3

  sealed abstract class Colour4 extends Product
  final case class Red4(opacity: Int) extends Colour4
  final case object Green4 extends Colour4

  sealed abstract class Colour5 extends Serializable
  final case class Red5(opacity: Int) extends Colour5
  final case object Green5 extends Colour5
}
