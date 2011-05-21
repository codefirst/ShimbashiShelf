package org.codefirst.shimbashishelf.search
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import java.io.{FileWriter,File}
import org.codefirst.shimbashishelf.util.FileUtil
import org.scalatest.BeforeAndAfterEach

class IndexerSpec extends Spec with ShouldMatchers with BeforeAndAfterEach {
  val IndexFile = "index"
  val SampleFile = new File("index_test/index_test1.txt")
  val AnotherFile = new File("index_test/index_test2.txt")

  override def beforeEach() {
  new File("index_test").mkdir()
    FileUtil.touch(SampleFile,"hello")
    FileUtil.touch(AnotherFile,"hi")
  }

  override def afterEach() {
    FileUtil.delete(new File(IndexFile))
    FileUtil.delete(SampleFile)
    FileUtil.delete(AnotherFile)
  }

  describe("index管理"){
    def indexer = {
      val indexer = Indexer()
      indexer.index(SampleFile)
      indexer
    }

    it("indexに追加できる") {
      val _ = indexer
      Searcher.searchByPath(SampleFile.getAbsolutePath()) should not be (None)
    }

    it("indexから削除できる") {
      indexer.delete(SampleFile)
      Searcher.searchByPath(SampleFile.getAbsolutePath()) should be (None)
    }
  }

  describe("管理番号") {
    it("管理番号が振られる") {
      val indexer = Indexer()
      indexer.index(SampleFile)
      indexer.index(AnotherFile)

      val doc1 = Searcher.searchByPath(SampleFile.getAbsolutePath()).orNull
      val doc2 = Searcher.searchByPath(AnotherFile.getAbsolutePath()).orNull

      doc1.manageID should not be(doc2.manageID)
    }

    it("管理番号が引きつがれる") {
      val indexer = Indexer()
      indexer.index(SampleFile)
      val doc1 = Searcher.searchByPath(SampleFile.getAbsolutePath()).orNull

      indexer.index(SampleFile)
      val doc2 = Searcher.searchByPath(SampleFile.getAbsolutePath()).orNull

      doc1.manageID should be (doc2.manageID)
    }
  }

  describe("ファイルリスト") {
    it ("一覧を取得する") {
      val xs = Indexer.allFiles(new File("index_test"))
      xs should contain (SampleFile)
      xs should contain (AnotherFile)
    }
  }
}
