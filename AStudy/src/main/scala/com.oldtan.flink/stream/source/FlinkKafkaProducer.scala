package com.oldtan.flink.stream.source

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

import scala.io.Source

object FlinkKafkaProducer extends App {
  val pro = new Properties
  pro.setProperty("bootstrap.servers", "localhost:9092")
  pro.setProperty("key.serializer", classOf[StringSerializer].getName)
  pro.setProperty("value.serializer", classOf[StringSerializer].getName)

  val producer = new KafkaProducer[String,String](pro)

  var iterator =  Source.fromFile("/home/tanchy/software/IdeaProjects/NeuTdd/AStudy/src/main/resources/data/data_carFlow_all_column_test.txt").getLines
  for (i <- 1 to 100) {
    for (elem <- iterator) {
      println(elem)
      val splits = elem.split(",")
      val monitorId = splits(0).replace("'","")
      val carId = splits(2).replace("'","")
      val timestamp = splits(4).replace("'","")
      val speed = splits(6)
      val builder = new StringBuilder
      val info = builder.append(monitorId + "\t").append(carId + "\t").append(timestamp + "\t").append(speed)
      producer.send(new ProducerRecord[String,String]("flink-kafka",i+"",info.toString))
    }
  }


}
