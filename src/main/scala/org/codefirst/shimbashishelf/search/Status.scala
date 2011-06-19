package org.codefirst.shimbashishelf.search

import net.liftweb.json._
import net.liftweb.json.JsonDSL._

import scala.collection.mutable
import java.io.File
import org.codefirst.shimbashishelf.util.FileUtil

object Status{
  def empty =
    new Status(mutable.Map())

  def withDefault(proc : Status => Unit) {
    val status = FileUtil.readAll(STATUS_PATH) match {
      case Some(text) => fromJson(text)
      case None => empty
    }
    proc(status)
    FileUtil.touch(new File(STATUS_PATH), status.toJson)
  }

  def fromJson(body : String) = {
    val json = JsonParser.parse(body)
    val xs : List[(String,Int)] = for {
      JField("intMap", JObject(xs)) <- json
      JField(key, JInt(value)) <- xs
    } yield (key -> value.toInt)
      new Status(mutable.Map(xs : _*))
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
  def toJson : String = {
    val json = ("version" -> 1) ~
               ("intMap" ->
                  intMap.foldLeft(JObject(List())){ case (obj, (k, v)) =>
                    obj ~ (k -> v) })
    compact(JsonAST.render(json))
  }
}
