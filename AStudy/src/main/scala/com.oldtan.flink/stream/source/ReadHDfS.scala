package com.oldtan.flink.stream.source

import org.apache.flink.api.java.io.TextInputFormat
import org.apache.flink.core.fs.Path
import org.apache.flink.streaming.api.functions.source.FileProcessingMode
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

object ReadHDfS extends App {
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  val filePath = "hdfs://node01:9000/flink/data/"
  val format = new TextInputFormat(new Path(filePath))
  try {
    val stream = env.readFile(format, filePath, FileProcessingMode.PROCESS_CONTINUOUSLY, 10)
    stream.print
  } catch {
    case ex: Exception => {
      ex.getMessage
    }
  }
  env.execute()

}
