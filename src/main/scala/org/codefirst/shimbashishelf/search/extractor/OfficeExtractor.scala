package org.codefirst.shimbashishelf.search.extractor

import org.apache.poi.hwpf.extractor._
import org.apache.poi.hssf.extractor._
import org.apache.poi.hssf.usermodel._
import org.apache.poi.hslf.extractor._
import org.apache.poi.xwpf.extractor._
import org.apache.poi.xslf.extractor._
import org.apache.poi.xssf.extractor._
import org.apache.poi.extractor._
import org.codefirst.shimbashishelf.util.FileUtil

import java.io.{InputStream,FileInputStream}

object OfficeExtractor extends ExtensionExtractor {
  val map = Map("doc"  -> "application/msword",
                "docx" -> "application/msword",
                "ppt"  -> "application/mspowerpoint",
                "pptx" -> "application/mspowerpoint",
                "xls"  -> "application/msexcel",
                "xlsx" -> "application/msexcel")

  def extensions =
    map.keys.toList

  def mimeType(key : String) =
    map(key)

  def extract(in : InputStream) =
    Some(ExtractorFactory.createExtractor(in).getText())

}
