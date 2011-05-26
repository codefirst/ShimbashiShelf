package org.codefirst.shimbashishelf.search.extractor
import java.io.{InputStream,FileInputStream}
import org.codefirst.shimbashishelf.util.FileUtil

case class Item(content : String, mimeType : String)

trait Extractor{
  def extract(fileName : String) : Option[Item]
}

trait ExtensionExtractor extends Extractor {
  def extensions : List[String]
  def extract(in : InputStream) : Option[String]
  def mimeType(key : String) : String

  def extract(fileName : String) : Option[Item] = {
    val ext = FileUtil.getExtension(fileName)
    if(extensions contains ext) {
      try {
        val in : InputStream = new FileInputStream(fileName)
        try {
          for {
            s <- extract(in)
          } yield Item(s, mimeType(ext))
        } finally {
          in.close()
        }
      } catch{ case _ => None }
    } else
      None
  }
}
