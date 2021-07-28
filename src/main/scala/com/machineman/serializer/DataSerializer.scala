package com.machineman.serializer

import akka.serialization.Serializer
import com.machineman.commands.Data

class DataSerializer extends Serializer{
  val SEPARATOR = "||"
  override def identifier: Int = 2500

  override def toBinary(o: AnyRef): Array[Byte] = o match {
    case person @ Data(message, id) =>
      println(s"Serializing $person")
      s"[$message$SEPARATOR$id]".getBytes()
    case _ => throw new IllegalArgumentException("only data are supported for this serializer")
  }

  override def includeManifest: Boolean = false

  override def fromBinary(bytes: Array[Byte], manifest: Option[Class[_]]): AnyRef = {
    val string = new String(bytes)
    val values = string.substring(1, string.length - 1).split(SEPARATOR)
    val message = values(0)
    val id = values(1)
    val data = Data(message, id)
    data
  }
}
