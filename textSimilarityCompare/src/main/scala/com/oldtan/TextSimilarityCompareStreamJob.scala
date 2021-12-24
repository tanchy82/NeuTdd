package com.oldtan

import com.oldtan.arithmetic.SimhashAlgorithm
import com.oldtan.operation.TextSimilarityRichMap
import com.oldtan.source.RichSourceFromOracle
import com.oldtan.tools.YamlConfig
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object TextSimilarityCompareStreamJob extends App {

//  val text = "我希望是希望张晚霞的背影被晚霞映红"
  /*val text = "我是张晚霞的太阳被晚霞红光万丈"
  val b =    "我非常希望是希望张晚霞的背影被晚霞映红"
  val s1 = SimhashAlgorithm.simhashCode(text)
  println(s1)
  val s2 = SimhashAlgorithm.simhashCode(b)
  println(s2)
  println(SimhashAlgorithm.hammingDistance(s1,s2))

  println(NLPTokenizer.analyze(text).toStringWithoutLabels)
  println(NLPTokenizer.analyze(b).toStringWithoutLabels)*/



  val yamlConfig = YamlConfig.load
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  env.addSource(new RichSourceFromOracle)
    .map(t => (t.pkId, t.code, SimhashAlgorithm.simhashCode(t.document)))
    .keyBy(_._2)//.disableChaining.setParallelism(4)
    .map(new TextSimilarityRichMap)
    .addSink(s => s match {
      case Some(s) => print(s)
      case _ =>
    })
  env.execute
}