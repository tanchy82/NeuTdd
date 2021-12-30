package com.oldtan.flink.stream.source

import java.util.Properties

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.kafka.common.serialization.StringSerializer

object ReadKafkaNoKey extends App {
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  val props = new Properties
  props.setProperty("bootstrap.servers","localhost:9092")
  //props.setProperty("group.id","flink-kafka-001")
  props.setProperty("key.deserializer",classOf[StringSerializer].getName)
  props.setProperty("value.deserializer",classOf[StringSerializer].getName)
  props.setProperty("auto.offset.reset","earliest") // latest
  val stream = env.addSource(new FlinkKafkaConsumer[String]("flink-kafka", new SimpleStringSchema, props))
  stream.print
  env.execute
}