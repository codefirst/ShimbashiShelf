package org.codefirst.shimbashishelf

import org.apache.lucene.document.Document
import java.io.File

object ShimbashiShelf {
  val INDEX_PATH : String  = "index"
  val commands : Array[String] = Array("index", "search", "index-all", "search-by-path")
  def main(args : Array[String]) {
    if (args.length == 0) { 
      println("no arguments found")
      println("available commands:")
      commands.foreach((command) => println("   " + command))

      System.exit(0)
    }

    if (!commands.contains(args(0))) {
      println("unknown command")
      println("available commands:")
      commands.foreach((command) => println("   " + command))
      System.exit(0)
    }

    if (args(0).equals("search")) {
      if (args.length < 2) {
        println("usage: search <word>")
        System.exit(0)
      }
      val documents : Array[Document] = Searcher.search(args(1))
      println(documents.length + " documents found:")
      documents.foreach ((doc) => println(doc.getField("path").stringValue()))
    } else if (args(0).equals("search-by-path")) {
        if (args.length < 2) {
          println("usage: search-by-path <path>")
          System.exit(0)
        }
        val documents : Array[Document] = Searcher.search(args(1), "path")
        println(documents.length + " documents found:")
        documents.foreach ((doc) => println(doc.getField("path").stringValue()))
    } else if (args(0).equals("index")) {
      if (args.length < 2) { 
        println("usage: index <filepath>")
        System.exit(0)
      }
      val file : File = new File(args(1))
      if (Indexer.index(file)) { 
        println("index successful")
      } else { 
        println("index failed")
      }
    } else if (args(0).equals("index-all")) {
      if (args.length < 2) {
        println("usage: index-all <directory-path>")
        System.exit(0)
      }
      val dir : File = new File(args(1))
      if (Indexer.indexRecursively(dir)) {
        println("index successful")
      } else {
        println("index failed")
      }
    }
  }
}
