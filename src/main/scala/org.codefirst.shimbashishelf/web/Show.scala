package org.codefirst.shimbashishelf.web

import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._
import org.apache.commons.codec.binary.Base64

import org.codefirst.shimbashishelf.util.SCalendar
import org.codefirst.shimbashishelf.util.Base._
import org.codefirst.shimbashishelf.vcs.Commit
import org.codefirst.shimbashishelf.filesystem.{File, FileSystem}

object Show {
  def apply[A](req : HttpRequest[A], id : String) = {
    FileSystem(id) match {
      case Some(file@File(id,mimeType,_,content,_)) =>
        val body : scala.xml.Node =
          if(mimeType startsWith "image") {
            FileSystem.read(file) match {
              case Some(xs) =>
                val base64 = Base64.encodeBase64String( xs )
                <div><img src={"data:%s;base64,%s".format(mimeType, base64)} /></div>
              case None =>
                <div class="error no-image">no image</div>
            }
          } else
            <pre>{content}</pre>
        Ok ~> Scalate(req, "show.scaml",
                      ("file", file),
                      ("body", body))
      case Some(_) | None =>
        NotFound ~> Scalate(req, "404.scaml")
    }
  }
}
