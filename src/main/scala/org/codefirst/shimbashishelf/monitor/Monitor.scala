package org.codefirst.shimbashishelf.monitor
import org.codefirst.shimbashishelf.vcs.VersionControl
import org.codefirst.shimbashishelf.search.Indexer
import java.io.File

class Monitor(indexer : Indexer, vc : VersionControl) {
  private val config = org.codefirst.shimbashishelf.common.Config.default
  private val globs  = config.ignoreFiles.map(Glob(_))

  private def isWatch(file : File) : Boolean =
    !globs.exists(_.isMatch(file))


  private def update(file : File){
    println("update: " + file.getAbsolutePath())
    vc.commit(file)
    indexer.index(file)
  }

  private def delete(file : File){
    println("delete: " + file)
    vc.remove(file)
    indexer.delete(file)
  }

  def start(file : File){
    Fam.watch(file) {
      case OnFileCreate(file) if isWatch(file) => update(file)
      case OnFileChange(file) if isWatch(file) => update(file)
      case OnFileDelete(file) if isWatch(file) => delete(file)
      case _ => ()
    }
  }
}
