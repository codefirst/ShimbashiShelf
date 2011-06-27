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
import net.reduls.igo.Tagger
import net.reduls.igo.analysis.ipadic.IpadicAnalyzer
import scala.xml.{Node, XML}
import org.codefirst.shimbashishelf.util.Base._
import org.codefirst.shimbashishelf.filesystem.{File, Metadata}

object Searcher{
  def apply() = new Searcher(INDEX_PATH)
  def apply(path : JFile) = new Searcher(path.getPath())
}

class Searcher(indexPath : String) {
  val analyzer = new IpadicAnalyzer(new Tagger("ipadic"));
  val formatter = new SimpleHTMLFormatter("]]><strong>","</strong><![CDATA[")
  val parser : QueryParser = new MultiFieldQueryParser(Version.LUCENE_31,
                                                       Array("content","file_path"),
                                                       analyzer)

  private def prefix(s : String, n : Int) : String =
    s.substring(0, scala.math.min(s.length, n))

  private def field(doc : LDocument, key : String) =
    doc.getField(key).stringValue()

  private def file(id : String, doc : LDocument) : File =
    File(new JFile(field(doc, "path")),
         Some(Metadata(
           mimeType = field(doc, "mimeType"),
           content  = field(doc, "content"),
           attrs    = Map("manageID" -> field(doc, "manageID")))))

  private def search(query : Query) : Seq[(File, Node)] =
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
        } yield (file(scoreDoc.doc.toString, doc), XML.loadString("<pre><![CDATA[" + high + "]]></pre>"))
      } } }

  def searchByQuery(query : String) : Seq[(File, scala.xml.Node)] = {
    if (query == null || query.trim().length() == 0)
      Seq()
    else
      sure( search(parser.parse(query)) ) getOrElse Seq()
  }

  def searchByPath(path : String) : Option[File] =
    safe {
      val term : Term = new Term("path", path)
      val query : Query = new TermQuery(term)
      search(query).headOption.map(_._1)
    }

  def searchByID(id : String) : Option[File] =
    safe {
      using( FSDirectory.open(new JFile(indexPath)) ) { case dir =>
        using( new IndexSearcher(dir, true) ) { case searcher => {
          Some(file(id, searcher.doc(id.toInt)))
        } } } }
}
