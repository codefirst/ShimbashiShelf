package org.codefirst.shimbashishelf.web

import unfiltered.response.{Ok, NotFound}
import org.apache.commons.codec.binary.Base64

import org.codefirst.shimbashishelf.util.SCalendar
import org.codefirst.shimbashishelf.util.Base._
import org.codefirst.shimbashishelf.filesystem.{File, FileSystem, Directory}

case class View[A,B](context : Context[A,B], path : List[String]) {
  def dir[A,B](context : Context[A,B], dir : Directory ) =
      Ok ~> context.render( "view-dir.scaml",
                           ("files", dir.children), ("dir",dir))

  def file[A,B](context : Context[A,B], file : File) = {
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
      Ok ~> context.render( "view-file.scaml", ("file", file), ("body", body) )
  }

  def response = {
    ( path, FileSystem(path.mkString("/")) ) match {
      case ( List(), _ ) =>
        dir(context, FileSystem.root)
      case ( _, Some(x@Directory(_,_)) ) =>
        dir(context, x)
      case ( _, Some(x@File(_,_)) ) =>
        file(context, x)
      case _ =>
        NotFound ~> context.render( "404.scaml" )
    }
  }
}
