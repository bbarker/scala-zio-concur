package concur

import zio.*

object Types:
  import Widget.*
  import WidgetHandle.*

  /** | Callback returns the unused effect
   * Canceling will *always* have some leftover effect, else it would have ended already
   * TODO: Have a way to check if the callback is finished (i.e. will never be called again)
   * one option is to have a cb = (Either partResult a -> Effect Unit)
   */
  type Callback[+V, +E,  E1 >: E, +A] = (Result[V, E, E1, A] => IO[E1, Unit]) => IO[E, WidgetHandle[V, E, E1, A]]
  object Callback:
    // TODO: I think I need to unpack this in purescript:
    // mapCallback :: forall v a b. (a -> b) -> Callback v a -> Callback v b
    // mapCallback f g = \cb -> map f <$> g (cb <<< map f)
    def map[V, E, E1 >: E, A, B](fn: A => B)(cb: Callback[V,E,E1,A]): Callback[V,E,E1,B] = resRunB => {
      // val resRunB0: ResultRun[V, E, E1, B] = resRunB
      val resRunA = Result.map[V,E,E1,A,B](fn).andThen(resRunB)
      val cbRunA: IO[E, WidgetHandle[V, E, E1, A]] = cb(resRunA)
      cbRunA.map(wh => WidgetHandle.map(fn)(wh))

    }
      // cb((res: Result[V,E,A]) => Result.map(fn, res))
  


  object WidgetHandle:
    opaque type WidgetHandle[+V,+E, E1 >: E,+A] = IO[E, Callback[V, E, E1, A]]
    def map[V,E,E1 >: E,A,B](fn: A => B)(wh: WidgetHandle[V,E,E1,A]): WidgetHandle[V,E,E1,B] 
      = wh.map(cb => Callback.map(fn)(cb))

  /**
   * An Array context with a hole
   */
  case class ZipList[A](left: List[A], right: List[A])
  object ZipList:
    def map[A,B](fn: A => B)(zl: ZipList[A]): ZipList[B] =
      ZipList(zl.left.map(fn), zl.right.map(fn))

  /**
   * A Widget is basically a callback that returns a view or a return value
   */
  enum Result[+V, +E, E1 >: E, +A]:
    case View(view: V)
    case Eff(eff: IO[E, Unit])
    case Res(result: A, remaining: Unit => ZipList[RemainingWidget[V,E,E1,A]])
  object Result:
    def map[V,E, E1 >: E,A,B](fn: A => B)(res: Result[V,E,E1,A]): Result[V,E,E1,B] = res match
      case View(v) => View(v)
      case Eff(e) => Eff(e)
      case Res(res, rem) => Res(fn(res), _ => ZipList.map(RemainingWidget.map[V,E,E1,A,B](fn))(rem(())) )
  
//  -- | A stopped Widget which is not populated, or a handle to an already running and populated widget
//    data RemainingWidget v a = RunningWidget (WidgetHandle v a) | StoppedWidget (Widget v a)
//  derive instance functorRemainingWidget :: Functor (RemainingWidget v)
  enum RemainingWidget[+V,+E,E1 >: E,+A]:
    case RunningWidget(wh: WidgetHandle[V,E,E1,A])
    case StoppedWidget(sw: Widget[V,E,E1,A])
  object RemainingWidget:
    def map[V,E,E1 >: E,A,B](fn: A => B)(rw: RemainingWidget[V,E,E1,A]): RemainingWidget[V,E,E1,B] = ??? // rw match
      // case RunningWidget(wh) => ??? // TODO: functor for WidgetHandle m

  object Widget:
    opaque type Widget[+V, +E,  E1 >: E, +A] = Callback[V,E,E1,A]


