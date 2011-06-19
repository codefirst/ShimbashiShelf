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


class DurationRevWalk(repository : Repository,  startDate : Option[Date], endDate : Option[Date]) extends RevWalk(repository) {
  (startDate, endDate) match {
    case (None, None) => setRevFilter(RevFilter.ALL)
    case (None, Some(endDate)) => setRevFilter(CommitTimeRevFilter.before(endDate))
    case (Some(startDate), None) => setRevFilter(CommitTimeRevFilter.after(startDate))
    case (Some(startDate), Some(endDate)) => setRevFilter(CommitTimeRevFilter.between(startDate, endDate))
  }
  markStart(parseCommit(repository.resolve("master")))

  def getCommits(body : RevCommit => Option[Commit[String]]) = null

  def walkRevisions(body : RevCommit => Commit[String]) : scala.collection.immutable.List[Commit[String]] =
    try { this.map(commit => body(commit)).toList } finally { dispose() }


}
