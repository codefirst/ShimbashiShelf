package org.codefirst.shimbashishelf.search.extractor

import org.codefirst.shimbashishelf.util.FileUtil
import org.mozilla.intl.chardet._
import org.mozilla.universalchardet.UniversalDetector

object PlainTextExtractor extends Extractor {

  def detect( xs : Array[Byte]) : String = {
    val detector = new UniversalDetector(null)
    detector.handleData( xs, 0, xs.length)
    detector.dataEnd()
    val charset = detector.getDetectedCharset
    if(charset == null) "Ascii" else charset
  }

  def extract(fileName : String) = {
    for {
      raw <- FileUtil.readArray(fileName)
      encoding = detect(raw)
    } yield new String(raw, encoding)
  }
}
