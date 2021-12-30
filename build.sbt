

name := "NeuTdd"

version := "0.1"

scalaVersion := "2.12.12"

val flinkVersion = "1.13.3"

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.apache.flink" %% "flink-scala" % flinkVersion,
    "org.apache.flink" %% "flink-streaming-scala" % flinkVersion,
    "org.apache.flink" %% "flink-clients" % flinkVersion,
    "org.yaml" % "snakeyaml" % "1.29",
    "info.debatty" % "java-string-similarity" % "2.0.0",
    "com.oracle.database.jdbc" % "ojdbc8" % "12.2.0.1",

    "org.apache.flink" %% "flink-connector-kafka" % "1.13.3"
  )
)

lazy val largeScreenDisplay = (project in file("largeScreenDisplay")).settings(
  commonSettings,
  mainClass := Some("com.oldtan.LargeScreenDisplayStreamJob"),
  assembly / mainClass := Some("com.oldtan.LargeScreenDisplayStreamJob"),
  assembly / assemblyJarName := "LargeScreenDisplayStreamJob_1.0.jar"
)

lazy val textSimilarityCompare = (project in file("textSimilarityCompare")).settings(
  commonSettings,
  mainClass := Some("com.oldtan.TextSimilarityCompareStreamJob"),
  assembly / mainClass := Some("com.oldtan.TextSimilarityCompareStreamJob"),
  assembly / assemblyJarName := "TextSimilarityCompareStreamJob_1.0.jar"
)

lazy val AStudy = (project in file("AStudy")).settings(
  commonSettings,
  mainClass := Some("com.oldtan.flink.Study"),
  assembly / mainClass := Some("com.oldtan.flink.Study"),
  assembly / assemblyJarName := "AStudy_1.0.jar"
)

