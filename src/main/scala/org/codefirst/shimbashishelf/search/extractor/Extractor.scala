package org.codefirst.shimbashishelf.search.extractor

trait Extractor{
  def extract(fileName : String) : Option[String]
}
