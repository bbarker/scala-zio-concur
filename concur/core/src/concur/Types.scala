package concur

import zio.*

object Types {

  /** | Callback returns the unused effect
   * Canceling will *always* have some leftover effect, else it would have ended already
   * TODO: Have a way to check if the callback is finished (i.e. will never be called again)
   * one option is to have a cb = (Either partResult a -> Effect Unit)
   */
  type Callback[+V, +E,  E1 >: E, +A] = (Result[V, E, E1, A] => IO[E1, Unit]) => IO[E, WidgetHandle[V, E, E1, A]]
  object Callback{
    // mapCallback :: forall v a b. (a -> b) -> Callback v a -> Callback v b
    // mapCallback f g = \cb -> map f <$> g (cb <<< map f)
//    def map[A, B, V, E, E1](fn: A => B)(cb: Callback[V,E,E1,A]): Callback[V,E,E1,B] = cb1 => {
//
//    }
      // cb((res: Result[V,E,A]) => Result.map(fn, res))
  }



  case class WidgetHandle[+V,+E, E1 >: E,+A](effCb: IO[E, Callback[V, E, E1, A]]) extends AnyVal

  /**
   * An Array context with a hole
   */
  case class ZipList[A](left: List[A], right: List[A])
  object ZipList {
    def map[A,B](fn: A => B)(zl: ZipList[A]): ZipList[B] =
      ZipList(zl.left.map(fn), zl.right.map(fn))
  }

  /**
   * A Widget is basically a callback that returns a view or a return value
   */
  enum Result[+V, +E, E1 >: E, +A]:
    case View(view: V)
    case Eff(eff: IO[E, Unit])
    case Res(result: A, remaining: Unit => ZipList[RemainingWidget[V,E,E1,A]])
  object Result {
    def map[A,B,V,E](fn: A => B)(res: Result[V,E,E,A]): Result[V,E,E,B] = res match
      case View(v) => View(v)
      case Eff(e) => Eff(e)
      case Res(res, rem) => Res(fn(res), _ => ZipList.map(RemainingWidget.map[V,E,A,B](fn))(rem(())) )
  }
  type ResultRun[V,E,A] = Result[V, E, E, A] => IO[E, Unit]
  object ResultRun {
    // f: A -> C
    // fn: B -> A
    // fn andThen f : B -> C
    // https://stackoverflow.com/questions/67779855/how-to-map-over-functions-input-in-scala/67784194#67784194
    def map[V,E,A,B](fn: B => A)(rr: ResultRun[V,E,A]): ResultRun[V,E,B] = Result.map(fn).andThen(rr)
  }

//  -- | A stopped Widget which is not populated, or a handle to an already running and populated widget
//    data RemainingWidget v a = RunningWidget (WidgetHandle v a) | StoppedWidget (Widget v a)
//  derive instance functorRemainingWidget :: Functor (RemainingWidget v)
  enum RemainingWidget[+V,+E,E1 >: E,+A]:
    case RunningWidget(wh: WidgetHandle[V,E,E1,A])
    case StoppedWidget(sw: Widget[V,E,E1,A])
  object RemainingWidget {
    def map[V,E,A,B](fn: A => B)(rw: RemainingWidget[V,E,E,A]): RemainingWidget[V,E,E,B] = ??? // rw match
      // case RunningWidget(wh) => ??? // TODO: functor for WidgetHandle m

  }

  case class Widget[+V, +E,  E1 >: E, +A](cb: Callback[V,E,E1,A]) extends AnyVal

}
