package concur

import zio._

object Types {
  type ZipList = Option[_] // ZipList (RemainingWidget v a)

  // A Widget is basically a callback that returns a view or a return value
  enum Result[+V, +E, +A]:
    case View(view: V)
    case Eff(eff: IO[E, Unit])
    case Result(result: A, remaining: Unit => Any)
}
