package com.oldtan.tools

import java.util

import com.typesafe.scalalogging.LazyLogging
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

import scala.io.Source

object YamlConfig extends LazyLogging{
  def load: YamlConfig = new Yaml(new Constructor(classOf[YamlConfig]))
    .load(Source.fromResource("application.yml").bufferedReader).asInstanceOf[YamlConfig]
  logger.info("Load application.yml file sucessful.")
}

import scala.beans.BeanProperty
class YamlConfig extends Serializable {
  @BeanProperty var dbConn: util.HashMap[String,String] = _
  lazy val dbConn_url = dbConn.get("url")
  lazy val dbConn_name = dbConn.get("name")
  lazy val dbConn_password = dbConn.get("password")
}