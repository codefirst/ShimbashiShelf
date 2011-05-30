package org.codefirst.shimbashishelf.monitor
import org.codefirst.shimbashishelf.vcs.VersionControl
import org.codefirst.shimbashishelf.search.Indexer
import java.io.File
import org.apache.log4j.Logger

class Monitor(indexer : Indexer, vc : VersionControl) {
  private val logger = Logger.getLogger(classOf[Monitor])
  private val config = org.codefirst.shimbashishelf.common.Config.default
  private val globs  = config.ignoreFiles.map(Glob(_))

  private def isWatch(file : File) : Boolean =
    !globs.exists(_.isMatch(file))

  private def update(file : File){
    logger.info("update: " + file.getAbsolutePath())
    vc.commit(file)
    indexer.index(file)
  }

  private def delete(file : File){
    logger.info("delete: " + file)
    vc.remove(file)
    indexer.delete(file)
  }

  def start(file : File){
    thread(file).run()
  }

  def thread(file : File) =
    Fam.thread(file) {
      case OnFileCreate(file) if isWatch(file) => update(file)
      case OnFileChange(file) if isWatch(file) => update(file)
      case OnFileDelete(file) if isWatch(file) => delete(file)
      case _ => ()
    }
}
