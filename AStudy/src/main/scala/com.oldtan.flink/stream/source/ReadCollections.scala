package com.oldtan.flink.stream.source

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
object ReadCollections extends App {
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  val stream:DataStream[Int] = env.fromElements(1,2,3,4,5)
  stream.print
  env.execute
}
