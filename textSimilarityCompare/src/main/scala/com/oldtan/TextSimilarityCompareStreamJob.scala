package com.oldtan

import com.oldtan.arithmetic.{TextSimilarityArithmetic, WriteFile}
import com.oldtan.source.RichSourceFromOracle
import com.oldtan.tools.YamlConfig
import info.debatty.java.stringsimilarity.NGram
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object TextSimilarityCompareStreamJob extends App {

  val yamlConfig = YamlConfig.load
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  env.addSource(new RichSourceFromOracle).map(new WriteFile).map(new TextSimilarityArithmetic).print
  env.execute
/*  val s1 = "可以afjalfj但是街坊邻里"
  val s2 = "可以afjalfj但是街坊邻里"
  val ngram = new NGram(8)
  println(ngram.distance(s1, s2))
  println(if ((ngram.distance(s1, s2)) < 0.3) true else false)*/
}