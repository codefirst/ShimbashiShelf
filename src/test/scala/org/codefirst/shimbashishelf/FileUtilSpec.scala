package org.codefirst.shimbashishelf
import org.scalatest.Spec
import org.scalatest.matchers.MustMatchers
import org.scalatest.matchers.ShouldMatchers

class FileUtilSpec extends Spec with ShouldMatchers {
  describe("extension") {
    it("get from filename") {
      FileUtil.getExtension("hoge.fuga.ppt") should be ("ppt")
    }

    it("get from path") {
      FileUtil.getExtension("/path/to/hoge.fuga.ppt") should be ("ppt")
    }
  }
  describe("basename") {
    it("get from filename") {
      FileUtil.basename("hoge.fuga.ppt") should be ("hoge.fuga.ppt")
    }

    it("get from unix path") {
      FileUtil.basename("/path/to/hoge.fuga.ppt") should be ("hoge.fuga.ppt")
    }
    it("get from windows path") {
      FileUtil.basename("c:\\path\\to\\hoge.fuga.ppt") should be ("hoge.fuga.ppt")
    }
  }
}

