package org.codefirst.shimbashishelf.monitor
import org.codefirst.shimbashishelf.vcs.VersionControl
import org.codefirst.shimbashishelf.search.Indexer
import java.io.File

class Monitor(vc : VersionControl) {
  private def isIgnore(file : File) : Boolean =
    ".git".r.findFirstIn(file.toString()) == None


  private def update(file : File){
    println("update: " + file)
    vc.commit(file)
    Indexer().index(file)
  }

  def start(file : File){
    Fam.watch(file) {
      case OnFileCreate(file) if !isIgnore(file) => update(file)
      case OnFileChange(file) if !isIgnore(file) => update(file)
      case _ => ()
    }
  }
}
