package org.codefirst.shimbashishelf.search
import org.scalatest.Spec
import org.scalatest.matchers.MustMatchers
import org.scalatest.matchers.ShouldMatchers
import scala.collection.mutable.Stack
import org.apache.pdfbox.pdfparser._
import org.scalatest.mock.MockitoSugar
import java.io.InputStream

class TextExtractorSpec extends Spec with ShouldMatchers with MockitoSugar {
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
