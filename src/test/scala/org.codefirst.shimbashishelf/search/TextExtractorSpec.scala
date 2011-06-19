package org.codefirst.shimbashishelf.search
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class TextExtractorSpec extends Spec with ShouldMatchers {
  val FileName = "src/test/scala/org/codefirst/shimbashishelf/search/extractor/sample.pdf"
  val text = TextExtractor.extract(FileName)
  describe("PDF") {
    it("PDFからテキストが抜ける") {
      text should include regex("codefirst")
    }
  }
}
