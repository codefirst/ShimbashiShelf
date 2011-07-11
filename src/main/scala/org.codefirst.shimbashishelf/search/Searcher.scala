package org.codefirst.shimbashishelf.search

import java.io.{File => JFile}
import org.apache.lucene.document.{Document => LDocument}
import org.apache.lucene.search.highlight._
import org.apache.lucene.analysis.cjk.CJKAnalyzer
import org.apache.lucene.store.Directory
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version
import org.apache.lucene.index.Term
import org.apache.lucene.search._
import org.apache.lucene.queryParser._
import net.reduls.gomoku.Tagger
import net.reduls.gomoku.analysis.ipadic.IpadicAnalyzer
import scala.xml.{Node, XML}
import org.codefirst.shimbashishelf.util.Base._
import org.codefirst.shimbashishelf.filesystem.{File, Metadata}

object Searcher{
  def apply() = new Searcher(INDEX_PATH)
  def apply(path : JFile) = new Searcher(path.getPath())

  def toQuery(file : JFile) : Query = {
    val term : Term = new Term("path", file.getPath)
    new TermQuery(term)
  }
}

class Searcher(indexPath : String) {
  val analyzer = new IpadicAnalyzer(new Tagger())
  val formatter = new SimpleHTMLFormatter("]]><strong>","</strong><![CDATA[")
  val parser : QueryParser = new MultiFieldQueryParser(Version.LUCENE_31,
                                                       Array("content","file_path"),
                                                       analyzer)

  private def prefix(s : String, n : Int) : String =
    s.substring(0, scala.math.min(s.length, n))

  private def field(doc : LDocument, key : String, default : String = "") = {
    val field = doc.getField(key)
    if(field eq null)
      default
    else {
      field.stringValue()
    }
  }

  private def file(doc : LDocument) : File =
    File(new JFile(field(doc, "path")),
         Some(Metadata(
           mimeType = field(doc, "mimeType"),
           content  = field(doc, "content"),
           tags     = field(doc, "tags").split(",").toList,
           attrs    = Map("manageID" -> field(doc, "manageID")))))

  private[search] def searchDocuments(query : Query) : Seq[(LDocument, Node)] =
    using( FSDirectory.open(new JFile(indexPath)) ) { case dir =>
      using( new IndexSearcher(dir, true) ) { case searcher => {
        val scorer = new QueryScorer(query, "content")
        val hightlighter = new Highlighter(formatter, scorer)
        val td : TopDocs = searcher.search(query, 1000)
        for {
          scoreDoc    <- td.scoreDocs
          val doc      = searcher.doc(scoreDoc.doc)
          val content  = doc.getField("content").stringValue()
          val fragment = hightlighter.getBestFragment(analyzer, "content",content)
          val high     = notNull(fragment, prefix(content,100))
        } yield (doc, XML.loadString("<pre><![CDATA[" + high + "]]></pre>"))
      } } }

  private def search(query : Query) : Seq[(File, Node)] =
    searchDocuments(query).map({case (doc, high) => (file(doc), high)})

  def searchByQuery(query : String) : Seq[(File, scala.xml.Node)] = {
    if (query == null || query.trim().length() == 0)
      Seq()
    else
      sure( search(parser.parse(query)) ) getOrElse Seq()
  }

  def searchByPath(path : String) : Option[File] =
    safe {
      search(Searcher.toQuery(new JFile(path))).headOption.map(_._1)
    }
}
