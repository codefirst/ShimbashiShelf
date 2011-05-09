package org.codefirst.shimbashishelf
import sjson.json._
import sjson.json.DefaultProtocol._
import scala.collection.mutable
import dispatch.json.JsonParser
import scala.util.parsing.input.CharArrayReader
import java.io.FileWriter

object Status{
  case class JsStatus(version : Int, intMap : Map[String, Int])
  implicit val JsStatusFormat: Format[JsStatus] =
    asProduct2("version", "intMap")(JsStatus)(JsStatus.unapply(_).get)

  def empty =
    new Status(mutable.Map())

  def withDefault(proc : Status => Unit) {
    val status = FileUtil.readAll(STATUS_PATH) match {
      case Some(text) => fromJson(text)
      case None => empty
    }
    proc(status)
    val w = new FileWriter(STATUS_PATH)
    try { w.write(status.toJson) }
    finally{  w.close }
  }

  def fromJson(json : String) = {
    val reader = new CharArrayReader(json.toArray)
    val js     = JsonParser(reader)
    val status = JsonSerialization.fromjson[JsStatus](js)
    new Status(scala.collection.mutable.Map(status.intMap.toSeq: _*))
  }
}

class Status(intMap : mutable.Map[String, Int] ){
  def update(key : String, value : Int) =
    intMap(key) = value

  def int(key : String) : Int =
    intMap(key)

  def safeInt(key : String) : Option[Int] =
    intMap.get(key)

  import Status._
  def toJson : String =
     JsonSerialization.tojson(JsStatus(1, intMap.toMap)).toString
}
