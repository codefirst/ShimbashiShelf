package org.codefirst.shimbashishelf.search

import java.io.File
import org.apache.lucene.search.highlight._
import org.apache.lucene.analysis.cjk.CJKAnalyzer
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version
import org.apache.lucene.index.Term
import org.apache.lucene.search._
import org.apache.lucene.queryParser._
import org.codefirst.shimbashishelf._

object Searcher{
  def apply() = new Searcher(INDEX_PATH)
  def apply(path : String) = new Searcher(path)

}

class Searcher(indexPath : String) {
  val analyzer  = new CJKAnalyzer(Version.LUCENE_31)
  val formatter = new SimpleHTMLFormatter("]]><strong>","</strong><![CDATA[")

  private def searchByQuery(query : Query, field : String) : Array[Document] =
    using( FSDirectory.open(new File(indexPath)) ) { case dir =>
      using( new IndexSearcher(dir, true) ) { case searcher => {
	    val scorer = new QueryScorer(query, field)
	    val hightlighter = new Highlighter(formatter, scorer)
	    val td : TopDocs = searcher.search(query, 1000)
	    for {
	      scoreDoc    <- td.scoreDocs
	      val doc     = searcher.doc(scoreDoc.doc)
	      val content = doc.getField("content").stringValue()
	      val high    = hightlighter.getBestFragment(analyzer, field, content)
	    } yield Document(scoreDoc.doc, doc,"<pre><![CDATA[" + high + "]]></pre>")
      } } }

  def search(query : String, field : String) : Array[Document] = {
    val parser : QueryParser = new QueryParser(Version.LUCENE_31, field, analyzer)
    searchByQuery(parser.parse(query),field)
  }

  def searchByPath(path : String) : Option[Document] = {
    try {
      val term : Term = new Term("path", path)
      val query : Query = new TermQuery(term)
      searchByQuery(query, "path") match {
        case Array(x) =>
	  Some(x)
        case _ =>
	  None
      }
    } catch { case _ => None }
  }
}

