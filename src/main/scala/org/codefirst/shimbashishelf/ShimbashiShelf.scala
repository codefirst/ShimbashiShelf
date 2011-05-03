package org.codefirst.shimbashishelf
import org.apache.lucene.document.Document
import java.io.File

object ShimbashiShelf {
  val INDEX_PATH : String  = "index"

  def main(args : Array[String]) {
    if (args.length == 0) { 
      println("no arguments found")
      System.exit(0)
    }
    
    if (args(0).equals("search")) { 
      if (args.length < 2) { 
        println("usage: search <word>")
        System.exit(0)
      }
      val documents : Array[Document] = Searcher.search(args(1))
      println(documents.length + " documents found:")
      documents.map ((doc) => println(doc.getField("path").stringValue()))
    } else if (args(0).equals("index")) { 
      if (args.length < 2) { 
        println("usage: index <filepath>")
        System.exit(0)
      }
      val file : File = new File(args(1))
      if (Indexer.index(file)) { 
        "index successful"
      } else { 
        "index failed"
      }
    }
  }
}
