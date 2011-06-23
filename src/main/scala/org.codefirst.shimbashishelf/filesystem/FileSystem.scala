package org.codefirst.shimbashishelf.filesystem
import java.io.{File => JFile}
import java.util.Date
import org.codefirst.shimbashishelf.common.Config
import org.codefirst.shimbashishelf.util.FileUtil
import org.codefirst.shimbashishelf.common.Home
import org.codefirst.shimbashishelf.search.Searcher
import org.codefirst.shimbashishelf.vcs.{VersionControl,Commit}

case class Metadata(mimeType : String, content : String, attrs : Map[String, String]){
  def manageID = attrs.getOrElse("manageID", "-")
}

sealed abstract class FileObject {
  def path     : String
  def initMetadata : Option[Metadata]

  def name : String =
    FileUtil.basename( path )

  def metadata : Metadata =
    initMetadata getOrElse {
      FileSystem.metadata(path) getOrElse {
        Metadata( "application/octet-stream", "", Map() )
      }
    }
}

case class File(path : String, initMetadata : Option[Metadata]) extends FileObject {
  def read : Option[Array[Byte]] = {
    FileUtil.readArray(path)
  }
}

case class Directory(path : String, initMetadata : Option[Metadata]) extends FileObject {
  def children : Iterable[FileObject] = {
    val files = new JFile( path ).listFiles()
    files.flatMap( FileSystem.fromJFile(_) )
  }
}

object FileSystem {
  private def searcher = Searcher()
  def apply(id : String) : Option[FileObject] =
    searcher.searchByID(id)

  def searchByQuery(query : String) : Seq[( File, scala.xml.Node )] = {
    searcher.searchByQuery(query)
  }

  def searchByPath(path : String) : Option[FileObject] = {
    searcher.searchByPath(path)
  }

  def metadata(path : String) : Option[Metadata] = {
    searchByPath(path).map(_.metadata)
  }

  private def vcs =
    new VersionControl(new JFile( Config.default.repository ))

  def commitList(from : Option[Date], to : Option[Date]) : Iterable[Commit[FileObject]] =
    vcs.commitList(from, to).toIterable.map(commit =>
      commit.copy(files = commit.files.flatMap{file =>
        (searcher.searchByPath(file) : Option[FileObject])
    }))

  def fromJFile(file : JFile, metadata : Option[Metadata] = None) : Option[FileObject] = {
    if ( ! file.exists() )
      None
    else if ( file.isDirectory() )
      Some( Directory( file.getPath(), metadata ) )
    else
      Some( File( file.getPath(), metadata ) )
  }

  def root : Directory =
    fromJFile( Home.dir("git") ).get match {
      case dir@Directory(_,_) => dir
      case _ => throw new RuntimeException("must not happen")
    }
}
