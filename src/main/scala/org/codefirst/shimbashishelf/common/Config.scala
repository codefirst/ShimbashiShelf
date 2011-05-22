package org.codefirst.shimbashishelf.common
import org.codefirst.shimbashishelf.util.FileUtil
import java.io.File
import scala.util.parsing.input.CharArrayReader
import dispatch.json.JsonParser
import sjson.json._
import sjson.json.DefaultProtocol._

object Config{
  def open(path : String) =
    new Config(new File(path))

  def default(getenv : String => String) : Config = {
    val home = getenv("SHIMBASHI_SHELF_HOME")
    if( home != null && home != "")
      new Config(new File(home, "config.json"))
    else
      new Config(new File("config.json"))
  }
  def default : Config = default(System.getenv(_))
}

class Config(val path : File){
  // ------------------------------
  // field
  // ------------------------------
  var ignoreFiles : List[String] = List()

  // ------------------------------
  // serialize
  // ------------------------------
  private case class T(version : Int, ignoreFiles : List[String])
  private implicit val format: Format[T] =
    asProduct2("version", "ignoreFiles")(T)(T.unapply(_).get)
  private def json() : String =
    JsonSerialization.tojson(T(1, ignoreFiles)).toString

  // ------------------------------
  // deserialize
  // ------------------------------
  FileUtil.readAll(path.getAbsolutePath()) match {
    case None => ()
    case  Some(body) => {
      val reader = new CharArrayReader(body.toArray)
      val js = JsonParser(reader)
      val t = JsonSerialization.fromjson[T](js)
      ignoreFiles = t.ignoreFiles
    }
  }

  def save() {
    FileUtil.touch(path, json)
  }
}
