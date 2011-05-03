package org.codefirst.shimbashishelf

import java.io.File

import org.apache.lucene.analysis.cjk.CJKAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version

import org.apache.lucene.search._
import org.apache.lucene.queryParser._

object Searcher {
  val INDEX_PATH : String  = "index"
  def search(query : String, field : String) : Array[Document] = {
    val dir : Directory = FSDirectory.open(new File(INDEX_PATH))
    val searcher : IndexSearcher = new IndexSearcher(dir, true)
    val parser : QueryParser = new QueryParser(Version.LUCENE_31, field, new CJKAnalyzer(Version.LUCENE_31))
    val td : TopDocs = searcher.search(parser.parse(query), 1000)
    val docs = td.scoreDocs.map ((scoreDoc) => searcher.doc(scoreDoc.doc))
    searcher.close()
    dir.close()
    return docs
  }

  def search(query : String) : Array[Document] = {
    return search(query, "content")
  }
}
