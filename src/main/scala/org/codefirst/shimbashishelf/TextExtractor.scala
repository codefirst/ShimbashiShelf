package org.codefirst.shimbashishelf
import java.io._
import org.pdfbox.util._
import org.pdfbox.pdfparser._
import org.pdfbox.pdmodel._
import org.apache.poi.hwpf.extractor._
import org.apache.poi.hssf.extractor._
import org.apache.poi.hssf.usermodel._
import org.apache.poi.hslf.extractor._
import org.apache.poi.xwpf.extractor._
import org.apache.poi.xslf.extractor._
import org.apache.poi.xssf.extractor._
import org.apache.poi.extractor._

object TextExtractor {
    def extract(fileName : String) : String = {
        if (fileName == null) {
            return null;
        }
        if (fileName.lastIndexOf(".") > 0) {
            var extension : String = fileName.substring(fileName.lastIndexOf(".") + 1)
            if (extension.equals("pdf")) {
              return extractPDF(fileName)
            } else if (isOfficeFile(extension)) {
              return extractOfficeDocument(fileName)
            }
        }
        return null
    }

    def isOfficeFile(extension : String) : Boolean = List("doc", "docx", "ppt", "pptx", "xls", "xlsx").contains(extension)

    private def extractPDF(fileName : String) : String = {
        val in : InputStream = new FileInputStream(fileName)
        val pdfParser : PDFParser = new PDFParser(in)
        pdfParser.parse()
        return new PDFTextStripper().getText(pdfParser.getPDDocument())
    }

    private def extractOfficeDocument(fileName : String) : String = {
        val in : InputStream = new FileInputStream(fileName)
        return ExtractorFactory.createExtractor(in).getText()
    }

    def main(args : Array[String]) {
      for (fileName <- args) {
        println(fileName)
        println(extract(fileName))
      }
    }
}

