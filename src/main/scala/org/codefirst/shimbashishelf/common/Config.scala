package org.codefirst.shimbashishelf.common
import java.io.File

import net.liftweb.json._
import net.liftweb.json.JsonAST._
import net.liftweb.json.JsonDSL._

import org.codefirst.shimbashishelf.util.FileUtil

object Config{
  def open(path : String) =
    new Config(new File(path))

  def default(getenv : String => String) : Config = {
    new Config( Home.file("config.json",getenv) )
  }
  def default : Config = default(System.getenv(_))
}

class Config(val path : File){
  // ------------------------------
  // field
  // ------------------------------
  var ignoreFiles : List[String] = List(".git")
  var repository  : String = FileUtil.join(System.getProperty("user.home"),"ShimbashiShelf")
  // ------------------------------
  // serialize
  // ------------------------------
  def toJson = {
    ("version" -> 1) ~
    ("ignoreFiles" -> ignoreFiles) ~
    ("repository" -> repository)
  }

  FileUtil.readAll(path.getAbsolutePath()) match {
    case None => ()
    case Some(body) => {
      val json = JsonParser.parse(body)
      ignoreFiles = for {
        JField("ignoreFiles", JArray(xs)) <- json
        JString(x) <- xs
      } yield x
      for {
        JField("repository", JString(x)) <- json
      } repository = x
    }
  }

  def save() {
    FileUtil.touch(path, compact(JsonAST.render(toJson)))
  }
}
