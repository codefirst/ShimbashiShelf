package org.codefirst.shimbashishelf.search.extractor

import org.apache.pdfbox.util._
import org.apache.pdfbox.pdfparser._
import org.apache.pdfbox.pdmodel._
import org.codefirst.shimbashishelf.util.FileUtil
import java.io.{InputStream,FileInputStream}

object PdfExtractor extends Extractor {
  def extract(fileName : String) = {
    if(FileUtil.getExtension(fileName) == "pdf") {
      val in : InputStream = new FileInputStream(fileName)
      val pdfParser : PDFParser = new PDFParser(in)
      pdfParser.parse()
      Some(new PDFTextStripper().getText(pdfParser.getPDDocument()))
    } else
      None
  }
}

