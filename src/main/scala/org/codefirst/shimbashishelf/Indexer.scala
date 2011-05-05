package org.codefirst.shimbashishelf

import java.io.File
import java.io.IOException
import org.apache.lucene.analysis.cjk.CJKAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version
import org.apache.lucene.index.{IndexWriterConfig, IndexWriter}
import org.apache.lucene.document.Field.{Index,Store}
import org.apache.lucene.analysis.SimpleAnalyzer

object Indexer {
  import SLucene._

  def index(path : String, text : String) : Boolean = {
    val config = new IndexWriterConfig(Version.LUCENE_31, new CJKAnalyzer(Version.LUCENE_31))
    try {
      using(FSDirectory.open(new File(INDEX_PATH))) { case dir =>
	    using(new IndexWriter(dir, config)){
	      case writer => {
	        writer.deleteDocuments(("path", path))
	        val doc : Document = new Document()
	        doc.add(("path", path, Store.YES, Index.NOT_ANALYZED))
	        doc.add(("content", text, Store.YES, Index.ANALYZED))
	        writer.addDocument(doc)
	      }}}
      true
    }catch{ case _ => false
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
