package org.codefirst

import java.io._

package object shimbashishelf {
  //@ indexを格納するファイル名
  val INDEX_PATH : String  = "index"

  val STATUS_PATH : String  = "status.json"

  def using[A <: { def close() : Unit }, B](resource : A)(body : A => B) : B =
    try     { body(resource) }
    finally { resource.close() }

  def readAll(path : String) : Option[String] =
    try {
      val in : InputStream = new FileInputStream(path)
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
