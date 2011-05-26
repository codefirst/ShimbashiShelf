package org.codefirst.shimbashishelf.search

import java.io._
import org.codefirst.shimbashishelf.search.extractor._


object TextExtractor {
  private val Extractors : List[Extractor] = List(PdfExtractor,
                                                  OfficeExtractor,
                                                  PlainTextExtractor)

  private def any[A,B](xs : List[A])(f : A => Option[B]) : Option[B] = {
    xs match {
      case List() => None
      case x::xs  =>
        f(x) match {
          case x @ Some(_) => x
          case None => any(xs)(f)
        }
    }
  }

  def extract(fileName : String) : Item =
    any(Extractors){ e => e.extract(fileName) } match {
      case Some(item) => item
      case None => Item("","")
    }
}
