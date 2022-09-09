ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .enablePlugins(SbtTwirl)
  .settings(
    name := "untitled",
    libraryDependencies += "com.typesafe.play" %% "twirl-api" % "1.5.1"

  )
