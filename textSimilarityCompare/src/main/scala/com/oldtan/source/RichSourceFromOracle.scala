package com.oldtan.source

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

import com.oldtan.tools.{OracleOperation, YamlConfig}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}

class RichSourceFromOracle extends RichSourceFunction[TextInstance] {

  var dbOperation: OracleOperation = _

  var yamlConfig: YamlConfig = _

  override def open(parameters: Configuration) = {
    yamlConfig = YamlConfig.load
    super.open(parameters)
    dbOperation = OracleOperation.openConnection
  }

  override def run(ctx: SourceFunction.SourceContext[TextInstance]) = {
    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val sd = LocalDate.parse(yamlConfig.startDate)
    val ed = if(yamlConfig.endDate.equalsIgnoreCase("now")) LocalDate.now else LocalDate.parse(yamlConfig.endDate)
    val sql = """select V.PKID as PKID,V.DOCUMENTCODE as DOCUMENTCODE,V.DOCUMENTDATA as DOCUMENTDATA from V_EMR_DOCUMENTDATA V
         where V.BUSINESSTIME BETWEEN to_date(?,'YYYY-MM-DD') AND to_date(?,'YYYY-MM-DD')"""
    (0 to (ChronoUnit.DAYS.between(sd, ed).toInt, 1)).foreach(d => {
      dbOperation.executeQuerySql(sql)(sd.plusDays(d).format(dateFormat), sd.plusDays(d+1).format(dateFormat))
        .foreach(m => ctx collect TextInstance(m.get("PKID").get,m.get("DOCUMENTCODE").get,m.get("DOCUMENTDATA").get))
      println(sd.plusDays(d).format(dateFormat))
    })
  }

  override def cancel() = dbOperation.closeConnection

  override def close() = dbOperation.closeConnection

}
case class TextInstance(pkId:String, code:String, document:String)