package com.oldtan.flink.stream.source

import java.util.Properties

import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, KafkaDeserializationSchema}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringSerializer

object ReadKafka extends App {

  val env = StreamExecutionEnvironment.getExecutionEnvironment
  val props = new Properties
  props.setProperty("bootstrap.servers","localhost:9092")
  //props.setProperty("group.id","flink-kafka-0011")
  props.setProperty("key.deserializer",classOf[StringSerializer].getName)
  props.setProperty("value.deserializer",classOf[StringSerializer].getName)
  props.setProperty("auto.offset.reset","earliest") // latest

  val stream = env.addSource(new FlinkKafkaConsumer[(String, String)]("flink-kafka",
    new KafkaDeserializationSchema[(String,String)]{
      override def isEndOfStream(t: (String, String)): Boolean = false

      override def deserialize(consumerRecord: ConsumerRecord[Array[Byte], Array[Byte]]): (String, String) = {
        val key = new String(consumerRecord.key, "UTF-8")
        val value = new String(consumerRecord.value, "UTF-8")
        (key, value)
      }

      override def getProducedType: TypeInformation[(String, String)] = {
        createTuple2TypeInformation(createTypeInformation[String], createTypeInformation[String])
      }
    },props))
  stream.print
  env.execute
}