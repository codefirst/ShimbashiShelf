package org.codefirst.shimbashishelf.monitor

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfterEach
import org.codefirst.shimbashishelf.util.FileUtil
import java.io.File

class GlobSpec extends Spec with ShouldMatchers {
   describe("ファイル名によるマッチ") {
     it("成功") {
       Glob("foo").isMatch(new File("foo")) should be(true)
     }

     it("サブディレクトリにあってもマッチする") {
       Glob("foo").isMatch(new File("bar/foo")) should be(true)
     }

     it("部分マッチはしない") {
       Glob("foo").isMatch(new File("fooo")) should be(false)
     }

    it("親ディレトリにもマッチする") {
      Glob("foo").isMatch(new File("foo/bar")) should be(true)
    }

     it("失敗"){
       Glob("foo").isMatch(new File("bar")) should be(false)
     }
   }

  describe("*によるマッチ") {
    it("全体") {
      Glob("*").isMatch(new File("fooo")) should be(true)
    }

    it("後方") {
      Glob("foo*").isMatch(new File("fooxx")) should be(true)
    }

    it("前方") {
      Glob("*foo").isMatch(new File("xxfoo")) should be(true)
    }

    it("マッチしない") {
      Glob("*foo").isMatch(new File("oo")) should be(false)
    }
  }
}


