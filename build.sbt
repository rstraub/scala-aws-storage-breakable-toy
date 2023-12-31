import sbt.Keys.libraryDependencies

ThisBuild / scalaVersion := "2.13.11"
ThisBuild / organization := "nl.codecraftr"
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / scalacOptions ++= Seq("-Wunused:imports")
ThisBuild / scalafixScalaBinaryVersion := "2.13"

val awsVersion = "2.20.68"

lazy val root = project
  .enablePlugins(ScalafmtPlugin)
  .in(file("."))
  .settings(
    name := "aws-storage-scala",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.16" % Test,
    libraryDependencies += "software.amazon.awssdk" % "dynamodb" % awsVersion,
    libraryDependencies += "software.amazon.awssdk" % "s3" % awsVersion,
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
    libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.7",
    libraryDependencies += "io.circe" %% "circe-parser" % "0.14.5",
    libraryDependencies += "io.circe" %% "circe-generic" % "0.14.5"
  )
