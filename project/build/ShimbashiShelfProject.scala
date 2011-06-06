import sbt._
import eu.henkelmann.sbt.JUnitXmlTestsListener
import reaktor.scct.ScctProject

class ShimbashiShelfProject(info: ProjectInfo)
  extends DefaultWebProject(info)
  with    ScctProject
  with    WinstoneProject
{
  val lift   = "net.liftweb" %% "lift-mapper" % "2.2" % "compile"
  val fam  = "org.apache.commons" % "commons-jci-fam" % "1.0" % "compile"
  val pdfbox = "org.apache.pdfbox" % "pdfbox" % "1.5.0"
  val poi = "org.apache.poi" % "poi" % "3.8-beta2"
  val poiOoxml = "org.apache.poi" % "poi-ooxml" % "3.8-beta2"
  val poiScratchpad = "org.apache.poi" % "poi-scratchpad" % "3.8-beta2"
  val poiExcelant = "org.apache.poi" % "poi-excelant" % "3.8-beta2"
  val luceneCore = "org.apache.lucene" % "lucene-core" % "3.1.0"
  val luceneAnalyzers = "org.apache.lucene" % "lucene-analyzers" % "3.1.0"
  val luceneHightlighter = "org.apache.lucene" % "lucene-highlighter" % "3.1.0"
  val jgit = "com.madgag" % "org.eclipse.jgit" % "0.11.99.4-UNOFFICIAL-ROBERTO-RELEASE"
  val log4j = "log4j" % "log4j" % "1.2.16"

  // for development
  val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.25" % "test"
  val scalaTest = "org.scalatest" % "scalatest" % "1.3"

  def junitXmlListener: TestReportListener = new JUnitXmlTestsListener(outputPath.toString)
  override def testListeners: Seq[TestReportListener] = super.testListeners ++ Seq(junitXmlListener)

  override def includeTest(s: String) = { s.endsWith("Spec") || s.contains("UserGuide") }

  lazy val jar = packageTask(packagePaths, jarPath, packageOptions).dependsOn(compile) describedAs "Creates a jar file."
}
