package com.oldtan

import com.oldtan.arithmetic.WriteFile
import com.oldtan.source.RichSourceFromOracle
import com.oldtan.tools.YamlConfig
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object TextSimilarityCompareStreamJob extends App {

  val yamlConfig = YamlConfig.load
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  env.addSource(new RichSourceFromOracle).map(new WriteFile).print
  env.execute
}