package com.oldtan

import com.oldtan.source.RichSourceFromOracle
import com.oldtan.tools.YamlConfig
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

object TextSimilarityCompareStreamJob extends App {

  val yamlConfig = YamlConfig.load
  val env = StreamExecutionEnvironment.getExecutionEnvironment
  env.addSource(new RichSourceFromOracle).print
  env.execute

  /*val yamlConfig = YamlConfig.load
  val dbOperation = OracleOperation.openConnection
  val countSql = """select count(*) as count from emr_common_struct where length(itemvalue) > 32"""
  val recordSql = """SELECT pkid,documentcode,itemtitle,itemvalue FROM emr_common_struct where length(itemvalue) > 32 and rownum > ? and rownum <= ?"""

  val countRecord = dbOperation.executeQuerySql(countSql)()
  val count = countRecord.head.get("count") match {
    case Some(v) => v.toInt
    case _ => 2000
  }
  var rowNum = 0
  dbOperation.executeQuerySql(recordSql)(rowNum, rowNum + 10).foreach(println(_))
  dbOperation.closeConnection*/
}