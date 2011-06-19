package org.codefirst.shimbashishelf.common

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mock.MockitoSugar
import org.codefirst.shimbashishelf.util.FileUtil
import java.io.File

class ConfigSpec extends Spec with ShouldMatchers with BeforeAndAfterEach with MockitoSugar {
  val FileName = "test_config"
  override def beforeEach() {
  }

  override def afterEach(){
    FileUtil.delete(new File(FileName))
  }

  def config = Config.open(FileName)
  describe("無視ファイルの設定・取得"){
    it("設定・取得できる")  {
      val c = config
      c.ignoreFiles = List(".git", "*~")
      c.ignoreFiles should be (List(".git", "*~"))
    }
  }

  describe("パス"){
    it("環境変数なの場合はカレントディレクトリ以下") {
      val c = Config.default { case _ => null }
      c.path should be (new File("config.json"))
    }
    it("環境変数ありの場合はその下") {
      val c = Config.default { case "SHIMBASHI_SHELF_HOME" => "/tmp" }
      c.path should be (new File("/tmp/config.json"))
    }
  }

  describe("シリアライズ") {
    it("保存できる") {
      val c = config
      c.ignoreFiles = List("a","b","c")
      c.save()
      config.ignoreFiles should be (List("a","b","c"))
    }
  }
}

