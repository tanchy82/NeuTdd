package com.oldtan.arithmetic

import java.io.File

import com.oldtan.tools.{FileOperate, YamlConfig}
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration

class WriteFile extends RichMapFunction[Map[String, String], (String, String, String)]{

  val dir = new File(YamlConfig.load.writeFileDir)

  val maxByte = 134217728

  override def open(parameters: Configuration) = {
    super.open(parameters)
    Option(dir).filter(!_.isDirectory).foreach(_.mkdir)
  }

  override def map(data: Map[String, String]): (String, String, String) = {
    data.get("documentcode")
      .map(p => new File(s"${dir.getPath}/$p"))
      .map(d => {if(!d.isDirectory) d.mkdir;d})
      .map(d => (d, if(d.listFiles.length == 0) 0 else d.listFiles.length - 1))
      .map(d => (d._1, d._2, new File(s"${d._1.getPath}/${d._2}")))
      .map(t => {if(!t._3.exists) t._3.createNewFile; t})
      .map(t => {if(t._3.length > maxByte) new File(s"${t._1.getPath}/${t._2 + 1}") else t._3})
      .map(f => {
        val pkid = data.get("pkid").get
        val simhash = SimhashAlgorithm.simhashCode(data.get("documentdata").get)
        FileOperate.writeFile(f, s"$pkid $simhash\n")
        (pkid, data.get("documentcode").get, simhash)
      }).get
  }
}