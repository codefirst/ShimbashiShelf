package org.codefirst.shimbashishelf

import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.util.ArrayList
import java.util.List
import org.apache.lucene.analysis.cjk.CJKAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.Field.Index
import org.apache.lucene.document.Field.Store
import org.apache.lucene.index.CorruptIndexException
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version
import org.apache.lucene.search._

import org.apache.lucene.queryParser._

object Indexer {
  val INDEX_PATH : String  = "index"
  def index(path : String, text : String) : Boolean = {
    var writer : IndexWriter = null
    try {
      val dir : Directory = FSDirectory.open(new File(INDEX_PATH))
      writer = new IndexWriter(dir, new CJKAnalyzer(Version.LUCENE_31),
                               IndexWriter.MaxFieldLength.UNLIMITED)

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
