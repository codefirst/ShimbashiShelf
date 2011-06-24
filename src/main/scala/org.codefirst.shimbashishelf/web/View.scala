package org.codefirst.shimbashishelf.web

import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._
import org.apache.commons.codec.binary.Base64

import org.codefirst.shimbashishelf.util.SCalendar
import org.codefirst.shimbashishelf.util.Base._
import org.codefirst.shimbashishelf.vcs.Commit
import org.codefirst.shimbashishelf.filesystem.{File, FileSystem, Directory}

object View {
  def dir[A](req : HttpRequest[A], dir : Directory) =
      Ok ~> Scalate(req, "view-dir.scaml", ("files", dir.children),("dir",dir))

  def file[A](req : HttpRequest[A], file : File) = {
    val metadata =
      file.metadata
    val body : scala.xml.Node =
      if ( metadata.mimeType startsWith "image" ) {
        file.read match {
          case Some(xs) =>
            val base64 = Base64.encodeBase64String( xs )
            <div><img src={"data:%s;base64,%s".format( metadata.mimeType, base64 )} /></div>
          case None =>
            <div class="error no-image">no image</div>
        }
      } else
        <pre>{metadata.content}</pre>
     Scalate(req, "view-file.scaml", ("file", file), ("body", body))
  }

  def apply[A](req : HttpRequest[A], path : List[String]) = {
    ( path, FileSystem(path.mkString("/")) ) match {
      case ( List(), _ ) =>
        dir(req, FileSystem.root)
      case ( _, Some(x@Directory(_,_)) ) =>
        dir(req, x)
      case ( _, Some(x@File(_,_)) ) =>
        file(req, x)
      case _ =>
        NotFound ~> Scalate(req, "404.scaml")
    }
  }
}
