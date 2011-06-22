package org.codefirst.shimbashishelf.web

import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._
import org.apache.commons.codec.binary.Base64

import org.codefirst.shimbashishelf.util.SCalendar
import org.codefirst.shimbashishelf.util.Base._
import org.codefirst.shimbashishelf.vcs.Commit
import org.codefirst.shimbashishelf.filesystem.{File, FileSystem}

object View {
  def isPjax[A](req : HttpRequest[A]) : Boolean =
    notNull(req.headers("X-PJAX"), List()).nonEmpty

  def pjax[A](req : HttpRequest[A], template : String) =
        if( isPjax(req) )
          Ok ~> Scalate(req, template, ("layout", ""))
        else
          Ok ~> Scalate(req, template)

  def apply[A](req : HttpRequest[A], path : List[String]) = {
    path match {
      case List() =>
        pjax(req, "index.scaml")
      case "tmp"::_ | "images"::_ | "xxx"::_ =>
        pjax(req, "dummy.scaml")
      case _ =>
        NotFound ~> Scalate(req, "404.scaml")
    }
  }
}
