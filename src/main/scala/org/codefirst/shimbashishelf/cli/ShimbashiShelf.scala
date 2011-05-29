package org.codefirst.shimbashishelf.cli

import java.io.File
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import java.text.DateFormat
import org.codefirst.shimbashishelf.search._
import org.codefirst.shimbashishelf.monitor._
import org.codefirst.shimbashishelf.vcs._
import org.codefirst.shimbashishelf.common.Config

object ShimbashiShelf {
  def repository = {
    val f = new File(Config.default.repository)
    f.mkdirs()
    f
  }

  val commands : Array[String] = Array("index", "search", "index-all", "search-by-path", "commit", "history","monitor")
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
          val documents : Array[Document] = Searcher().search(args(0))
          println(documents.length + " documents found:")
          for(doc <- documents){
            println("[%s] %s".format(doc.manageID, doc.path))
            println(doc.highlight)
          }
        }
      case "search-by-path"::args =>
        if (args.length < 1) {
          println("usage: search-by-path <path>")
        } else {
          Searcher().searchByPath(args(0)) match {
            case Some(doc) =>
              println("[%s] %s".format(doc.manageID, doc.path))
            case None =>
              println("not found")
          }
        }
      case "index"::args =>
        if (args.length < 1) {
          println("usage: index <filepath>")
        } else {
          val file : File = new File(args(0))
          Indexer().index(file)
        }
      case "index-all"::args =>
        if (args.length < 1) {
          println("usage: index-all <directory-path>")
        } else {
          val dir : File = new File(args(0))
          for(file <- Indexer.allFiles(dir)){
            println(file)
            Indexer().index(file)
          }
        }
      case "commit"::args =>
        if (args.length < 1) {
          println("usage: commit <filepath>")
        } else {
          val vc : VersionControl = new VersionControl(repository)
          if (vc.commit(new File(args(0)))) {
            println("commit successful")
          } else {
            println("commit failure")
          }
        }
      case "history"::args =>
        val vc : VersionControl = new VersionControl(repository)
      var commits : List[FileDiffCommit] = null
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
        println("hash    : " + commit.hash)
        println("author  : " + commit.author)
        println("email   : " + commit.email)
        println("date    : " + commitDateFormat.format(commit.date))
        println("modified: ")
        commit.files.foreach { file => println("   " + file) }
        println()
      }

      case "monitor"::_ => {
        val vc = new VersionControl(repository)
        val indexer = Indexer()
        println("start monitoring[%s]...".format(repository))
        new Monitor(indexer, vc).start(repository)
      }
      case _ => {
        println("unknown command")
        println("available commands:")
        commands.foreach((command) => println("   " + command))
      }
    }
  }
}

