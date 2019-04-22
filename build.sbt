import Dependencies._

lazy val networkAnalyser =
  Project("network-analyser", file("."))
    .enablePlugins(BuildInfoPlugin, AssemblyPlugin)
    .settings(
      organization := "dev.peproll",
      name := "network-analyser",
      version := "0.0-SNAPSHOT",
      scalaVersion := "2.12.8",
      scalacOptions ++= Seq(
        "-deprecation",
        "-feature",
        "-unchecked",
        "-Xlint:-unused,_",
        "-Xfatal-warnings",
        "-language:higherKinds",
        "-Yno-adapted-args",
        "-Ywarn-value-discard"
      ),
      libraryDependencies ++= dependencies,
      buildInfoKeys := Seq(name),
      buildInfoPackage := "dev.peproll.na",
      mainClass in assembly := Some("dev.peproll.na.Main"),
      assemblyJarName in assembly := { s"${name.value}-${version.value}.jar"},
      assemblyMergeStrategy in assembly := {
        case PathList("META-INF", xs @ _*) => MergeStrategy.discard
        case PathList("reference.conf") => MergeStrategy.concat
        case _ => MergeStrategy.first
      }
)