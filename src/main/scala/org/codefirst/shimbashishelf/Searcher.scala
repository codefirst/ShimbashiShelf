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

object Searcher {
  val INDEX_PATH : String  = "index"

    def search(query : String) : Array[Document] = {
      val dir : Directory = FSDirectory.open(new File(INDEX_PATH))
      val searcher : IndexSearcher = new IndexSearcher(dir, true)
      val parser : QueryParser = new QueryParser(Version.LUCENE_31, "document", new CJKAnalyzer(Version.LUCENE_31))
      val td : TopDocs = searcher.search(parser.parse(query), 1000)
      val docs = td.scoreDocs.map ((scoreDoc) => searcher.doc(scoreDoc.doc))
      searcher.close()
      dir.close()
      return docs
    }

    def main(args : Array[String]) {
      val documents : Array[Document] = Searcher.search(args(0))
      println(documents.length + " documents found:")
      documents.map ((doc) => println(doc.getField("path").stringValue()))
    }
}
