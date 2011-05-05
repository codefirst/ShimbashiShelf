package code {
package snippet {

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
import net.liftweb.http.S

class Search {
//  lazy val date: Box[Date] = DependencyFactory.inject[Date] // inject the date

//  object message extends RequestVar(Full("q"))
  var q : String = S.param("q").openOr("")
  var documents : Array[Document] = Array()

  def searchForm(xhtml : NodeSeq) : NodeSeq = {
    if (q != "") { doSearch() }

    bind( "f", xhtml,
         "q" -> text(q, q = _) % ("autofocus" -> true) % ("id" -> "q") % ("name" -> "q"),
         "search" -> SHtml.submit(S.?("Search"), doSearch) % ("name" -> "s")
       )
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

  private def doSearch() {
    documents = Searcher.search(q, "content")
  }
}

}
}
