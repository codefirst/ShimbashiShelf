package scalate

import org.fusesource.scalate.{Binding,TemplateSource}
import org.fusesource.scalate.support.TemplatePackage

class ScalatePackage extends TemplatePackage {
  def header(source: TemplateSource, bindings: List[Binding]) = """
import org.codefirst.shimbashishelf.web.Helper._
  """
}
