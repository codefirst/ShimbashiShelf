package org.codefirst.shimbashishelf

import org.apache.lucene.document.Document
import java.io.File
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import java.text.DateFormat

object ShimbashiShelf {
  val INDEX_PATH : String  = "index"
  val commands : Array[String] = Array("index", "search", "index-all", "search-by-path", "commit", "history")
  def main(args : Array[String]) {
    args.toList match {
      case List() => {
	    println("no arguments found")
	    println("available commands:")
	    commands.foreach((command) => println("   " + command))
      }
      case "search"::args =>
	    if(args.length < 1) {
          println("usage: search <word>")
	    } else {
	      val documents : Array[Document] = Searcher.search(args(0), "content")
	      println(documents.length + " documents found:")
	      documents.foreach ((doc) => println(doc.getField("path").stringValue()))
	    }
      case "search-by-path"::args =>
	    if (args.length < 1) {
          println("usage: search-by-path <path>")
	    } else try {
          val document : Document = Searcher.searchByPath(args(0))
          println("found:")
          println(document.getField("path").stringValue())
	    } catch {
          case e : Exception => e.printStackTrace()
	    }
      case "index"::args =>
	    if (args.length < 1) {
          println("usage: index <filepath>")
	    } else {
	      val file : File = new File(args(0))
	      if (Indexer.index(file)) {
            println("index successful")
	      } else {
            println("index failed")
	      }
	    }
      case "index-all"::args =>
	    if (args.length < 1) {
          println("usage: index-all <directory-path>")
	    } else {
	      val dir : File = new File(args(0))
	      if (Indexer.indexRecursively(dir)) {
            println("index successful")
	      } else {
            println("index failed")
	      }
	    }
      case "commit"::args =>
	    if (args.length < 1) {
          println("usage: commit <filepath>")
	    } else {
	      val vc : VersionControl = new VersionControl(new File("files"))
	      if (vc.commit(new File(args(0)))) {
            println("commit successful")
	      } else {
            println("commit failure")
	      }
	    }
      case "history"::args =>
	    val vc : VersionControl = new VersionControl(new File("files"))
        var commits : List[Commit] = null
        val format = new SimpleDateFormat("yyyy-MM-dd")
        if (args.length >= 2) { 
          val cal = Calendar.getInstance()
          val startDate = format.parse(args(0))
          val endDate = format.parse(args(1))
          cal.setTimeInMillis(endDate.getTime())
          cal.add(Calendar.DATE, 1)
          cal.add(Calendar.MILLISECOND, -1)
          commits = vc.commitList(Some(startDate), Some(cal.getTime()))
        } else if (args.length == 1) { 
          val startDate = format.parse(args(0))
          commits = vc.commitList(Some(startDate), None)
        } else { 
          commits = vc.commitList(None, None)
        }
      
        val commitDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val cal = Calendar.getInstance()

        for (commit <- commits) { 
          println("hash    : " + commit.getHash())
          println("author  : " + commit.getAuthor())
          println("email   : " + commit.getEmailAddress())
          println("date    : " + commitDateFormat.format(commit.getDate()))
          println("modified: ")
          commit.getFiles().foreach { file => println("   " + file) }
          println()
        }

      case _ => {
	    println("unknown command")
	    println("available commands:")
	    commands.foreach((command) => println("   " + command))
      }
    }
  }
}
