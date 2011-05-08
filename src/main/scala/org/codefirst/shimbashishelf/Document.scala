package org.codefirst.shimbashishelf
import scala.util.matching._
import scala.xml.XML

object Document {
  def apply(doc : org.apache.lucene.document.Document) =
    new Document(doc,"")

  def apply(doc : org.apache.lucene.document.Document, hightligth : String) =
    new Document(doc,hightligth)
}

class Document(doc : org.apache.lucene.document.Document, high : String){
  def path    = getString("path")
  def content = getString("content")
  val highlight = XML.loadString("<pre>" + high + "</pre>")

  private def getString(key : String) : String = {
    doc.getField(key).stringValue()
  }
}
