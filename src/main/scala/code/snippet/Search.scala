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
import org.codefirst.shimbashishelf.Searcher
import org.apache.lucene.document.Document
import net.liftweb.http._

class Search extends StatefulSnippet {
  var query = S.param("q").openOr("")
  var documents : Array[Document] = Array()

  override def dispatch : DispatchIt = {
    case "searchForm" => searchForm(_)
    case "show"       => show(_)
  }

  def searchForm(xhtml : NodeSeq) : NodeSeq = {
    def doSearch() {
      println("doSearch : " + query)
      documents = Searcher.search(query, "content")
      redirectTo("/search?q="+query)
    }
    if(!query.isEmpty()) { documents = Searcher.search(query, "content") }

    bind("f", xhtml,
         "q" -> text(query, query = _) % ("autofocus" -> true) % ("id" -> "q"),
         "search" -> SHtml.submit(S.?("Search"), doSearch))
  }

  def show(xhtml : NodeSeq) : NodeSeq = {
    <xml:Group> {
      documents.map(document =>
        bind("result", xhtml,
             "path" -> document.getField("path").stringValue(),
             "content" -> document.getField("content").stringValue()
           ))
    }</xml:Group>
  }
}

