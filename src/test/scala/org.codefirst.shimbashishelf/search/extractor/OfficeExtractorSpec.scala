package org.codefirst.shimbashishelf.search.extractor
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class OfficeExtractorSpec extends Spec with ShouldMatchers {
  describe("オフィス以外") {
    it ("オフィス以外を与えるとNoneが返る") {
      OfficeExtractor.extract("hoge.txt") should be (None)
    }
  }

  describe("オフィス") {
    it ("テキストが抽出できる") {
      val FileName = "src/test/scala/org.codefirst.shimbashishelf/search/extractor/sample.ppt"
      val text = OfficeExtractor.extract(FileName).getOrElse("<not found>")
      text should include regex ("コードファースト")
    }

    it ("存在しないファイル") {
      OfficeExtractor.extract("hoge.pdf") should be (None)
    }
  }
}

