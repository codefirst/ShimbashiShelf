package org.codefirst.shimbashishelf.search.extractor
import java.io.{InputStream,FileInputStream}
import org.codefirst.shimbashishelf.util.FileUtil

trait Extractor{
  def extract(fileName : String) : Option[String]
}

trait ExtensionExtractor extends Extractor {
  def extensions : List[String]
  def extract(in : InputStream) : Option[String]

  def extract(fileName : String) : Option[String] =
    if(extensions contains FileUtil.getExtension(fileName)) {
      try {
        val in : InputStream = new FileInputStream(fileName)
        try {
          extract(in)
        } finally {
          in.close()
        }
      } catch{ case _ => None }
    } else
      None
}
