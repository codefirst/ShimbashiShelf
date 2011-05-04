package org.codefirst.shimbashishelf
import org.specs2.mutable._

class TextExtractorSpec extends Specification {
  "office extension check" should {
    "ppt is office" in {
      TextExtractor.isOfficeFile("ppt") must beTrue
    }
    "txt is not office" in {
      TextExtractor.isOfficeFile("txt") must beFalse
    }
    "pdf is not office" in {
      TextExtractor.isOfficeFile("txt") must beFalse
    }
  }
}
