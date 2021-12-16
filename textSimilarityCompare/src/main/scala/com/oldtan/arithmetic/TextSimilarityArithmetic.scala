package com.oldtan.arithmetic

import info.debatty.java.stringsimilarity.NGram

import scala.collection.mutable

class TextSimilarityArithmetic {

  val dataCollection = mutable.ListBuffer.empty[Map[String, String]]

  def calculate(subDataCollection: List[Map[String, String]]): String = {
    val buffer = mutable.StringBuilder.newBuilder
    var skipNum = 0
    subDataCollection.foreach(f => {
      skipNum += 1
      buffer ++= loopCollection(f, subDataCollection.toIterable, skipNum)
      buffer ++= loopCollection(f, dataCollection.toIterable,0)
    })
    dataCollection ++= subDataCollection
    buffer.mkString
  }

  def loopCollection(f: Map[String, String], c: Iterable[Map[String, String]], s:Int):String ={
    c.filter(f.get("pkid") != _.get("pkid"))
      .filter(f2 => compare(f.get("itemvalue"), f2.get("itemvalue")) > 0.1)
      .map(f2 => s"Text Similarity: ${f.get("pkid")} and ${f2.get("pkid")} \n").mkString
  }

  def compare(s1: Option[String], s2: Option[String]): Double ={
    val ngram = new NGram(8)
    ngram.distance(s1.get, s2.get)
  }

}
