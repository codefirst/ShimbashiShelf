package org.codefirst.shimbashishelf.search
import org.scalatest.Spec
import org.scalatest.matchers.{MustMatchers,ShouldMatchers}

class StatusSpec extends Spec with ShouldMatchers {
  describe("map") {
    val status = Status.empty
    status("hi") = 42

    it("should return stored value") {
      status.int("hi") should be(42)
    }

    describe("safe-way") {
      it("should return Some"){
        status.safeInt("hi") should be(Some(42))
      }
      it("should return None"){
        status.safeInt("ho") should be(None)
      }
    }
  }

  describe("serialize") {
    val status = Status.empty
    status("hi") = 42

    it("serialize/unserialize") {
      val s = Status.fromJson(status.toJson)
      s.int("hi") should be(42)
    }
  }
}
