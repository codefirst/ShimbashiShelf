package org.codefirst.shimbashishelf.vcs

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfterEach
import java.io.{File,FileWriter}
import org.codefirst.shimbashishelf.util.FileUtil

class VersionControlSpec extends Spec with ShouldMatchers with BeforeAndAfterEach {
  val Repository = "__vcs_git__"
  val FileName = "__vcs_git__/hello.txt"

  override def beforeEach() {
    (new File(Repository)).mkdir()

    val writer = new FileWriter(FileName)
    try {
      writer.write("foo\nbar\nbaz")
    } finally {
      writer.close()
    }
  }

  override def afterEach(){
    FileUtil.delete(new File(Repository))
  }

  describe("コミットリスト"){
    it("コミットあり") {
      val vcs = new VersionControl(new File(Repository))
      vcs.commit(new File(FileName))
      vcs.commitList(None, None).length should be(1)
    }

    it("削除あり") {
      val vcs = new VersionControl(new File(Repository))
      vcs.commit(new File(FileName))
      vcs.remove(new File(FileName))
      vcs.commitList(None, None).length should be(2)
    }
  }

  describe("コミットオブジェクト") {
    def commit = {
      val vcs = new VersionControl(new File(Repository))
      vcs.commit(new File(FileName))
      vcs.commitList(None, None)(0)
    }

    it("Author") {
      commit.author should be("ShimbashiShelf")
    }

    it("email") {
      commit.email should be("ShimbashiShelf@codefirst.org")
    }

    it("files") {
      commit.files should be(List("hello.txt"))
    }
  }
}

