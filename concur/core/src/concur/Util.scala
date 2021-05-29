
// Something from Miles Sabin: https://twitter.com/milessabin/status/1398308204598206468

type Apply[F[_], A] = F[A]

type OptArg[FT] = FT match {
  case Apply[f, t] => f[Option[t]]
}

type LOI = OptArg[List[Int]]
