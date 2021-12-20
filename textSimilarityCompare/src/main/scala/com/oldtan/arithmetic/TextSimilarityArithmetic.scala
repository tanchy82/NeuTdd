package com.oldtan.arithmetic

import java.io.{File, RandomAccessFile}
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets

import com.oldtan.tools.YamlConfig
import info.debatty.java.stringsimilarity.NGram
import org.apache.flink.api.common.functions.RichMapFunction

import scala.collection.mutable

class TextSimilarityArithmetic extends RichMapFunction[Map[String, String], Option[String]]{

  /** Compare two string return true is Similarity */
  def compare(s1: String, s2: String): Boolean ={
    val ngram = new NGram(8)
    ngram.distance(s1, s2)
    if ((ngram.distance(s1, s2)) < 0.3) true else false
  }

  val dir = new File(YamlConfig.load.writeFileDir)

  override def map(data: Map[String, String]): Option[String] = {
    var opt = Option.empty[String]
    data.get("documentcode")
      .map(p => new File(s"${dir.getPath}/$p")).filter(_.isDirectory)
      .map(d => d.listFiles).foreach(arr => {
      arr.foreach(f => {
        val iFile = new RandomAccessFile(f.getPath, "r")
        val iChannel = iFile.getChannel
        val m = iChannel.map(FileChannel.MapMode.READ_ONLY, 0, iChannel.size)
        val dBuffer = StandardCharsets.UTF_8.newDecoder.decode(m)
        val buffer = mutable.StringBuilder.newBuilder
        try {
          for (i <- 0 until dBuffer.limit if opt.isEmpty){
            dBuffer.get(i) match {
              case '\n' => {
                val arr = buffer.toString.split(" ")
                (0 until 1).filter(arr(_) != data.get("pkid").get)
                  .map(_ => (data.get("documentdata").get, arr(2)))
                  .foreach(t => if(compare(t._1, t._2)) opt = Option(s"${(data.get("pkid").get, arr(0))}\n"))
              }
              case c:Char => buffer += c.toChar
              case _ =>
            }
          }
        }finally {
          iChannel.close; iFile.close
        }
      })
    })
    opt
  }
}