package org.codefirst.shimbashishelf.web

import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._
import unfiltered.filter.InittedFilter

import org.fusesource.scalate._
import org.fusesource.scalate.servlet._

import org.codefirst.shimbashishelf.filesystem._
import org.codefirst.shimbashishelf.util.Base._
import org.codefirst.shimbashishelf.util.FileUtil

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

class Plan extends unfiltered.filter.Plan with Template {
  def intent = { case request =>
    val context = Context[HttpServletRequest, HttpServletResponse](request, render(request)_ )
    request match {
      case Path(Seg("static" :: _)) | Path("/favicon.ico") =>
        Pass
      case Path("/") =>
        Ok ~> context.render("index.scaml")
      case Path("/about") =>
        Ok ~> context.render("about.scaml")
      case Path("/search") =>
        Search(context).response
      case Path("/calendar") =>
        Calendar(context).response
      case Path(Seg("view"::path)) =>
        View( context, path.map(Helper.decode(_)) ).response
      case Path(Seg("download"::path)) =>
        Download(context, path).response
      case Path("/mkdir") =>
        (for {
          path <- context string "cwd"
          name <- context string "name"
          val dir = FileUtil.join(path,name)
          val _ = FileSystem.mkdir(dir)
        } yield Redirect( Helper.url_for("view", dir ))) getOrElse {
          BadRequest ~> context.render("404.scaml")
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
                BadRequest ~> context.render("404.scaml")
            }
          case _ =>
            BadRequest ~> context.render("404.scaml")
        }
      case _ =>
        NotFound ~> context.render("404.scaml")
    }
  }
}
