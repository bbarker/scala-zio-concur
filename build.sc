import coursier.maven.MavenRepository

import mill._
import mill.api.Loose
import mill.define.Target
import mill.scalajslib._
import mill.scalalib._
import mill.scalalib.api.CompilationResult

trait BaseModule extends ScalaModule {
  def scalaVersion   = T("3.0.0")
  def scalaJSVersion = T("1.5.1")
  //def scalaNativeVersion = T()

  override def repositoriesTask =
    T.task {
      super.repositoriesTask() ++ Seq(
        MavenRepository(
          "https://s01.oss.sonatype.org/content/repositories/snapshots",
        ),
      )
    }
}

trait BaseCrossModule extends BaseModule {
  override def millSourcePath = super.millSourcePath / os.up
}

trait CrossModule extends Module { outer =>
  def moduleDeps: Seq[CrossModule] = Seq.empty
  object jvm extends BaseCrossModule with ScalaModule {
    override def moduleDeps = super.moduleDeps ++ outer.moduleDeps.map(_.jvm)
  }
  object js extends BaseCrossModule with ScalaJSModule {
    override def moduleDeps = super.moduleDeps ++ outer.moduleDeps.map(_.js)
  }
  //  object native extends BaseCrossModule with ScalaNativeModule {
  //    def moduleDeps = super.moduleDeps ++ outer.moduleDeps.map(_.native)
  //  }
}

//noinspection ScalaFileName
object concur extends Module {

  object core extends CrossModule {

    def ivyDeps: Target[Loose.Agg[Dep]] =
      Agg(
        ivy"dev.zio::zio::1.0.8",
      )

  }

  object frontend extends BaseModule with ScalaJSModule {

    override def moduleDeps = super.moduleDeps ++ Seq(core.jvm)

    override def ivyDeps: Target[Loose.Agg[Dep]] =
      T {
        super.ivyDeps() ++ Agg(
          ivy"org.scala-js::scalajs-dom::1.1.0".withDottyCompat(scalaVersion()),
        )
      }

  }

  object backend extends BaseModule {

    override def moduleDeps = super.moduleDeps ++ Seq(core.jvm)

    override def ivyDeps: Target[Loose.Agg[Dep]] =
      T {
        super.ivyDeps() ++ Agg(
          ivy"com.lihaoyi::scalatags:0.9.4".withDottyCompat(scalaVersion()),
          ivy"io.d11::zhttp:1.0.0.0-RC16+18-bfbb9858-SNAPSHOT",
        )
      }

    override def compile: T[CompilationResult] =
      T {
        frontend.fastOpt.apply()
        super.compile.apply()
      }

  }

}
