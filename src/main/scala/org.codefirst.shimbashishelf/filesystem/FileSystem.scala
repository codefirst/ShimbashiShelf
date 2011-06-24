package org.codefirst.shimbashishelf.filesystem
import java.io.{File => JFile}
import java.util.Date
import org.codefirst.shimbashishelf.common.Config
import org.codefirst.shimbashishelf.util.FileUtil
import org.codefirst.shimbashishelf.common.Home
import org.codefirst.shimbashishelf.search.{Searcher, Indexer}
import org.codefirst.shimbashishelf.vcs.{VersionControl,Commit}

case class Metadata(mimeType : String, content : String, attrs : Map[String, String]){
  def manageID = attrs.getOrElse("manageID", "-")
}

sealed abstract class FileObject {
  def file : JFile
  def initMetadata : Option[Metadata]

  def url : String = {
    val path =file.
      getPath().
      replace( JFile.pathSeparatorChar, '/' ).
      replaceFirst( Home.dir("git").getPath() + "/?", "" )
    "/" + path
  }

  def name : String =
    file.getName()

  def metadata : Metadata =
    initMetadata getOrElse {
      FileSystem.metadata(file) getOrElse {
        Metadata( "application/octet-stream", "", Map() )
      }
    }
}

case class File(file : JFile, initMetadata : Option[Metadata]) extends FileObject {
  def read : Option[Array[Byte]] = {
    FileUtil.readArray(file.getAbsolutePath())
  }
}

case class Directory(file : JFile, initMetadata : Option[Metadata]) extends FileObject {
  def children : Iterable[FileObject] = {
    file.listFiles().flatMap( FileSystem.fromJFile(_) )
  }
}

object FileSystem {
  private def searcher = Searcher()
  def apply(path : String) : Option[FileObject] =
    fromJFile(new JFile(FileUtil.join( Home.dir("git").getPath(), path )))

  def mkdir(path : String) {
    val file = new JFile(FileUtil.join( Home.dir("git").getPath(), path ))
    file.mkdirs()
  }

  def save( path : String, write : JFile => Unit ) : Option[FileObject] = {
    val file = new JFile(FileUtil.join( Home.dir("git").getPath(), path ))
    write(file)
    Indexer().index(file)
    vcs.commit(file)
    fromJFile(file)
  }

  def searchByQuery(query : String) : Seq[( File, scala.xml.Node )] = {
    searcher.searchByQuery(query)
  }

  def searchByPath(path : String) : Option[FileObject] = {
    searcher.searchByPath(path)
  }

  def metadata(file : JFile) : Option[Metadata] = {
    searchByPath(file.getPath()).map(_.metadata)
  }

  private def vcs =
    new VersionControl( Home.dir("git") )

  def commitList(from : Option[Date], to : Option[Date]) : Iterable[Commit[FileObject]] =
    vcs.commitList(from, to).toIterable.map(commit =>
      commit.copy(files = commit.files.flatMap{file =>
        (FileSystem(file) : Option[FileObject])
    }))

  def fromJFile(file : JFile, metadata : Option[Metadata] = None) : Option[FileObject] = {
    if ( ! file.exists() )
      None
    else if ( file.getName().startsWith(".") )
      None
    else if ( file.isDirectory() )
      Some( Directory( file, metadata ) )
    else
      Some( File( file, metadata ) )
  }

  def root : Directory =
    fromJFile( Home.dir("git") ).get match {
      case dir@Directory(_,_) => dir
      case _ => throw new RuntimeException("must not happen")
    }
}

