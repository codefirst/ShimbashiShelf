package org.codefirst.shimbashishelf
import scala.io.Source
import java.io._
import org.apache.pdfbox.util._
import org.apache.pdfbox.pdfparser._
import org.apache.pdfbox.pdmodel._
import org.apache.poi.hwpf.extractor._
import org.apache.poi.hssf.extractor._
import org.apache.poi.hssf.usermodel._
import org.apache.poi.hslf.extractor._
import org.apache.poi.xwpf.extractor._
import org.apache.poi.xslf.extractor._
import org.apache.poi.xssf.extractor._
import org.apache.poi.extractor._

trait Extractor{
  def extract(fileName : String) : Option[String]
}

object PdfExtractor extends Extractor {
  def extract(fileName : String) = {
    if(FileUtil.getExtension(fileName) == "pdf") {
      val in : InputStream = new FileInputStream(fileName)
      val pdfParser : PDFParser = new PDFParser(in)
      pdfParser.parse()
      Some(new PDFTextStripper().getText(pdfParser.getPDDocument()))
    } else
      None
  }
}

object OfficeExtractor extends Extractor {
  val extensions = List("doc", "docx", "ppt", "pptx", "xls", "xlsx")

  def extract(fileName : String) = {
    val ext = FileUtil.getExtension(fileName)
    if(extensions contains ext) {
      val in : InputStream = new FileInputStream(fileName)
      Some(ExtractorFactory.createExtractor(in).getText())
    } else
      None
  }
}

object PlainTextExtractor extends Extractor {
  def extract(fileName : String) =
    FileUtil.readAll(fileName)
}

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

  def extract(fileName : String) : String =
    any(Extractors){ e => e.extract(fileName) }.getOrElse("")

}
