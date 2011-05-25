package org.codefirst.shimbashishelf.web.snippet

import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.RequestVar
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import net.liftweb.http._
import Helpers._

import org.codefirst.shimbashishelf._
import org.codefirst.shimbashishelf.search.Document

class Show {
  val id = S.param("id").openOr("0")
  lazy val document : Box[Document] = Box(Document.get(id.toInt))

  def render(xhtml : NodeSeq) : NodeSeq = {
    val seq = for {
      doc <-document
      val extra = List(
        "link" -> { (x:NodeSeq) => <a class="download" href={"/download/" + doc.id}>{x}</a>},
        "pathfield" -> { (x:NodeSeq) => <input type="text" class="info" value={doc.path} onmousedown="this.select();"/>})
      val base = doc.toBindParams
    } yield extra ++ base
    bind("result", xhtml,
         seq.getOrElse(List()) : _*)
  }
}
