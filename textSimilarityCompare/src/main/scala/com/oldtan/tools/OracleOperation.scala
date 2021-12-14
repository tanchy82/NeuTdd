package com.oldtan.tools

import java.sql.{Connection, DriverManager, ResultSet}

import com.typesafe.scalalogging.LazyLogging

import scala.collection.mutable

/**
  * Oracle Sql Operation Common Tools
  */
object OracleOperation extends LazyLogging {

  private var conn: Option[Connection] = None

  @throws("Due to the Oracle database connect error!")
  def openConnection: OracleOperation = {
    val yamlConfig = YamlConfig.load
    val DB_URL = yamlConfig.dbConn_url
    val USER = yamlConfig.dbConn_name
    val PASS = yamlConfig.dbConn_password
    Class forName "oracle.jdbc.driver.OracleDriver"
    conn = Option(DriverManager.getConnection(DB_URL, USER, PASS))
    logger.info("Get database connection sucessful.")
    OracleOperation(conn)
  }
}
case class OracleOperation(conn: Option[Connection]) extends LazyLogging{

  @throws("Due to the Oracle database execute sql error!")
  def executeQuerySql(sql: String)(implicit objs: AnyVal *): List[Map[String, String]] = {
    val psFun = (con: Connection) => con prepareStatement sql
    val ps = psFun(conn.get)
    (0 until objs.size).foreach(i => ps.setObject(i+1, objs(i)))
    val rSet = ps.executeQuery
    val metaCols = rSet.getMetaData
    val records = mutable.ListBuffer.empty[Map[String, String]]
    new Iterator[ResultSet] {
      def hasNext = rSet next
      def next = rSet
    }.toStream.foreach(r => {
      records += (1 to r.getMetaData.getColumnCount).toIterator.map(i => (r.getMetaData.getColumnName(i), r getString i)).toMap
    })
    ps.close
    records.toList
  }

  @throws("Due to the Oracle database close error!")
  def closeConnection = {
    conn.foreach(_ close)
  }
}