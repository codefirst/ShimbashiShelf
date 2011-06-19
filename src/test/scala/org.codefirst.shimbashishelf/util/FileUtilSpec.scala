package org.codefirst.shimbashishelf.util
import org.scalatest.Spec
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.ShouldMatchers
import java.io.{File,FileWriter}

class FileUtilSpec extends Spec with ShouldMatchers {
  it("パスの結合") {
    FileUtil.join("/foo/bar","baz") should be ("/foo/bar/baz")
  }

  describe("getExtension") {
    it("ファイル名から取得する") {
      FileUtil.getExtension("hoge.txt") should be ("txt")
      FileUtil.getExtension("hoge.fuga.ppt") should be ("ppt")
    }

    it("パスから取得する") {
      FileUtil.getExtension("/path/to/hoge.fuga.ppt") should be ("ppt")
    }

    it("Windowsのパスから取得する") {
      FileUtil.getExtension("c:\\path\\to\\hoge.fuga.ppt") should be ("ppt")
    }
  }

  describe("basename") {
    it("ファイル名から取得する") {
      FileUtil.basename("hoge.fuga.ppt") should be ("hoge.fuga.ppt")
    }

    it("パスから取得する") {
      FileUtil.basename("/path/to/hoge.fuga.ppt") should be ("hoge.fuga.ppt")
    }
    it("Windowsのパスから取得する") {
      FileUtil.basename("c:\\path\\to\\hoge.fuga.ppt") should be ("hoge.fuga.ppt")
    }
  }
}

class FileUtilSpecWithIO extends Spec with ShouldMatchers with BeforeAndAfterEach  {
  val FileName = "temp-file.txt"

  override def beforeEach() {
    val writer = new FileWriter(FileName)
    try {
      writer.write("foo\nbar\nbaz")
    } finally {
      writer.close()
    }
  }
  override def afterEach(){
    val file = new File(FileName)
    file.delete()
  }

  describe("ファイルがある場合") {
    it("readAll") {
      FileUtil.readAll(FileName) should be (Some("foo\nbar\nbaz"))
    }

    it("readByte") {
      FileUtil.readArray(FileName) should be (Some("foo\nbar\nbaz".toArray.map(_.toByte)))
    }
  }

  describe("ファイルがない場合") {
    it("readAll") {
      FileUtil.readAll("/path/to/not/exists") should be (None)
    }

    it("readByte") {
      FileUtil.readArray("/path/to/not/exists") should be (None)
    }
  }
}

