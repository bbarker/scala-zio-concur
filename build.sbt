import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

Global / semanticdbEnabled := true
Global / onChangedBuildSource := ReloadOnSourceChanges
Global / resolvers += Resolver.sonatypeRepo("snapshots")

inThisBuild(List(
  crossScalaVersions := Seq("3.1.0"),
  scalaVersion := crossScalaVersions.value.head,
  scalacOptions := Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    // "-Xfatal-warnings", see Cancelable#cancel
    "-Xlint:-unused,_",
    "-language:higherKinds",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard"),
  organization := "in.nvilla",
  scalaJSLinkerConfig ~= { _.withSourceMap(true) },
  licenses := Seq(("MIT", url("http://opensource.org/licenses/MIT"))),
  homepage := Some(url("https://github.com/OlivierBlanvillain/monadic-html")),
  developers := List(
    Developer(
    "OlivierBlanvillain",
    "Olivier Blanvillain",
    "noreply@github.com",
    url("https://github.com/OlivierBlanvillain/")
  )
)))

val cats       = "2.6.1"
val scalajsdom = "2.0.0"
// val scalatags  = "0.8.2"
val zio = "2.0.0-M4"
val zioPrelude = "1.0.0-RC7+11-3a2eb33a-SNAPSHOT"

publish / skip := true

lazy val `monadic-rxJS`  = `monadic-rx`.js
lazy val `monadic-rxJVM` = `monadic-rx`.jvm
lazy val `monadic-rx`    = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .jvmSettings(publish / skip := true)
  .jsSettings(testSettings)
  .settings(libraryDependencies ++= Seq(
    "io.github.ciaraobrien" %% "dottytags" % "1.1.0",
    // "com.lihaoyi" %%% "scalatags" % "0.8.2", // Doesn't work on Dotty, using DottyTags
     /* "org.scalatest" %%% "scalatest" % scalatest % Test*/
    "dev.zio" %%% "zio" % zio,
    "dev.zio" %%% "zio-prelude" % zioPrelude,
    )
  )


/* // examples disabled for now as scalacss isn't supported, can probably remove it
lazy val `examples` = project
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(`monadic-html`, `monadic-rx-catsJS`)
  .settings(
    testSettings,
    publish / skip := true,
    Test / test := {},
    libraryDependencies += "com.github.japgolly.scalacss" %%% "core" % "0.7.0")
*/

lazy val testSettings = Seq(
  //testOptions  in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF"),
  Test / jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv())
