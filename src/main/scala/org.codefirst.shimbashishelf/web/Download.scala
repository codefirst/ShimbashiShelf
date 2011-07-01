package org.codefirst.shimbashishelf.web

import unfiltered.response._
import org.apache.commons.codec.binary.Base64
import org.codefirst.shimbashishelf.filesystem.{File, FileSystem, Directory}

case class Download[A,B](context : Context[A,B], path : List[String]) {
  def response = {
    val response = for {
      file@File(_,_) <- FileSystem(path.map(Helper.decode(_)).mkString("/"))
      val name = file.name
      xs   <- file.read
    } yield (name,xs)

    response match {
      case Some((name, xs)) =>
        Ok ~>
        ResponseHeader("Content-Disposition",
                       List("attachment; filename=\"%s\"".format(name))) ~>
        CharContentType("application/octet-stream") ~>
        ResponseBytes(xs)
      case None =>
        NotFound ~> context.render("404.scaml")
    }
  }
}

