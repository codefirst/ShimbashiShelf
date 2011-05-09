package org.codefirst.shimbashishelf
import java.io.FileInputStream
import scala.math.max

object FileUtil {
  def getExtension(path : String) : String = {
    val i =path.lastIndexOf(".")
    if (i > 0)
      path.substring(i + 1)
    else
      ""
  }

  def basename(path : String) : String = {
    val i = max(path.lastIndexOf("/"),
		path.lastIndexOf("\\"))
    if (i > 0)
      path.substring(i + 1)
    else
      path
  }

  def readAll(path : String) : Option[String] =
    try {
      val in = new FileInputStream(path)
      try{
	val sb = new StringBuffer()
	var c  : Int = in.read
	while( c != -1){
	  sb.append(c.toChar)
	  c = in.read
	}
	Some(sb.toString)
      } finally { in.close }
    } catch { case _ => None}
}
