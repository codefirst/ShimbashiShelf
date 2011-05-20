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

object OfficeExtractor extends Extractor {
  val extensions = List("doc", "docx", "ppt", "pptx", "xls", "xlsx")

  def extract(fileName : String) = {
    val ext = FileUtil.getExtension(fileName)
    if(extensions contains ext) {
      val in : InputStream = new FileInputStream(fileName)
      Some(ExtractorFactory.createExtractor(in).getText())
    } else
      None
  }
}
