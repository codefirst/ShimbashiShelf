package org.codefirst.shimbashishelf.web

import unfiltered.response.Ok
import org.codefirst.shimbashishelf.filesystem.{File, FileSystem}

case class Search[A,B](context : Context[A,B]) {
  val itemsPerPage =
    10
  val query =
    context string "q"

  val files : Iterable[(File, scala.xml.Node)] =
    query match {
      case Some(q) =>
        FileSystem.searchByQuery(q)
      case None =>
        Seq()
    }

  def response =
    Ok ~> context.render( "search.scaml",
                         ("query", query.getOrElse("")),
                         ("page" ,  files))

}
