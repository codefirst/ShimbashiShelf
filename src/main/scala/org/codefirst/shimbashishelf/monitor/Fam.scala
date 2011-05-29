package org.codefirst.shimbashishelf.monitor
import java.io.File
import org.apache.commons.jci.monitor.FilesystemAlterationListener
import org.apache.commons.jci.monitor.FilesystemAlterationMonitor
import org.apache.commons.jci.monitor.FilesystemAlterationObserver

sealed abstract class FileEvent
case class OnStart(observer : FilesystemAlterationObserver) extends FileEvent
case class OnStop(observer : FilesystemAlterationObserver) extends FileEvent
case class OnFileCreate(file : File) extends FileEvent
case class OnFileChange(file : File) extends FileEvent
case class OnFileDelete(file : File) extends FileEvent
case class OnDirectoryCreate(file : File) extends FileEvent
case class OnDirectoryChange(file : File) extends FileEvent
case class OnDirectoryDelete(file : File) extends FileEvent

object Fam {
  def watch(file : String)(f : FileEvent => Unit) {
    watch(new File(file))(f)
  }

  def thread(file : File)(f : FileEvent => Unit) : FilesystemAlterationMonitor = {
    val monitor = new FilesystemAlterationMonitor();
    monitor.addListener(file, new FilesystemAlterationListener{
      def onStart(observer : FilesystemAlterationObserver) {
	f(OnStart(observer))
      }

      def onFileCreate(file : File) {
	f(OnFileCreate(file))
      }

      def onFileDelete(file : File) {
	f(OnFileDelete(file))
      }
      def onFileChange(file : File) {
	f(OnFileChange(file))
      }

      def onDirectoryCreate(dir : File) {
	f(OnDirectoryCreate(dir))
      }

      def onDirectoryChange(dir : File) {
	f(OnDirectoryChange(dir))
      }

      def onDirectoryDelete(dir : File) {
	f(OnDirectoryDelete(dir))
      }

      def onStop(observer : FilesystemAlterationObserver) {
	f(OnStop(observer))
      }
    });
    monitor
  }

  def watch(file : File)(f : FileEvent => Unit) {
    thread(file)(f).run()
  }
}
