package org.codefirst.shimbashishelf.search
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class MimeDetectorSpec extends Spec with ShouldMatchers {

  def file(name : String) =
    new java.io.File("src/test/scala/org/codefirst/shimbashishelf/search/" + name)
  describe("拡張子から判断する") {
    it("png") {
      MimeDetector( file("test.png")) should be("image/png")
    }
  }

  describe("内容から判断する") {
    it("png") {
      MimeDetector( file("test-png.hogehoge") ) should be("image/png")
    }
  }
}
