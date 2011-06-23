package org.codefirst.shimbashishelf.search
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import java.io.{FileWriter,File}
import org.codefirst.shimbashishelf.util.FileUtil
import org.scalatest.BeforeAndAfterEach

class SearcherSpec extends Spec with ShouldMatchers with BeforeAndAfterEach {
  val IndexFile = "search_test/index"
  val SampleFile = new File("search_test/index_test1.txt")

  override def beforeEach() {
    new File(IndexFile).mkdirs()
    FileUtil.touch(SampleFile,"hello world")
    Indexer(IndexFile).index(SampleFile)
  }

  override def afterEach() {
    FileUtil.delete(new File(IndexFile))
    FileUtil.delete(SampleFile)
  }

  def searcher =
    Searcher(IndexFile)

  describe("空白文字列で検索") {
    it("検索結果") {
      searcher.searchByQuery("").length should be (0)
    }
  }

  describe("nullで検索") {
    it("検索結果") {
      searcher.searchByQuery(null).length should be (0)
    }
  }

  describe("検索") {
    lazy val (doc, highlight) =
      searcher.searchByQuery("hello")(0)

    describe("ドキュメント") {
      it("パス") {
        doc.path should be ( SampleFile.getAbsolutePath())
      }

      it("ファイル名") {
        doc.name should be ( "index_test1.txt" )
      }

      it("content") {
        doc.metadata.content should be ("hello world")
      }

      it("ハイライト") {
        highlight should be (<pre><strong>hello</strong> world</pre>)
      }
    }
  }

  describe("searchByPath") {
    it("seacrhbypath") {
      searcher.searchByPath(SampleFile.getAbsolutePath()) should not be(None)
    }
  }

  describe("パスによる検索") {
    lazy val (doc,_) =
      searcher.searchByQuery("index_test1")(0)

    describe("ドキュメント") {
      it("パス") {
        doc.path should be ( SampleFile.getAbsolutePath())
      }

      it("ファイル名") {
        doc.name should be ( "index_test1.txt" )
      }

      it("content") {
        doc.metadata.content should be ("hello world")
      }
    }
  }
}
