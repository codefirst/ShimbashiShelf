package org.codefirst.shimbashishelf.vcs

import org.eclipse.jgit.api._
import org.eclipse.jgit.lib._
import org.eclipse.jgit.revwalk._
import org.eclipse.jgit.revwalk.filter._
import org.eclipse.jgit.treewalk._
import org.eclipse.jgit.treewalk.filter._

import java.io.File
import java.util._
import collection.JavaConversions._
import scala.collection.mutable._


class RecursiveTreeWalk(repository : Repository) extends TreeWalk(repository.newObjectReader()){
  setRecursive(true)

  def getCommits(commit : RevCommit) : Commit[String] = {
    reset(commit.getTree())
    if (commit.getParentCount() >= 1) {
      setFilter(TreeFilter.ANY_DIFF)
      addTree(commit.getParent(0).getTree())
    } else {
      setFilter(TreeFilter.ALL)
    }
    var files : ListBuffer[String] = new ListBuffer()
    while (next()) {
      files.add(getPathString())
    }
    val cal = Calendar.getInstance()
    cal.setTimeInMillis(commit.getCommitTime().asInstanceOf[Long] * 1000)
    new Commit[String](commit.getName(), commit.getAuthorIdent().getName(),
                       commit.getAuthorIdent().getEmailAddress(), cal.getTime(), files.toList)
  }


}
