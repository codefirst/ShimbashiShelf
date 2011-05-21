package org.codefirst.shimbashishelf.search

import scala.xml.XML
import java.io.File
import net.liftweb.util._
import net.liftweb.util.Helpers._
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.search.IndexSearcher
import org.codefirst.shimbashishelf._
import org.codefirst.shimbashishelf.util.FileUtil

object Document {
  private[search] def apply(id : Int, doc : org.apache.lucene.document.Document, hightligth : String) =
    new Document(id, doc, hightligth)

  def get(id : Int) : Option[Document] =
    try{
      using( FSDirectory.open(new File(INDEX_PATH)) ) { case dir =>
	using( new IndexSearcher(dir, true) ) { case searcher => {
          val doc   = searcher.doc(id)
          Some(new Document(id, doc,"<pre />"))
	}}}
    } catch { case _ => None }
}

class Document(val id : Int, doc : org.apache.lucene.document.Document, high : String){
  def path     = getString("path")
  def filename = FileUtil.basename(path)
  def content  = getString("content")
  def manageID  = getString("manageID")
  val highlight = try { XML.loadString(high) } catch { case _ => <pre /> }
  def is : Option[Array[Byte]] = FileUtil.readArray(path)


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
