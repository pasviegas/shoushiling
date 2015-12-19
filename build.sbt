import scalariform.formatter.preferences._

lazy val shoushiling = (project in file("."))
  .settings(moduleName := "shoushiling")
  .settings(parentSettings)
  .aggregate(core, cli)

lazy val core = (project in file("core"))
  .settings(moduleName := "shoushiling-core")
  .settings(subProjectSettings)

lazy val cli = (project in file("cli"))
  .settings(moduleName := "shoushiling-cli")
  .settings(subProjectSettings)
  .dependsOn(core)

lazy val parentSettings =
  buildSettings ++ coverageSettings

lazy val subProjectSettings =
  buildSettings ++ baseSettings ++ codeQualitySettings ++ scalariformSettings ++ coverageSettings

lazy val buildSettings = Seq(
  version := "1.0",
  organization := "com.pasviegas",
  scalaVersion := "2.11.7"
)

lazy val baseSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "2.2.5" % "test"
  )
)

lazy val coverageSettings = Seq(
  coverageEnabled := true
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

