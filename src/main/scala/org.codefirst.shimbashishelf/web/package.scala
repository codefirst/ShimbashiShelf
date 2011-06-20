package org.codefirst.shimbashishelf

import unfiltered.request._

import org.codefirst.shimbashishelf.util.Base._


package object web {
  def getStr[A](req : HttpRequest[A], name : String) : Option[String] = {
    val xs = req.parameterValues(name)
    if(xs eq null)
      None
    else
      xs.headOption
  }

  def getInt[A](req : HttpRequest[A], name : String, value : Int) : Int = {
    getStr(req, name).flatMap(s => sure(s.toInt)).getOrElse(value)
  }
}
