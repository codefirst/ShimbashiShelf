package org.codefirst.shimbashishelf

import java.io.File
import java.io.IOException
import org.apache.lucene.analysis.cjk.CJKAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.Field.Index
import org.apache.lucene.document.Field.Store
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version
import org.apache.lucene.index.{IndexWriterConfig, IndexWriter}
import org.apache.lucene.analysis.SimpleAnalyzer
import org.apache.lucene.index.Term

object SLucene {
  implicit def tuple2term(x : (String, String)) : Term =
    new Term(x._1, x._2)

  implicit def tuple2field(x : (String, String, Field.Store, Field.Index)) : Field =
    new Field(x._1, x._2, x._3, x._4)
}

object Indexer {
  import SLucene._


  def using[A <: { def close() : Unit }, B](resource : A)(body : A => B) : Option[B] =
    try     { Some(body(resource)) }
    catch   { case _ => None }
    finally { resource.close() }


  def index(path : String, text : String) : Boolean = {
    val config = new IndexWriterConfig(Version.LUCENE_31, new CJKAnalyzer(Version.LUCENE_31))
    using(FSDirectory.open(new File(INDEX_PATH))) { case dir =>
      using(new IndexWriter(dir, config)){
	case writer => {
	  writer.deleteDocuments(("path", path))
	  val doc : Document = new Document()
	  doc.add(("path", path, Store.YES, Index.NOT_ANALYZED))
	  doc.add(("content", text, Store.YES, Index.ANALYZED))
	  writer.addDocument(doc)
	}}} match {
	  case Some(_) => true
	  case None    => false
	}
  }

  def index(file : File) : Boolean = {
    val text : String = TextExtractor.extract(file.getAbsolutePath())
    return Indexer.index(file.getAbsolutePath(), text)
  }

  def indexRecursively(file : File) : Boolean = {
    if (file.isFile()) {
      return index(file)
    }
    file.listFiles.map(indexRecursively).forall(identity)
  }
}
