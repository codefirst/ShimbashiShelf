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

class Search {
  lazy val date: Box[Date] = DependencyFactory.inject[Date] // inject the date

  object message extends RequestVar(Full(""))
  var documents : Array[Document] = Array()

  def searchForm(xhtml : NodeSeq) : NodeSeq = {
    if (message.get.get != "") { doSearch() }

    bind( "f", xhtml,
         "q" -> text(message.get.get, m => message(Full(m))),
         "search" -> SHtml.submit("search", doSearch)
       )
  }
  
  def show(xhtml : NodeSeq) : NodeSeq = {
    <xml:Group> { 
      documents.map(document => 
        bind("result", xhtml, 
             "path" -> document.getField("path").stringValue(),
             "content" -> document.getField("content").stringValue().substring(0, 100)
           ))
    }</xml:Group>
  }

  private def doSearch() {
    documents = Searcher.search(message.get.get, "content")
    documents.foreach { d => println(d.getField("path").stringValue())}
  }
}

}
}
