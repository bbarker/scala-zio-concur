import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

Global / onChangedBuildSource := ReloadOnSourceChanges

inThisBuild(List(
  crossScalaVersions := Seq("3.0.0"),
  scalaVersion := crossScalaVersions.value.head,
  scalacOptions := Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-Xfatal-warnings", // see Cancelable#cancel
  ),
  organization := "in.nvilla",
  scalaJSLinkerConfig ~= { _.withSourceMap(true) },
  licenses := Seq(("MIT", url("http://opensource.org/licenses/MPL-2.0"))),
  homepage := Some(url("https://github.com/bbarker/scala-zio-concur")),
  developers := List(
    Developer(
      "bbarker",
      "Brandon Barker",
      "brandon.barker@gmail.com",
      url("https://github.com/bbarker/")
    )
  )))

val scalajsdom = "1.1.0"

publish / skip := true

//lazy val `monadic-html` = project
//  .enablePlugins(ScalaJSPlugin)
//  .dependsOn(
//    `concurJS`,
//    `concur-catsJS` % "test->compile")
//  .settings(
//    testSettings,
//    libraryDependencies += "org.scala-js"  %%% "scalajs-dom" % scalajsdom,

lazy val `concurJS`  = `concur`.js
lazy val `concurJVM` = `concur`.jvm
lazy val `concur`    = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Full)
  .jvmSettings(publish / skip := true)
  .jsSettings(testSettings)
  .settings(
    libraryDependencies += "dev.zio" %%% "zio" % "1.0.8")

//lazy val `concur-catsJS`  = `concur-cats`.js
//lazy val `concur-catsJVM` = `concur-cats`.jvm
//lazy val `concur-cats`    = crossProject(JSPlatform, JVMPlatform)
//  .crossType(CrossType.Pure)
//  .jvmSettings(skip in publish := true)
//  .dependsOn(`concur`)
//  .settings(
//    testSettings,
//    libraryDependencies += "org.typelevel" %%% "cats-concur" % cats)

lazy val `examples` = project
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(`concurJS`)
  //.dependsOn(/*`monadic-html`, `concur-catsJS`*/)
  .settings(
    testSettings,
    publish / skip := true,
    Test / test := {},
    // libraryDependencies += "com.github.japgolly.scalacss" %%% "core" % "0.6.1"
  )


lazy val testSettings = Seq(
  // Test / testOptions   += Tests.Argument(TestFrameworks.ScalaTest, "-oDF"),
  Test / jsEnv     := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv())
