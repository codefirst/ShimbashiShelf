package org.codefirst.shimbashishelf.web.snippet

import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.RequestVar
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import net.liftweb.http._
import Helpers._

import org.codefirst.shimbashishelf.filesystem._
import net.liftweb.util.BindPlus._
import org.apache.commons.codec.binary.Base64
import org.codefirst.shimbashishelf.web.helper.FileHelper

class Show {
  val id = S.param("id").openOr("0")
  lazy val file : Box[FileObject] = Box(FileSystem(id))

  def bind(xhtml : NodeSeq, file : File) =
    xhtml.
      bind("doc", FileHelper.asBind(file) : _* ).
      bind("result",
           "link" -> { (x:NodeSeq) =>
             <a class="download" href={"/download/" + file.id}>{x}</a>},
           "pathfield" ->
             <input type="text" class="info" value={file.path} onmousedown="this.select();"/>)

  def render(xhtml : NodeSeq) : NodeSeq =
    file match {
      case Full(Directory()) => xhtml
      case Full(file@File(id,mimeType,path,_,_)) if mimeType startsWith "image" =>
        FileSystem.read(file) match {
          case Some(xs) =>
            val base64 = Base64.encodeBase64String( xs )
            bind(xhtml, file).
              bind("result",
                   "content" -> <div><img src={"data:%s;base64,%s".format(mimeType, base64)} /></div>)
          case None =>
            bind(xhtml, file).
              bind("result",
                   "content" -> <div class="error no-image">no image</div>)
        }
      case Full(file@File(_,_,_,content,_)) =>
        bind(xhtml, file).
          bind("result",
               "content" -> <pre>{content}</pre>)
      case _ => xhtml
    }
}
