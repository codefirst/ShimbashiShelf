package org.codefirst.shimbashishelf.web

import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._

import org.codefirst.shimbashishelf.filesystem._

class Plan extends unfiltered.filter.Planify({
  case Path(Seg("static" :: _)) =>
    Pass
  case req@Path("/") =>
    Ok ~> Scalate(req, "index.scaml")
  case req@Path("/about") =>
    Ok ~> Scalate(req, "about.scaml")
  case req@Path("/search") =>
    Search(req)
  case req@Path("/calendar") =>
    Calendar(req)
  case req@Path(Seg("show"::id::_)) =>
    Show(req, id)
  case req@Path(Seg("download"::id::_)) =>
    val response = for {
      file <- FileSystem(id)
      val name = file.name
      xs   <- FileSystem.read(file)
    } yield (name,xs)
    response match {
      case Some((name, xs)) =>
        Ok ~>
          ResponseHeader("Content-Disposition", List("attachment; filename=\"%s\"".format(name))) ~>
          CharContentType("application/octet-stream") ~>
          ResponseBytes(xs)
      case None =>
        NotFound ~> Scalate(req, "404.scaml")
    }
  case req =>
    NotFound ~> Scalate(req, "404.scaml")
})
