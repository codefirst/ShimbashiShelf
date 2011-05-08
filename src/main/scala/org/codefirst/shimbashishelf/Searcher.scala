package org.codefirst.shimbashishelf

import java.io.File
import org.apache.lucene.search.highlight._
import org.apache.lucene.analysis.cjk.CJKAnalyzer
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version
import org.apache.lucene.index.Term
import org.apache.lucene.search._
import org.apache.lucene.queryParser._

object Searcher {
  val analyzer = new CJKAnalyzer(Version.LUCENE_31)

  private def searchByQuery(query : Query, field : String) : Array[Document] =
    using( FSDirectory.open(new File(INDEX_PATH)) ) { case dir =>
      using( new IndexSearcher(dir, true) ) { case searcher => {
	val scorer = new QueryScorer(query, field)
	val hightlighter = new Highlighter(scorer)
	val td : TopDocs = searcher.search(query, 1000)
	for {
	  scoreDoc    <- td.scoreDocs
	  val doc     = searcher.doc(scoreDoc.doc)
	  val content = doc.getField("content").stringValue()
	  val high    = hightlighter.getBestFragment(analyzer, field, content)
	} yield Document(doc,high)
      } } }

  def search(query : String, field : String) : Array[Document] = {
    val parser : QueryParser = new QueryParser(Version.LUCENE_31, field, analyzer)
    searchByQuery(parser.parse(query),field)
  }

  def searchByPath(path : String) : Document = {
    if (path == null) {
      throw new Exception("null path is not allowed")
    }
    val term : Term = new Term("path", path)
    val query : Query = new TermQuery(term)
    searchByQuery(query, "path") match {
      case Array() =>
	throw new Exception("cannot find: " + path)
      case Array(x) =>
	x
      case _ =>
	throw new Exception("found multiply: " + path)
    }
  }
}

