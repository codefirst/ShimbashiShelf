package org.codefirst.shimbashishelf.monitor
import scala.util.matching.Regex
import java.io.File

object Glob{
  def apply(pattern : String) = new Glob(globToPattern(pattern))

  private def globToPattern(glob: String): Regex = {
    val star = "<STAR>"
    val authorizedNamePattern = "[^\\/\\?<>\\|\\" + star + ":\"]" + star
    var pattern = glob.replace("\\", "/")
      .replace(".", "\\.")
      .replace("*", authorizedNamePattern)
      .replace(star, "*")
    "^%s$".format(pattern).r
  }

  def split(file : File, sep : String = File.separator) : List[String] =
    file.getPath().split(sep.replace("\\","\\\\")).toList
}

class Glob(pattern : Regex){
  def isMatch(file : java.io.File) =
    Glob.split(file).exists(pattern.findFirstIn(_).isDefined)
}
