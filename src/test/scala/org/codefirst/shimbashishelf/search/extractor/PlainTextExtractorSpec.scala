package org.codefirst.shimbashishelf.search.extractor
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class PlainTextExtractorSpec extends Spec with ShouldMatchers {
  def extractText(filename : String) {
    val text = PlainTextExtractor.extract(filename).getOrElse(Item("",""))
    it ("テキストが抽出できる") {
      text.content should include regex ("codefirst")
    }

    it ("日本語が抽出できる") {
      text.content should include regex ("世界中のコーダ")
    }

    it ("mimetypeがplain/text") {
      text.mimeType should be ("plain/text")
    }
  }

  describe("UTF-8") {
    it should behave like extractText("src/test/scala/org/codefirst/shimbashishelf/search/extractor/sample-utf8.txt")
  }

  describe("EUC_JP") {
    it should behave like extractText("src/test/scala/org/codefirst/shimbashishelf/search/extractor/sample-euc.txt")
  }
}

