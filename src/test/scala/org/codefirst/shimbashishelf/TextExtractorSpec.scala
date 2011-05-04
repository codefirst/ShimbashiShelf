package org.codefirst.shimbashishelf
import org.scalatest.Spec
import org.scalatest.matchers.MustMatchers
import org.scalatest.matchers.ShouldMatchers
import scala.collection.mutable.Stack


class TextExtractorSpec extends Spec with ShouldMatchers {
  describe("FileNameUtil") {

    describe("get extension of hoge.fuga.ppt") {
      it("should be ppt") {
        FileNameUtil.getExtension("hoge.fuga.ppt") should be ("ppt")
      }
    }
  }

  describe("PdfExtractor") { 
    it ("PDFファイルを入力すると Noneが返る") { 
      PdfExtractor.extract("hoge.ppt") should be (None)
    }
    // it ("PDFファイルを入力すると Someが返る") { 
    //   PdfExtractor.extract("hoge.pdf") should be (Some("hoge"))
    // }
  }
}

