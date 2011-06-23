package org.codefirst.shimbashishelf.web

import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._
import org.apache.commons.codec.binary.Base64

import org.codefirst.shimbashishelf.util.SCalendar
import org.codefirst.shimbashishelf.util.Base._
import org.codefirst.shimbashishelf.vcs.Commit
import org.codefirst.shimbashishelf.filesystem.{File, FileSystem, Directory}

object View {
  def isPjax[A](req : HttpRequest[A]) : Boolean =
    notNull(req.headers("X-PJAX"), List()).nonEmpty

  def pjax[A](req : HttpRequest[A], dir : Directory) =
        if( isPjax(req) )
          Ok ~> Scalate(req, "view.scaml", ("layout", ""), ("files", dir.children))
        else
          Ok ~> Scalate(req, "view.scaml", ("files", dir.children))

  def apply[A](req : HttpRequest[A], path : List[String]) = {
    path match {
      case List() =>
        pjax(req, FileSystem.root)
      case _ =>
        NotFound ~> Scalate(req, "404.scaml")
    }
  }
}
