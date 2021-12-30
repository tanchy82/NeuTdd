package com.oldtan.flink.stream.source

import org.apache.flink.streaming.api.functions.source.{ParallelSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

import scala.util.Random

object CustomSourceStandalone extends App {

  val env = StreamExecutionEnvironment.getExecutionEnvironment

  /** ParallelSourceFunction & SourceFunction */
  val stream = env.addSource(new ParallelSourceFunction[String] {
    var flag = true
    override def run(ctx: SourceFunction.SourceContext[String]): Unit = {
      val random = new Random
      while (flag) {
        ctx.collect("hello" + random.nextInt(1000))
      }
    }
    override def cancel(): Unit = {
      flag = false
    }
  }).setParallelism(4)
  stream.print
  env.execute
}