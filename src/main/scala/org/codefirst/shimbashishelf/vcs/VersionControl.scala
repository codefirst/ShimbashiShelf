package org.codefirst.shimbashishelf.vcs

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

class FileDiffCommit(hash : String, author : String, email : String, date : Date, files : scala.collection.immutable.List[String]) {
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
    withGit { index => index.add(repositoryDir, file)  }
    true
  }

  def remove(file : File) {
    withGit { index => index.remove(repositoryDir, file)  }
  }

  private def withGit(f : GitIndex => Unit) {
    if (!new File(repositoryDir.getAbsolutePath() + File.separatorChar + Constants.DOT_GIT).exists()) {
      println(new File(repositoryDir.getAbsolutePath() + File.separatorChar + Constants.DOT_GIT).getAbsolutePath() + " created")
      Git.init().setDirectory(repositoryDir).call()
    }

    // TODO: getIndex() is deprecated, but AddCommand does not work...
    val index = repository.getIndex()
    f(index)
    index.write()

    val format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    // TODO: Author configuration
    val cal = Calendar.getInstance()
    git.commit().setAuthor("ShimbashiShelf", "ShimbashiShelf@codefirst.org").setMessage(format.format(cal.getTime())).call()
  }



  def commitList(startDate : Option[Date], endDate : Option[Date]) : scala.collection.immutable.List[FileDiffCommit] = {
    val tw : RecursiveTreeWalk = new RecursiveTreeWalk(repository)
    new DurationRevWalk(repository, startDate, endDate).walkRevisions { case commit => { tw.getFileDiffCommits(commit) }}
  }

}

