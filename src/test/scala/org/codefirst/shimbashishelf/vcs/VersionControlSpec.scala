package org.codefirst.shimbashishelf.vcs

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfterEach
import java.io.{File,FileWriter}

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

  private def delete(f : File) {
    if(f.exists() != false) {
      if(f.isFile())
        f.delete()
      else if(f.isDirectory()){
        for(x <- f.listFiles()) delete(x)
        f.delete()
      }
    }
  }
  override def afterEach(){
    delete(new File(Repository))
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
      commit.getAuthor() should be("ShimbashiShelf")
    }

    it("email") {
      commit.getEmailAddress() should be("ShimbashiShelf@codefirst.org")
    }

    it("files") {
      commit.getFiles() should be(List("hello.txt"))
    }
  }
}

