ThisBuild / version := "0.1.0"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaTetris"
  )

libraryDependencies ++= Seq(
  "org.jline" % "jline-terminal" % "3.27.0",
  "org.jline" % "jline-reader" % "3.27.0"
)

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", "services", _*) => MergeStrategy.concat
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}