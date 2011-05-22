package org.codefirst.shimbashishelf.web.snippet

import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.RequestVar
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import net.liftweb.http._
import Helpers._

import org.codefirst.shimbashishelf.common.{Config => C}

class Config {
  def render(xhtml : NodeSeq) : NodeSeq = {
    val config = C.default
    def doSave() {
      config.save()
    }
    def updateIgnoreFiles(s : String) {
      config.ignoreFiles = s.split("\n").toList
    }

    bind("config", xhtml,
         "ignoreFiles" -> textarea(config.ignoreFiles.mkString("\n"),
                                   updateIgnoreFiles),
         "submit" -> SHtml.submit(S.?("Save"), doSave))
  }
}
