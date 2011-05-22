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

  describe("空白文字列で検索") {
    it("検索結果") {
      Searcher(IndexFile).search("", "content").length should be (0)
    }
  }

  describe("nullで検索") {
    it("検索結果") {
      Searcher(IndexFile).search(null, "content").length should be (0)
    }
  }

  describe("検索") {
    def doc =
      Searcher(IndexFile).search("hello", "content")(0)

    describe("ドキュメント") {
      it("パス") {
        doc.path should be ( SampleFile.getAbsolutePath())
      }

      it("ファイル名") {
        doc.filename should be ( "index_test1.txt" )
      }

      it("content") {
        val s = new String(doc.content)
        s should be ("hello world")
      }

      it("is") {
        val s = new String(doc.is.orNull)
        s should be ("hello world")
      }

      it("ハイライト") {
        doc.highlight should be (<pre><strong>hello</strong> world</pre>)
      }
    }
  }

  describe("ドキュメントID") {
    def doc =
      Searcher(IndexFile).search("hello", "content")(0)
    it("IDからドキュメントを取得できる") {
      Document.get(doc.id, IndexFile).orNull.id should be (doc.id)
      Document.get(doc.id, IndexFile).orNull.path should be (doc.path)
    }
  }
}
