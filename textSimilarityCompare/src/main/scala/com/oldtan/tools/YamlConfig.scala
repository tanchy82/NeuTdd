package com.oldtan.tools

import java.util

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

import scala.io.Source

object YamlConfig {

  def load: YamlConfig = new Yaml(new Constructor(classOf[YamlConfig]))
    //.load(Source.fromFile("/home/tanchy/software/IdeaProjects/NeuTdd/textSimilarityCompare/src/main/resource/application.yml").bufferedReader).asInstanceOf[YamlConfig]
    .load(Source.fromFile("/home/flink-1.13.3/mytask/conf/application.yml").bufferedReader).asInstanceOf[YamlConfig]
}

import scala.beans.BeanProperty
class YamlConfig extends Serializable {
  @BeanProperty var dbConn: util.HashMap[String,String] = _
  lazy val dbConn_url = dbConn.get("url")
  lazy val dbConn_name = dbConn.get("name")
  lazy val dbConn_password = dbConn.get("password")
}