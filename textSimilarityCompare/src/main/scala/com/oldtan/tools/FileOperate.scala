package com.oldtan.tools

import java.io.{File, RandomAccessFile}
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets

import scala.collection.mutable

object FileOperate {

  def writeFile(file: File, text:String)={
    val oFile = new RandomAccessFile(file, "rw")
    val oChannel = oFile.getChannel
    val bytes = if(oFile.length > 0) s"\n$text".getBytes("UTF-8") else text.getBytes("UTF-8")
    val m = oChannel.map(FileChannel.MapMode.READ_WRITE, oFile.length, bytes.length)
    try{
      (m.limit - m.position - bytes.length) >= 0 match {
        case true => m put bytes
        case _ => {m.force; m.clear; m put bytes}
      }
    }finally {
      m.force; oChannel.close; oFile.close
    }
  }

  def readFile(file: File, op: String => Boolean) = {
    val iFile = new RandomAccessFile(file, "r")
    val iChannel = iFile.getChannel
    val m = iChannel.map(FileChannel.MapMode.READ_ONLY, 0, iChannel.size)
    val dBuffer = StandardCharsets.UTF_8.newDecoder.decode(m)
    val buffer = mutable.StringBuilder.newBuilder
    try {
      var flag = true
      for (i <- 0 until dBuffer.limit if flag){
        dBuffer.get(i) match {
          case '\n' => flag = op.apply(buffer.toString)
          case c:Char => buffer += c.toChar
          case _ =>
        }
      }
    }finally {
      iChannel.close; iFile.close
    }
  }

}