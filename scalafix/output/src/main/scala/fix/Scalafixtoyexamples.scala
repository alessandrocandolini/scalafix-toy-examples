package fix

import cats.data.EitherK

object Scalafixtoyexamples {
  val x = 43
  
  def something[A, F[_], G[_]](implicit c: EitherK[F,G,A]) = ???
}
