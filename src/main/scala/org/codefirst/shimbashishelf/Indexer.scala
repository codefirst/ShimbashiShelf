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
    if (text == null) {
      return true
    }

    var writer : IndexWriter = null
    var dir : Directory = null
    try {
      dir = FSDirectory.open(new File(INDEX_PATH))
      writer = new IndexWriter(dir, new IndexWriterConfig(Version.LUCENE_31, new CJKAnalyzer(Version.LUCENE_31)))

      val doc : Document = new Document()

      val pathField : Field = new Field("path", path, Store.YES, Index.ANALYZED)
      val contentField : Field = new Field("content", text, Store.YES, Index.ANALYZED)

      doc.add(pathField)
      doc.add(contentField)
      writer.addDocument(doc)
    } catch {
      case e:IOException => return false
    } finally {
      if (writer != null) {
        writer.close()
      }
    }
    return true
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
