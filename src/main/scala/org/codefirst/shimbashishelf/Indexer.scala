package org.codefirst.shimbashishelf

import java.io.File
import java.io.IOException
import org.apache.lucene.analysis.cjk.CJKAnalyzer
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version
import org.apache.lucene.index.{IndexWriterConfig, IndexWriter}
import org.apache.lucene.document.Field.{Index,Store}
import org.apache.lucene.analysis.SimpleAnalyzer
import scala.collection.immutable.Stream

trait IdGenerator {
  def generate(file : File, status : Status) : String
}

class SeqIdGenerator extends IdGenerator {
  def generate(file : File, status : Status) = {
    val id = status.safeInt("id").getOrElse(0)
    status("id") = id + 1
    id.toString
  }
}

object Indexer {
  def apply() = new Indexer(new SeqIdGenerator)

  def allFiles(file : File) : Stream[File] = {
    if (file.isFile()) {
      Stream.cons(file, Stream.empty)
    }else{
      for {
	sub  <- file.listFiles.toStream
	file <- allFiles(sub)
      } yield file
    }
  }
}

class Indexer(idGenerator : IdGenerator) {
  import SLucene._
  val config = new IndexWriterConfig(Version.LUCENE_31, new CJKAnalyzer(Version.LUCENE_31))

  def index(path : String, text : String) {
    using(FSDirectory.open(new File(INDEX_PATH))) { case dir =>
      using(new IndexWriter(dir, config)){
	case writer =>
	  Status.withDefault { case status => {
	    val manageID = idGenerator.generate( new File(path), status)
	    println(manageID)
	    writer.deleteDocuments(("path", path))
	    val doc = new org.apache.lucene.document.Document()
	    doc.add(("path", path, Store.YES, Index.NOT_ANALYZED))
	    doc.add(("manageID", manageID, Store.YES, Index.NOT_ANALYZED))
	    doc.add(("content", text, Store.YES, Index.ANALYZED))
	    writer.addDocument(doc)
	  }}}}
  }

  def index(file : File) {
    index(file.getAbsolutePath(),
	  TextExtractor.extract(file.getAbsolutePath()))
  }
}
