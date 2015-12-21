import scalariform.formatter.preferences._

lazy val shoushiling = (project in file("."))
  .settings(moduleName := "shoushiling")
  .settings(parentSettings)
  .aggregate(core, cli)
  .dependsOn(cli)

lazy val core = (project in file("core"))
  .settings(moduleName := "shoushiling-core")
  .settings(subProjectSettings)

lazy val cli = (project in file("cli"))
  .settings(moduleName := "shoushiling-cli")
  .settings(subProjectSettings)
  .dependsOn(core)

lazy val parentSettings = buildSettings ++ packageSettings ++ publishSettings

lazy val subProjectSettings =
  buildSettings ++ baseSettings ++ codeQualitySettings ++ scalariformSettings ++ noPublish

val projectVersion = "0.2.0"

lazy val buildSettings = Seq(
  version := projectVersion,
  organization := "com.pasviegas",
  scalaVersion := "2.11.7"
)

lazy val packageSettings = Seq(
  mainClass in assembly := Some("com.pasviegas.shoushiling.cli.Main"),
  assemblyJarName in assembly := s"shoushiling_2.11-$projectVersion-runnable.jar",
  test in assembly := {}
)

lazy val publishSettings = Seq(
  licenses +=("Unlicense", url("http://unlicense.org/")),
  publishMavenStyle := true
) ++ addArtifact(Artifact("shoushiling", "jar", "jar", "runnable"), assembly).settings

lazy val noPublish = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)


lazy val baseSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "2.2.5" % "test"
  )
)

lazy val codeQualitySettings = Seq(
  compileScalaStyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value,
  (compile in Compile) <<= (compile in Compile) dependsOn compileScalaStyle,
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
    .setPreference(DoubleIndentClassDeclaration, true)
)

lazy val compileScalaStyle = taskKey[Unit]("compileScalaStyle")

