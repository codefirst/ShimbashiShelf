package org.codefirst.shimbashishelf.web

import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._

import org.codefirst.shimbashishelf.filesystem._
import org.codefirst.shimbashishelf.util.Base._
import org.codefirst.shimbashishelf.util.FileUtil

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
  case req@Path(Seg("view"::path)) =>
    View(req, path)
  case req@Path(Seg("download"::path)) =>
    val response = for {
      file@File(_,_) <- FileSystem.searchByPath(path.mkString("/"))
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
        NotFound ~> Scalate(req, "404.scaml")
    }
  case req@Path("/mkdir") =>
    (for {
      path <- getStr(req, "cwd")
      name <- getStr(req, "name")
      val dir = FileUtil.join(path,name)
      val _ = FileSystem.mkdir(dir)
    } yield Redirect("/view/" + dir)) getOrElse {
      BadRequest ~> Scalate(req, "404.scaml")
    }
  case POST(Path(Seg("upload"::cwd)) & MultiPart(req)) =>
    val multi = MultiPartParams.Streamed(req)
    multi.files("file") match {
      case Seq(f, _*) =>
        val name = f.name
        val file = FileUtil.join( cwd.mkString("/"), name)
        FileSystem.save(file, f.write(_) )
        Redirect("/view" + file)
      case _ =>  ResponseString("what's f?")
    }
  case req =>
    NotFound ~> Scalate(req, "404.scaml")
})
