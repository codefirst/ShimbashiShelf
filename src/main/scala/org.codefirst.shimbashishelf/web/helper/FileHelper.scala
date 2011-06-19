package org.codefirst.shimbashishelf.web.helper
import org.codefirst.shimbashishelf.filesystem.File
import _root_.net.liftweb.util._
import Helpers._
object FileHelper {
  def asBind(file : File) : List[BindParam]=
    List("path"      -> file.path,
         "id"        -> file.id,
         "content"   -> file.content,
         "filename"  -> file.name,
         "mimeType"  -> file.mimeType,
         "manageID"  -> file.manageID)
}
