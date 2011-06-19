package org.codefirst.shimbashishelf.web.snippet

import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.RequestVar
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.java.util.Date
import Helpers._
import net.liftweb.util.BindPlus._
import net.liftweb.http._
import org.codefirst.shimbashishelf.filesystem._
import org.codefirst.shimbashishelf.web.helper.FileHelper
import scala.xml.Node

class Documents extends PaginatorSnippet[(FileObject,Node)] {
  var query = S.param("q").openOr("")
  lazy val files : Seq[(FileObject, Node)] = S.param("q") match {
    case Full(q) =>
      FileSystem.searchByQuery(q)
    case _ =>
      Seq() }

  override def itemsPerPage =
    10
  override def count =
    files.size
  override def page  =
    files.slice(curPage*itemsPerPage, (curPage+1)*itemsPerPage)
  override def pageUrl(offset: Long) : String =
    appendParams(super.pageUrl(offset), List("q" -> query))
  override def currentXml: NodeSeq =
    Text((first+1)+"-"+(first+itemsPerPage min count)+" of "+count+" files")
  override def prevXml: NodeSeq =
    Text(S.?("< prev"))
  override def nextXml: NodeSeq =
    Text(S.?("next >"))
  override def pageXml(newFirst: Long, ns: NodeSeq): NodeSeq =
    if(first==newFirst)
      <span>{ns}</span>
    else
      <a href={pageUrl(newFirst)}><span>{ns}</span></a>

  def search(xhtml : NodeSeq) : NodeSeq = {
    def doSearch() {
      S.redirectTo(appendParams("/search", List("q" -> query)))
    }

    bind("f", xhtml,
         "q" -> text(query, query = _) % ("autofocus" -> S.attr("autofocus")) % ("id" -> "q"),
         "search" -> SHtml.submit(S.?("Search"), doSearch))
  }

  def info(xhtml : NodeSeq) : NodeSeq =
    bind("result", xhtml,
         "query" -> query)

  def show(xhtml : NodeSeq) : NodeSeq =
    page.flatMap {
      case   (Directory(_,_), high) =>
        xhtml
      case (file@File(id, _, _, _, _), high) =>
        xhtml.
         bind("result",
              "link" -> <a href={"/show?id=" + id + "&q=" + query}>{file.name}</a>,
              "highlight" -> high).
         bind("result",
              FileHelper.asBind(file) : _*)
    }
}
