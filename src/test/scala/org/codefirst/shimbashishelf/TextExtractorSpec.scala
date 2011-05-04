package org.codefirst.shimbashishelf
import org.specs2.mutable._

class TextExtractorSpec extends Specification {
  
  "FileNameUtil" should {
    "get extension" in {
      FileNameUtil.getExtension("hoge.fuga.ppt") must_== "ppt"
    }
  }

  "PDFExtractor" should {
    "PDFではないファイルを入力すると Noneが返る" in { 
      PdfExtractor.extract("hoge.ppt") must_== None
    }
  }
}

