package com.oldtan.source

import com.oldtan.tools.OracleOperation
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}

import scala.collection.mutable

class RichSourceFromOracle extends RichSourceFunction[List[Map[String, String]]] {

  var dbOperation: OracleOperation = _

  override def open(parameters: Configuration) = {
    super.open(parameters)
    dbOperation = OracleOperation.openConnection
  }

  override def run(ctx: SourceFunction.SourceContext[List[Map[String, String]]]) = {
    val records = mutable.ListBuffer.empty[Map[String, String]]
    val countSql = """select count(*) as count from emr_common_struct where length(itemvalue) > 32"""
    val recordSql = """SELECT pkid,documentcode,itemtitle,itemvalue FROM emr_common_struct where length(itemvalue) > 32 and rownum > ? and rownum <= ?"""
    val countRecord = dbOperation.executeQuerySql(countSql)()
    val count = dbOperation.executeQuerySql(countSql)().head.get("count").get.toInt
    var rowNum = 0
    while (rowNum < count){
      records ++= dbOperation.executeQuerySql(recordSql)(rowNum, rowNum + 1000)
      ctx collect records.toList
    }
  }

  override def cancel() = dbOperation.closeConnection

  override def close() = dbOperation.closeConnection

}