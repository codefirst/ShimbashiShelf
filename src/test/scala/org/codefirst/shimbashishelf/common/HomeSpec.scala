package org.codefirst.shimbashishelf.common

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfterEach
import java.io.File
import org.codefirst.shimbashishelf.util.FileUtil

class HomeSpec extends Spec with ShouldMatchers with BeforeAndAfterEach {
  override def beforeEach() {
  }

  override def afterEach(){
    FileUtil.delete(new File("test_home_spec"))
  }

  def env(x : String) : String => String =
    { case "SHIMBASHI_SHELF_HOME" => x }

  describe("パスの取得") {
    it("環境変数がない場合") {
      Home.file("some_config_file.json", env(null)) should be(new File("some_config_file.json"))
    }

    it("環境変数がある場合") {
      Home.file("some_config_file.json",env("/tmp")) should be(new File("/tmp/some_config_file.json"))
    }

    it("ディレクトリの取得もできる") {
      Home.dir("test_path_spec", env(null)) should be(new File("test_path_spec"))
    }
  }

  describe("ディレクトリの自動生成") {
    it("ファイルの場合親ディレクトリができる") {
      val f = Home.file("test_path_spec/bar/baz.txt", env(null))
      f.exists() should be(false)
      new File(f.getParent()).exists() should be(true)
    }

    it("ディレクトリの場合ディレクトリができる") {
      val f = Home.dir("test_path_spec/bar/baz", env(null))
      f.exists() should be(true)
    }
  }
}

