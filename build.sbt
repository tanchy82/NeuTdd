

name := "NeuTdd"

version := "0.1"

scalaVersion := "2.11.11"

val flinkVersion = "1.13.0"

val log4jVersion = "2.12.1"

lazy val alinkFlinkScalaVersion = "1.13_2.11"

lazy val alinkVersion = "1.4.0"

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "org.apache.flink" %% "flink-scala" % flinkVersion,
    "org.apache.flink" %% "flink-streaming-scala" % flinkVersion,
    "org.apache.flink" %% "flink-clients" % flinkVersion,
    "org.apache.logging.log4j" % "log4j-core" % log4jVersion % Runtime,
    "org.apache.logging.log4j" % "log4j-api" % log4jVersion % Runtime,
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion % Runtime,
    "info.debatty" % "java-string-similarity" % "2.0.0",
    "com.oracle.database.jdbc" % "ojdbc8" % "12.2.0.1",
    "org.yaml" % "snakeyaml" % "1.29"
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

