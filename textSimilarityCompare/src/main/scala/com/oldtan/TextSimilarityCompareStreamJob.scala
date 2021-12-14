package com.oldtan

import com.oldtan.source.RichSourceFromOracle
import com.oldtan.tools.{YamlConfig}
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
object TextSimilarityCompareStreamJob extends App {

  /*

  val env = StreamExecutionEnvironment.getExecutionEnvironment

  val compareStr = "Adobe CreativeSuite 5 Master Collection from cheap"

  val dataStream = env.fromElements("AdobeDCreativeSuite 5 Master Collection from cheap 4zp",
        "Adobe 可以CreativeSuite 5 Master Collection from cheap 4啊p1")
  val ngram = new NGram(8)

  dataStream.map(s => (ngram.distance(s, compareStr),s)).filter(s => s._1 > 0.1).addSink(s => println)

  env.execute
  val sql = """SELECT pkid,documentcode,itemtitle,itemvalue FROM emr_common_struct where length(itemvalue) > 32 and rownum > 0 and rownum <= 1000"""
  val conn = OracleOperation.openConnection
  val records = conn.executeQuerySql(sql)()
  conn.closeConnection
  println(records.size)
  */

  val yamlConfig = YamlConfig.load
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  env.addSource(new RichSourceFromOracle).print



}