package org.codefirst.shimbashishelf.util
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
    readArray(path).map(new String(_))

  def readArray(path : String) : Option[Array[Byte]] =
    try {
      val in = new FileInputStream(path)
      try{
	val ab = new scala.collection.mutable.ArrayBuffer[Byte]()
	var c  : Int = in.read
	while( c != -1){
	  ab.append(c.toByte)
	  c = in.read
	}
	Some(ab.toArray)
      } finally { in.close }
    } catch { case _ => None}
}
