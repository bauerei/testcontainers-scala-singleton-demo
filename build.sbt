lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.13.6"
    )),
    name := "testcontainers-scala-singleton-demo",
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.4.5" % Test,
      "io.lettuce" % "lettuce-core" % "6.2.3.RELEASE" % Test,
      "org.scalatest" %% "scalatest" % "3.2.15" % Test,
      "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.40.12" % Test,
    )
  )

Test / fork := true