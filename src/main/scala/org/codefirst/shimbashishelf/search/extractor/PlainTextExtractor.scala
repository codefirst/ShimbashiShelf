package org.codefirst.shimbashishelf.search.extractor

import org.codefirst.shimbashishelf.util.FileUtil
import org.mozilla.intl.chardet._

object PlainTextExtractor extends Extractor {

  def detect( xs : Array[Byte]) : String = {
    val detector = new nsDetector( nsPSMDetector.ALL )
    detector.DoIt( xs, xs.length, false)
    detector.DataEnd()
    val cs = detector.getProbableCharsets()
    println(cs.toList)
    if(cs.length > 0) cs(0) else "Ascii"
  }

  def extract(fileName : String) = {
    for {
      raw <- FileUtil.readArray(fileName)
      encoding = detect(raw)
    } yield new String(raw, encoding)
  }
}
