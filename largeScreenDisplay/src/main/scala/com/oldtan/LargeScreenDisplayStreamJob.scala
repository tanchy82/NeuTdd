package com.oldtan

import java.util
import java.util.Properties
import java.util.concurrent.atomic.AtomicInteger

import scala.collection.mutable

object LargeScreenDisplayStreamJob extends App {
  /**
    * val env = StreamExecutionEnvironment.getExecutionEnvironment
    * val properties = new Properties
    *properties.setProperty("bootstrap.servers", "192.168.127.152:9092,192.168.127.42:9092:192.168.127.113:9092")
    *properties.setProperty("group.id", "myflink")
    *properties.setProperty("flink.partition-discovery.interval-millis","100")
    *properties.put("enable.auto.commit", "false")
    *properties.put("auto.offset.reset", "earliest")
    *properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    *properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    **
    *val topicList = new util.ArrayList[String]
    * topicList add "push"
    * topicList add "SUBSCRIBE"
    * val kafkaConsumer:FlinkKafkaConsumer[ObjectNode] =
    * new FlinkKafkaConsumer[ObjectNode](topicList, new JSONKeyValueDeserializationSchema(false), properties)
    *kafkaConsumer.setStartFromEarliest
    **
    *env.addSource(kafkaConsumer).addSink(new RichSinkFunction[ObjectNode]{
    * val count = new AtomicInteger
    * val subscriberCount = new AtomicInteger
    * val resultMap = new mutable.HashMap[String, DistrictResult]
    * val registerMap = new mutable.HashMap[String, Register]
    * var startTime = ""
    **
    *override def invoke(d: ObjectNode) = {
    * val v = d.get("value")
    * if (count.get == 0){
    * startTime = v.get("createTime").asText("")
    * }
    * val currentTime = v.get("createTime").asText("")
    * if(v.has("monitorSite")){
    * val sR = Subscribe(v.get("certificateNo").asText(""), v.get("district").asText(""))
    * Option(sR.district).map(s => resultMap.getOrElseUpdate(s, DistrictResult(s))).foreach(dr => {
    *dr.subscriberNum.incrementAndGet
    *subscriberCount.incrementAndGet
    * })
    * }else {
    * val nR = Register(v.get("certificateNo").asText(""), v.get("district").asText(""),
    * if (v.get("question1").asText("") == "是") '1' else '0',
    * if (v.get("question2").asText("") == "是") '1' else '0',
    * if (v.get("question3").asText("") == "是") '1' else '0')
    **
 *registerMap.get(nR.id).foreach(oR => {
    *resultMap.get(oR.district).foreach(o =>{
    *o.registerNum.decrementAndGet
    * Option(oR.question1).filter(_ == '1').foreach(_ => o.question1.decrementAndGet)
    * Option(oR.question2).filter(_ == '1').foreach(_ => o.question2.decrementAndGet)
    * Option(oR.question3).filter(_ == '1').foreach(_ => o.question3.decrementAndGet)
    *count.decrementAndGet
    * })
    * })
    * Option(nR.district).map(s => resultMap.getOrElseUpdate(s, DistrictResult(s))).foreach(dr => {
    *dr.registerNum.incrementAndGet
    * Option(nR.question1).filter(_ == '1').foreach(_ => dr.question1.incrementAndGet)
    * Option(nR.question2).filter(_ == '1').foreach(_ => dr.question2.incrementAndGet)
    * Option(nR.question3).filter(_ == '1').foreach(_ => dr.question3.incrementAndGet)
    *registerMap.put(nR.id, nR)
    *count.incrementAndGet
    * })
    * }
    **
  *val build = new StringBuilder(2048,s"""{"registerNum":${count.get},"subscriberNum":${subscriberCount.get},"startTime":${startTime},"currentTime":${currentTime},"districts":[""")
    *resultMap.toList.sortBy(-_._2.registerNum.get).foreach(v => build.++=(s"${v._2.toString},"))
    * println(build.deleteCharAt(build.length -1).++=("]}"))
    * }
    * })
    *env.execute("Flink streaming handle kafka data task start ...")
    * }
    * case class DistrictResult(district:String, var registerNum: AtomicInteger = new AtomicInteger,
    * var question1: AtomicInteger = new AtomicInteger,
    * var question2: AtomicInteger = new AtomicInteger,
    * var question3: AtomicInteger = new AtomicInteger,
    * var subscriberNum: AtomicInteger = new AtomicInteger) {
    * override def toString: String = {
    * s"""{"district":"$district","registerNum":$registerNum,"question1":$question1,"question2":$question2,"question3":$question3,"subscriber":$subscriberNum}"""
    * }
    * }
    **
  *case class Register(id:String, district:String, question1: Byte, question2: Byte, question3: Byte)
    * case class Subscribe(certificateNo: String, district:String)
    **/
}
