package com.oldtan.operation

import com.oldtan.arithmetic.SimhashAlgorithm
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.configuration.Configuration
import collection.JavaConverters._

class TextSimilarityRichMap extends RichMapFunction[(String,String,String), Option[(String,String)]]{

  private var lastState:ListState[DocumentHash] = _

  override def open(parameters: Configuration) = {
    val desc = new ListStateDescriptor[DocumentHash]("lastState", classOf[DocumentHash])
    lastState = getRuntimeContext.getListState(desc)
  }

  override def map(d: (String, String, String)): Option[(String, String)] = {
    var opt = Option.empty[(String,String)]
    val it = lastState.get.asScala
    for (v <- it.toStream if opt.isEmpty){
      if (SimhashAlgorithm.hammingDistance(d._3, v.hash)) opt = Option(d._1, v.pkid)
    }
    lastState.add(DocumentHash(d._1,d._3))
    opt
  }
}
case class DocumentHash(pkid:String, hash:String)