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
  def index(text : String) {
    var writer : IndexWriter = null
    try {
      val dir : Directory = FSDirectory.open(new File(INDEX_PATH))
      writer = new IndexWriter(dir, new CJKAnalyzer(Version.LUCENE_31),
                               IndexWriter.MaxFieldLength.UNLIMITED)
      val doc : Document = new Document()
      val field : Field = new Field("document", text, Store.YES, Index.ANALYZED)
      doc.add(field)
      writer.addDocument(doc)
    } catch {
      case e:IOException => e.printStackTrace()
    } finally {
      writer.close()
    }
  }
  def main(args : Array[String]) {
    Indexer.index(args(0))
  }
}
