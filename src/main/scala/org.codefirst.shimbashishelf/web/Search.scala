package org.codefirst.shimbashishelf.web

import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._

import org.codefirst.shimbashishelf.util.SCalendar
import org.codefirst.shimbashishelf.util.Base._
import org.codefirst.shimbashishelf.vcs.Commit
import org.codefirst.shimbashishelf.filesystem.{File, FileSystem}

object Search{
  val itemsPerPage = 10

  def apply[A](req : HttpRequest[A]) = {
    val query =
      getStr(req, "q")
    val files : Iterable[(File, scala.xml.Node)] =
      query match {
        case Some(q) =>
          FileSystem.searchByQuery(q)
        case None =>
          Seq()
      }

    Ok ~> Scalate(req,
                  "search.scaml",
                  ("query", query.getOrElse("")),
                  ("page" ,  files))
  }
}
