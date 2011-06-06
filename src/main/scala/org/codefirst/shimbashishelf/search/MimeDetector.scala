package org.codefirst.shimbashishelf.search
import eu.medsea.mimeutil.MimeUtil2
import scala.collection.JavaConverters._

object MimeDetector{
  private val detector = new MimeUtil2
  detector.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector")
  detector.registerMimeDetector("eu.medsea.mimeutil.detector.ExtensionMimeDetector")

  def apply(file : java.io.File) : String = {
    val xs = detector.getMimeTypes(file).asScala.toList
    xs(0).toString()
  }
}
