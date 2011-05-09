package org.codefirst.shimbashishelf
import scala.xml.XML
import net.liftweb.util._
import net.liftweb.util.Helpers._


object Document {
  def apply(id : Int, doc : org.apache.lucene.document.Document, hightligth : String) =
    new Document(id, doc, hightligth)
}

class Document(val id : Int, doc : org.apache.lucene.document.Document, high : String){
  def path     = getString("path")
  def filename = FileUtil.basename(path)
  def content  = getString("content")
  def manageID  = getString("manageID")
  val highlight = XML.loadString(high)

  def toBindParams : List[BindParam]=
    List("path" -> path,
         "id"   -> id,
         "content" -> content,
         "filename" -> filename,
         "highlight" -> highlight,
         "manageID" -> manageID)

  private def getString(key : String) : String = {
    doc.getField(key).stringValue()
  }
}
