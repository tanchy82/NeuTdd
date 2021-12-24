package com.oldtan.arithmetic

import com.hankcs.hanlp.tokenizer.NLPTokenizer

/**
  * Simhash Algorithm
  */
object SimhashAlgorithm {

  val bits = 16

  def simhashCode(text: String): String = {
    /** Step 1: text tokenizer return term list */
    NLPTokenizer.analyze(text).toStringWithoutLabels.split(" ")
      /** Step 2: every term  transform 64 bit binary string, return binary string list */
      .map(s => s"%0${bits}d".format(BigInt(s.hashCode.toBinaryString)))
      /** Step 3: weighted operate，default weight 1 */
      .map(_.toCharArray.map(s => if (s == '1') 2 else -1).mkString(","))
      /** Step 4: merge operate */
      .reduceLeft((a, b) => {
        val ar = a.split(",")
        val br = b.split(",")
        (0 until bits).map(i => ar(i).toInt + br(i).toInt).mkString(",")
      })
      /** Step 5: binary operate */
      .split(",").map(_.toInt).map(i => if (i > 0) 1 else 0).mkString
  }

  def hammingDistance(b1: String, b2: String): Boolean = {
    val pos = bits/4
    if ((0 until bits by pos).map(i =>
      Integer.parseInt(b1.substring(i, pos+i),2) ^ Integer.parseInt(b2.substring(i, pos+i),2))
      .filter(_ > 0).toList.size < 3) true else false
  }

}