package org.codefirst.shimbashishelf
import org.specs2.mutable._

class IndexerSpec extends Specification {
  "Indexer" should {
    "be enable to index" in {
      (Indexer.index("ShimbashiShelf")) must beTrue
    }
  }
}
