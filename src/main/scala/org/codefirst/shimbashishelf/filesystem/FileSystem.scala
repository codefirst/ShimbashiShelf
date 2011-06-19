package org.codefirst.shimbashishelf.filesystem

import org.codefirst.shimbashishelf.util.FileUtil
import org.codefirst.shimbashishelf.util.Base._
import org.codefirst.shimbashishelf.search.Searcher

sealed abstract class FileObject
case class File(
  id       : String,
  mimeType : String,
  path     : String,
  content  : String,
  attributes : Map[String, String]
) extends FileObject {
  def manageID = sure { attributes("manageID") }.getOrElse("<unknown>")
  def name = FileUtil.basename(path)

}
case class Directory() extends FileObject

object FileSystem {
  def read(doc : File) : Option[Array[Byte]] =
    FileUtil.readArray(doc.path)


  private val searcher = Searcher()
  def apply(id : String) : Option[FileObject] =
    searcher.searchByID(id)

  def searchByQuery(query : String) =
    searcher.searchByQuery(query)

//  def history()
}
