package org.codefirst.shimbashishelf

import java.io.File

import org.apache.lucene.analysis.cjk.CJKAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version
import org.apache.lucene.index.Term
import org.apache.lucene.search._
import org.apache.lucene.queryParser._

object Searcher {
  def search(query : String, field : String) : Array[Document] = {
    using( FSDirectory.open(new File(INDEX_PATH)) ) { case dir =>
      using( new IndexSearcher(dir, true) ) { case searcher => {
	    val parser : QueryParser = new QueryParser(Version.LUCENE_31, field, new CJKAnalyzer(Version.LUCENE_31))
	    val td : TopDocs = searcher.search(parser.parse(query), 1000)
	    td.scoreDocs.map ((scoreDoc) => searcher.doc(scoreDoc.doc))
      }}}
  }

  def searchByPath(path : String) : Document = {
    if (path == null) {
      throw new Exception("null path is not allowed")
    }
    using( FSDirectory.open(new File(INDEX_PATH)) ) { case dir =>
      using( new IndexSearcher(dir, true) ) { case searcher => {
	val term : Term = new Term("path", path)
	val termQuery : Query = new TermQuery(term)
	val td : TopDocs= searcher.search(termQuery,10)
	td.scoreDocs.map ((scoreDoc) => searcher.doc(scoreDoc.doc))
      }}} match {
	case Array() =>
	  throw new Exception("cannot find: " + path)
	case Array(x) =>
	  x
	case _ =>
	  throw new Exception("found multiply: " + path)
      }
  }
}
