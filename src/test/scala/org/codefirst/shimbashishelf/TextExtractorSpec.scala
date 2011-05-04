package org.codefirst.shimbashishelf
import org.scalatest.Spec
import org.scalatest.matchers.MustMatchers
import org.scalatest.matchers.ShouldMatchers
import scala.collection.mutable.Stack
import org.pdfbox.pdfparser._


class TextExtractorSpec extends Spec with ShouldMatchers {
  import org.mockito.Mockito

  describe("FileNameUtil") {

    describe("get extension of hoge.fuga.ppt") {
      it("should be ppt") {
        FileNameUtil.getExtension("hoge.fuga.ppt") should be ("ppt")
      }
    }
  }

  describe("PdfExtractor") { 
    it ("PDFではないファイルを入力すると Noneが返る") { 
      PdfExtractor.extract("hoge.ppt") should be (None)
    }
  }

  describe("OfficeExtractor") { 
    it ("Officeではないファイルを入力すると Noneが返る") { 
      OfficeExtractor.extract("hoge.txt") should be (None)
    }
  }

}

