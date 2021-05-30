package concur

import zio._

object Types {

  /**
   * An Array context with a hole
   */
  case class ZipList[A](left: Array[A], right: Array[A])

  /**
   * A Widget is basically a callback that returns a view or a return value
   */
  enum Result[+V, +E, +A]:
    case View(view: V)
    case Eff(eff: IO[E, Unit])
    case Result(result: A, remaining: Unit => Any)
}

