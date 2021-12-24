package com.oldtan.arithmetic

import java.io.File

import com.oldtan.tools.{FileOperate, YamlConfig}
import org.apache.flink.api.common.functions.RichMapFunction

class TextSimilarity extends RichMapFunction[(String, String, String), Option[String]]{

  val dir = new File(YamlConfig.load.writeFileDir)

  override def map(data: (String, String, String)): Option[String] = {
    new File(s"${dir.getPath}/${data._2}")
    var flag = false
    var opt = Option.empty[String]
    FileOperate.readFile(new File(s"${dir.getPath}/${data._2}"), s => {
      val arr = s.split(" ")
      flag = SimhashAlgorithm.hammingDistance(data._3, arr(2))
      if(flag) opt = Option(s"${data._1}, ${arr(0)}")
      !flag
    })
    opt
  }
}