package org.codefirst.shimbashishelf.search.extractor

import org.apache.pdfbox.util._
import org.apache.pdfbox.pdfparser._
import org.apache.pdfbox.pdmodel._
import java.io.{InputStream,FileInputStream}

object PdfExtractor extends ExtensionExtractor {
  def extensions = List("pdf")
  def extract(in : InputStream) = {
    val pdfParser : PDFParser = new PDFParser(in)
    pdfParser.parse()
    val doc = pdfParser.getPDDocument()
    try {
      Some(new PDFTextStripper().getText(doc))
    } finally{
      doc.close()
    }
  }
}

