package org.codefirst.shimbashishelf.web

import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._

import org.codefirst.shimbashishelf.filesystem._
import org.codefirst.shimbashishelf.util.Base._
import org.codefirst.shimbashishelf.util.FileUtil


class Plan extends unfiltered.filter.Plan{
  def scalate[A](request: HttpRequest[A], template: String, attributes:(String,Any)*) =
    HtmlContent ~> Scalate(request, template, attributes : _*)

  def intent = {
    case Path(Seg("static" :: _)) =>
      Pass
    case req@Path("/") =>
      Ok ~> scalate(req, "index.scaml")
    case req@Path("/about") =>
      Ok ~> scalate(req, "about.scaml")
    case req@Path("/search") =>
      Search(req)
    case req@Path("/calendar") =>
      Calendar(req)
    case req@Path(Seg("view"::path)) =>
      View(req, path.map(Helper.decode(_)))
    case req@Path(Seg("download"::path)) =>
      val response = for {
        file@File(_,_) <- FileSystem(path.map(Helper.decode(_)).mkString("/"))
        val name = file.name
        xs   <- file.read
      } yield (name,xs)
        response match {
          case Some((name, xs)) =>
            Ok ~>
          ResponseHeader("Content-Disposition", List("attachment; filename=\"%s\"".format(name))) ~>
          CharContentType("application/octet-stream") ~>
          ResponseBytes(xs)
          case None =>
            NotFound ~> scalate(req, "404.scaml")
        }
    case req@Path("/mkdir") =>
      (for {
        path <- getStr(req, "cwd")
        name <- getStr(req, "name")
        val dir = FileUtil.join(path,name)
        val _ = FileSystem.mkdir(dir)
      } yield Redirect( Helper.url_for("view", dir ))) getOrElse {
        BadRequest ~> scalate(req, "404.scaml")
      }
    case POST(Path(Seg("upload"::cwd)) & MultiPart(req)) =>
      val multi = MultiPartParams.Streamed(req)
    multi.files("file") match {
      case Seq(f, _*) =>
        val name = f.name
      val path = FileUtil.join( cwd.mkString("/"), name)
      FileSystem.save(path, f.write(_) ) match {
        case Some(file) =>
          Redirect( Helper.url_for( "view", file.url ))
        case None =>
          BadRequest ~> scalate(req, "404.scaml")
      }
      case _ =>  ResponseString("what's f?")
    }
    case req =>
      NotFound ~> scalate(req, "404.scaml")
  }
}

