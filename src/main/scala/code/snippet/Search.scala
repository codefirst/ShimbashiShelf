package code.snippet

import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.RequestVar
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.java.util.Date
import code.lib._
import Helpers._
import org.codefirst.shimbashishelf._
import net.liftweb.http._

class Search extends StatefulSnippet  with PaginatorSnippet[Document] {
  var query = S.param("q").openOr("")
  var documents : Array[Document] = Array()

  override def dispatch : DispatchIt = {
    case "searchForm" => searchForm(_)
    case "show"       => show(_)
    case "paginate"   => paginate(_)
  }

  override def count =
    documents.size
  override def page  =
    documents.slice(curPage*itemsPerPage, (curPage+1)*itemsPerPage)
  override def pageUrl(offset: Long) : String =
    appendParams(super.pageUrl(offset), List("q" -> query))
  override def currentXml: NodeSeq =
    Text((first+1)+"-"+(first+itemsPerPage min count)+" of "+count)

  def searchForm(xhtml : NodeSeq) : NodeSeq = {
    def doSearch() {
      S.notice("doSearch : " + query)
      documents = Searcher.search(query, "content")
      redirectTo("/search?q="+query)
    }
    if(!query.isEmpty()) { documents = Searcher.search(query, "content") }

    bind("f", xhtml,
         "q" -> text(query, query = _) % ("autofocus" -> true) % ("id" -> "q"),
         "search" -> SHtml.submit(S.?("Search"), doSearch))
  }

  def show(xhtml : NodeSeq) : NodeSeq = {
    page.flatMap(document =>
      bind("result", xhtml,
           "path" -> document.path,
           "content" -> document.content,
	   "highlight" -> document.highlight,
	   "manageID" -> document.manageID
         ))
  }
}

