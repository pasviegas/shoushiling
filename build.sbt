import scalariform.formatter.preferences._

lazy val shoushiling = (project in file("."))
  .settings(moduleName := "shoushiling")
  .settings(buildSettings)
  .settings(coverageSettings)
  .aggregate(core, cli)

lazy val core = (project in file("core"))
  .settings(moduleName := "shoushiling-core")
  .settings(allSettings)

lazy val cli = (project in file("cli"))
  .settings(moduleName := "shoushiling-cli")
  .settings(allSettings)
  .dependsOn(core)

lazy val allSettings = buildSettings ++ baseSettings ++ codeQualitySettings ++ scalariformSettings ++ coverageSettings

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
  compileScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value,
  (compile in Compile) <<= (compile in Compile) dependsOn compileScalastyle,
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
    .setPreference(DoubleIndentClassDeclaration, true)
)

lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")

