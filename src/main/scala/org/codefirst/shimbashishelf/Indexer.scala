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

object Indexer {
  val INDEX_PATH : String  = "index"
  def index(path : String, text : String) : Boolean = {
    var writer : IndexWriter = null
    try {
      val dir : Directory = FSDirectory.open(new File(INDEX_PATH))
      val config : IndexWriterConfig = new IndexWriterConfig(Version.LUCENE_31, new CJKAnalyzer(Version.LUCENE_31))
      writer = new IndexWriter(dir, config)

      val doc : Document = new Document()

      val pathField : Field = new Field("path", path, Store.YES, Index.ANALYZED)
      val contentField : Field = new Field("content", text, Store.YES, Index.ANALYZED)

      doc.add(pathField)
      doc.add(contentField)
      
      writer.addDocument(doc)
    } catch {
      case e:IOException => e.printStackTrace()
    } finally {
      writer.close()
    }
    return true
  }

  def index(file : File) : Boolean = { 
    val text : String = TextExtractor.extract(file.getAbsolutePath())
    return Indexer.index(file.getAbsolutePath(), text)
  }
}
