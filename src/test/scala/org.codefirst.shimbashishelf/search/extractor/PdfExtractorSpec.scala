package org.codefirst.shimbashishelf.search.extractor
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class PdfExtractorSpec extends Spec with ShouldMatchers {
  describe("PDF以外") {
    it ("PDF以外を与えるとNoneが返る") {
      PdfExtractor.extract("hoge.ppt") should be (None)
    }
  }

  describe("PDF") {
    val FileName = "src/test/scala/org/codefirst/shimbashishelf/search/extractor/sample.pdf"
    val text = PdfExtractor.extract(FileName).getOrElse("<not found>")
    it ("テキストが抽出できる") {
      text should include regex ("codefirst")
    }

    it ("日本語も抽出できる") {
      text should include regex ("世界中のコーダーが")
    }

    it ("存在しないファイル") {
      PdfExtractor.extract("hoge.pdf") should be (None)
    }
  }
}

