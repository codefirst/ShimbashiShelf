package org.codefirst.shimbashishelf.filesystem
import java.io.{File => JFile}
import java.util.Date
import org.codefirst.shimbashishelf.common.Config
import org.codefirst.shimbashishelf.util.FileUtil
import org.codefirst.shimbashishelf.util.Base._
import org.codefirst.shimbashishelf.search.Searcher
import org.codefirst.shimbashishelf.vcs.{VersionControl,Commit}

sealed abstract class FileObject {
  def id : String
  def name : String
}

case class File(
  id       : String,
  mimeType : String,
  path     : String,
  content  : String,
  attributes : Map[String, String]
) extends FileObject {
  def manageID = attributes.getOrElse("manageID", "-")
  def name = FileUtil.basename(path)
}

case class Directory(id : String, name : String) extends FileObject

object FileSystem {
  def read(file : FileObject) : Option[Array[Byte]] =
    file match {
      case File(_,_,path,_,_) =>
        FileUtil.readArray(path)
      case Directory(_,_) =>
        None
    }

  private def searcher = Searcher()
  def apply(id : String) : Option[FileObject] =
    searcher.searchByID(id)

  def searchByQuery(query : String) =
    searcher.searchByQuery(query)

  private def vcs =
    new VersionControl(new JFile( Config.default.repository ))

  def commitList(from : Option[Date], to : Option[Date]) : Seq[Commit[File]] =
    vcs.commitList(from, to).map(commit =>
      commit.copy(files = commit.files.flatMap{file =>
        val path = new JFile(Config.default.repository + "/" + file)
        searcher.searchByPath(path.getAbsolutePath())
                                               }))
}
