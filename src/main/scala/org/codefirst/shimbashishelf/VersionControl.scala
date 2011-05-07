package org.codefirst.shimbashishelf

import org.eclipse.jgit.api._
import org.eclipse.jgit.lib._
import org.eclipse.jgit.revwalk._
import org.eclipse.jgit.revwalk.filter._
import org.eclipse.jgit.treewalk._
import org.eclipse.jgit.treewalk.filter._

import java.io.File
import java.util._
import java.text.SimpleDateFormat

import collection.JavaConversions._
import scala.collection.mutable._

class Commit(hash : String, author : String, email : String, date : Date, files : scala.collection.immutable.List[String]) {
  def getHash() = hash
  def getAuthor() = author
  def getEmailAddress() = email
  def getDate() = date
  def getFiles() = files
}

class VersionControl(repositoryDir : File) {

  val repository : Repository = new RepositoryBuilder()
    .setGitDir(new File(repositoryDir.getAbsolutePath() + File.separatorChar + Constants.DOT_GIT)).readEnvironment().findGitDir().build()
  val git : Git = new Git(repository)


  def commit(file : File) : Boolean = {
    if (!new File(repositoryDir.getAbsolutePath() + File.separatorChar + Constants.DOT_GIT).exists()) {
      println(new File(repositoryDir.getAbsolutePath() + File.separatorChar + Constants.DOT_GIT).getAbsolutePath() + " created")
      Git.init().setDirectory(repositoryDir).call()
    }

    // TODO: getIndex() is deprecated, but AddCommand does not work... 
    val index = repository.getIndex()
    index.add(repositoryDir, file)
    index.write()

    val format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    // TODO: Author configuration 
    val cal = Calendar.getInstance()
    git.commit().setAuthor("ShimbashiShelf", "ShimbashiShelf@codefirst.org").setMessage(format.format(cal.getTime())).call()
    return true
  }

  def commitList(startDate : Date, endDate : Date) : scala.collection.immutable.List[Commit] = {
    val tw = new NameConflictTreeWalk(repository.newObjectReader())
    tw.setRecursive(true)

    val revWalk : RevWalk = new RevWalk(repository)
    (startDate, endDate) match {
      case (null, null) => revWalk.setRevFilter(RevFilter.ALL)
      case (null, _) => revWalk.setRevFilter(CommitTimeRevFilter.before(endDate))
      case (_, null) => revWalk.setRevFilter(CommitTimeRevFilter.after(endDate))
      case (_, _) => revWalk.setRevFilter(CommitTimeRevFilter.between(startDate, endDate))
    }
    revWalk.markStart(revWalk.parseCommit(repository.resolve("master")))
    var commits : ListBuffer[Commit] = new ListBuffer()
    for (commit <- revWalk) {
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
      commits.add(new Commit(commit.getName(), commit.getAuthorIdent().getName(), 
                             commit.getAuthorIdent().getEmailAddress(), cal.getTime(), files.toList))
    }
    revWalk.dispose()
    commits.toList
  }
}
