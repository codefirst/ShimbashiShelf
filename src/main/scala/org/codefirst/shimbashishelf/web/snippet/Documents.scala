package org.codefirst.shimbashishelf.web.snippet

import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.RequestVar
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.java.util.Date
import Helpers._
import org.codefirst.shimbashishelf.search.{Document,Searcher}
import net.liftweb.http._

class Documents extends PaginatorSnippet[Document] {
  var query = S.param("q").openOr("")
  lazy val documents : Array[Document] = S.param("q") match {
    case Full(q) =>
      Searcher.search(q, "content")
    case _ =>
      Array() }

  override def count =
    documents.size
  override def page  =
    documents.slice(curPage*itemsPerPage, (curPage+1)*itemsPerPage)
  override def pageUrl(offset: Long) : String =
    appendParams(super.pageUrl(offset), List("q" -> query))
  override def currentXml: NodeSeq =
    Text((first+1)+"-"+(first+itemsPerPage min count)+" of "+count)

  def search(xhtml : NodeSeq) : NodeSeq = {
    def doSearch() {
      S.notice("doSearch : " + query)
      S.redirectTo("/search?q="+ query)
    }

    bind("f", xhtml,
         "q" -> text(query, query = _) % ("autofocus" -> S.attr("autofocus")) % ("id" -> "q"),
         "search" -> SHtml.submit(S.?("Search"), doSearch))
  }

  def show(xhtml : NodeSeq) : NodeSeq = {
    page.flatMap(document => {
      val seq =
        List("link" -> <a href={"/show?id=" + document.id + "&q=" + query}>{document.filename}</a>) ++
        document.toBindParams
      bind("result", xhtml,
           seq : _*)})
  }
}
