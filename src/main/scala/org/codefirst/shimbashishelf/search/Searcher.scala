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
  val parser : QueryParser = new MultiFieldQueryParser(Version.LUCENE_31,
                                                       Array("content","file_path"),
                                                       analyzer)

  private def prefix(s : String, n : Int) : String =
    s.substring(0, scala.math.min(s.length, n))
  private def searchByQuery(query : Query) : Array[Document] =
    using( FSDirectory.open(new File(indexPath)) ) { case dir =>
      using( new IndexSearcher(dir, true) ) { case searcher => {
	    val scorer = new QueryScorer(query, "content")
	    val hightlighter = new Highlighter(formatter, scorer)
	    val td : TopDocs = searcher.search(query, 1000)
	    for {
	      scoreDoc    <- td.scoreDocs
	      val doc     = searcher.doc(scoreDoc.doc)
	      val content = doc.getField("content").stringValue()
	      val fragment = hightlighter.getBestFragment(analyzer, "content", content)
              val high = if(fragment eq null) prefix(content,100) else fragment
	    } yield Document(scoreDoc.doc, doc,"<pre><![CDATA[" + high + "]]></pre>")
      } } }

  def search(query : String) : Array[Document] = {
    if (query == null || query.trim().length() == 0)
      Array()
    else
      searchByQuery(parser.parse(query))
  }

  def searchByPath(path : String) : Option[Document] = {
    try {
      val term : Term = new Term("path", path)
      val query : Query = new TermQuery(term)
      searchByQuery(query) match {
        case Array(x) => Some(x)
        case _ => None
      }
    } catch { case _=>
      None
    }
  }
}

