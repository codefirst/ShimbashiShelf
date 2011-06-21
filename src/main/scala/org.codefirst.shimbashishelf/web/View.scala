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
  def apply[A](req : HttpRequest[A], path : List[String]) = {
    path match {
      case List() =>
        Ok ~> Scalate(req, "index.scaml")
      case "tmp"::_ | "images"::_ | "xxx"::_ =>
        Ok ~> Scalate(req, "dummy.scaml")
      case _ =>
        NotFound ~> Scalate(req, "404.scaml")
    }
  }
}
