/*
rule = Scalafixtoyexamples
*/
package fix

import cats.data.Coproduct
object Scalafixtoyexamples {
  val x = 42

  def something[A, F[_], G[_]](implicit c: Coproduct[F,G,A]) = ???
}
