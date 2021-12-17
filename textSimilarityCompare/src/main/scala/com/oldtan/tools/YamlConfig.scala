package com.oldtan.tools

import java.util

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

import scala.io.Source

object YamlConfig {

  var yaml:YamlConfig = null

  def load: YamlConfig = Option(yaml) match {
      case None => {
        yaml = new Yaml(new Constructor(classOf[YamlConfig])).load(Source.fromResource("application.yml").bufferedReader).asInstanceOf[YamlConfig]
        yaml
      }
      case _ => yaml
    }
}

import scala.beans.BeanProperty
class YamlConfig extends Serializable {
  @BeanProperty var dbConn: util.HashMap[String,String] = _
  @BeanProperty var startDate: String = _
  @BeanProperty var writeFileDir: String = _
  lazy val dbConn_url = dbConn.get("url")
  lazy val dbConn_name = dbConn.get("name")
  lazy val dbConn_password = dbConn.get("password")
}