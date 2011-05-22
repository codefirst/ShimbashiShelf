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
}

class Glob(pattern : Regex){

  def isMatch(file : java.io.File) =
    parents(file).exists(pattern.findFirstIn(_).isDefined)

  private def parents(file : File) : List[String] =
    file.getPath().split(File.separator).toList
}
