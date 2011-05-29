package org.codefirst.shimbashishelf.monitor

import scala.actors._
import scala.actors.Actor._
import java.io.File
import org.codefirst.shimbashishelf.common
import org.codefirst.shimbashishelf.search.Indexer
import org.codefirst.shimbashishelf.common._
import org.codefirst.shimbashishelf.vcs.VersionControl

object RepositoryMonitor {
  case object Reset
  case object Start

  type t = {
    def start() : Unit
    def stop() : Unit
  }

  private def monitorThread = {
    val repository = tee { new File(Config.default.repository) } { _.mkdirs() }
    println("start monitoring[%s]...".format(repository))
    val vc      = new VersionControl(repository)
    val indexer = Indexer()
    tee { new Monitor(indexer, vc).thread(repository) } { _.start() }
  }

  private val monitorActor = actor {
    var t : t = null
    loop {
      react {
        case Reset => {
          t.stop()
          t = monitorThread
        }
        case Start =>
          t = monitorThread
      }
    }
  }

  def init() {
    monitorActor ! Start
  }

  def restart() {
    monitorActor ! Reset
  }
}
