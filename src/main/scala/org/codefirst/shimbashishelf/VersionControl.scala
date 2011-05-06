package org.codefirst.shimbashishelf

import org.eclipse.jgit.api._
import org.eclipse.jgit.lib._
import org.eclipse.jgit.revwalk._
import org.eclipse.jgit.treewalk._
import org.eclipse.jgit.treewalk.filter._

import java.io.File
import java.util._
import java.text.SimpleDateFormat

import collection.JavaConversions._
import scala.collection.mutable._

class Commit(hash : String, date : Date, files : scala.collection.immutable.List[String]) {
  def getHash() = hash
  def getDate() = date
  def getFiles() = files
}

class VersionControl(repositoryDir : File) {

  val repository : Repository = new RepositoryBuilder().setGitDir(new File(repositoryDir.getAbsolutePath() + File.separatorChar + Constants.DOT_GIT)).readEnvironment().findGitDir().build()

  def commit(file : File) : Boolean = {
    if (!new File(repositoryDir.getAbsolutePath() + File.separatorChar + Constants.DOT_GIT).exists()) {
      println(new File(repositoryDir.getAbsolutePath() + File.separatorChar + Constants.DOT_GIT).getAbsolutePath() + " created")
      Git.init().setDirectory(repositoryDir).call()
    }

    val git : Git = new Git(repository)
    println(file.getPath())

    // TODO: getIndex() is deprecated, but AddCommand does not work... 
    val index = repository.getIndex()
    index.add(repositoryDir, file)
    index.write()

    val format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    // TODO: Author configuration 
    git.commit().setAuthor("ShimbashiShelf", "ShimbashiShelf@codefirst.org").setMessage(format.format(Calendar.getInstance().getTime())).call()
    return true
  }

  def commitList() : scala.collection.immutable.List[Commit] = {
    val git : Git = new Git(repository)

    val tw = new NameConflictTreeWalk(repository.newObjectReader())
    tw.setRecursive(true)

    var commits : ListBuffer[Commit] = new ListBuffer()
    for (commit <- git.log().call()) { 
      tw.reset(commit.getTree())
      if (commit.getParentCount() >= 1) { 
        tw.setFilter(TreeFilter.ANY_DIFF)
        tw.addTree(commit.getParent(0).getTree())
      } else { 
        tw.setFilter(TreeFilter.ALL)
      }
      var files : ListBuffer[String] = new ListBuffer()
      while (tw.next()) { 
        files.add(tw.getPathString())
      }
      val cal = Calendar.getInstance()
      cal.setTimeInMillis(commit.getCommitTime().asInstanceOf[Long] * 1000)
      commits.add(new Commit(commit.getName(),  cal.getTime(), files.toList))
    }
    commits.toList
  }
}
