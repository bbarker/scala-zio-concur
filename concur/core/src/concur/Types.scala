package concur

import zio.*

object Types {

  /** | Callback returns the unused effect
   * Canceling will *always* have some leftover effect, else it would have ended already
   * TODO: Have a way to check if the callback is finished (i.e. will never be called again)
   * ne option is to have a cb = (Either partResult a -> Effect Unit)
   */
  type Callback[+V, +E,  E1 >: E, +A] = (Result[V, E, A] => IO[E1, Unit]) => IO[E, WidgetHandle[V, E, E1, A]]

  case class WidgetHandle[+V,+E, E1 >: E,+A](effCb: IO[E, Callback[V, E, E1, A]]) extends AnyVal

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

