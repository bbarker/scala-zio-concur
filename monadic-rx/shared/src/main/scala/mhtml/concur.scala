package mhtml

import zio.ZIO

object concur:
  type Widget[R, E, A] = ZIO[R, E, A] // TODO
  val x: Int = 5
