package com.oldtan.arithmetic

import java.io.{File, RandomAccessFile}
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

import com.oldtan.tools.YamlConfig
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.configuration.Configuration

class WriteFile extends RichMapFunction[Map[String, String], String]{

  val dir = new File(YamlConfig.load.writeFileDir)

  override def open(parameters: Configuration) = {
    super.open(parameters)
    Option(dir).filter(!_.isDirectory).foreach(_.mkdir)
  }

  override def map(data: Map[String, String]): String = {
    data.get("documentcode")
      .map(p => new File(s"${dir.getPath}/$p"))
      .map(d => {if(!d.isDirectory) d.mkdir;d})
      .map(d => (d, if(d.listFiles.length == 0) 0 else d.listFiles.length - 1))
      .map(d => (d._1, d._2, new File(s"${d._1.getPath}/${d._2}")))
      .map(t => {if(!t._3.exists) t._3.createNewFile; t})
      .map(t => {if(t._3.length > 134217728) new File(s"${t._1.getPath}/${t._2 + 1}") else t._3})
      .foreach(f => {
        val oFile = new RandomAccessFile(f.getPath, "rw")
        val oChannel = oFile.getChannel
        val text = s"${data.get("pkid").get} ${data.get("documentcode").get} ${data.get("documentdata").get}"
        val bytes = if(oFile.length > 0) s"\n$text".getBytes("UTF-8") else text.getBytes("UTF-8")
        val m = oChannel.map(FileChannel.MapMode.READ_WRITE, oFile.length, bytes.length)
        (m.limit - m.position - bytes.length) >= 0 match {
          case true => m put bytes
          case _ => {m.force;m.clear;m put bytes}
        }
        m.force; oChannel.close
    })
    data.get("pkid").get
  }
}
