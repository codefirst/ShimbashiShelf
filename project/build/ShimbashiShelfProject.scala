import sbt._
import eu.henkelmann.sbt.JUnitXmlTestsListener
import reaktor.scct.ScctProject
import org.fusesource.scalate.sbt.PrecompilerWebProject

class ShimbashiShelfProject(info: ProjectInfo)
  extends DefaultWebProject(info)
  with    ScctProject
  with    WinstoneProject
  with    PrecompilerWebProject
{
  val jgitRepos = "jgit-repository" at "http://download.eclipse.org/jgit/maven"

  val uf_filter = "net.databinder" %% "unfiltered-filter" % "0.3.4"
  val uf_jetty  = "net.databinder" %% "unfiltered-jetty" % "0.3.4"
  val uf_uploads = "net.databinder" %% "unfiltered-uploads" % "0.3.4"
  val scalate_core = "org.fusesource.scalate" % "scalate-core" % "1.5.0"

  val json = "net.liftweb" %% "lift-json" % "2.4-M2" % "compile"
  val fam  = "org.apache.commons" % "commons-jci-fam" % "1.0" % "compile"
  val pdfbox = "org.apache.pdfbox" % "pdfbox" % "1.5.0"
  val poi = "org.apache.poi" % "poi" % "3.8-beta2"
  val poiOoxml = "org.apache.poi" % "poi-ooxml" % "3.8-beta2"
  val poiScratchpad = "org.apache.poi" % "poi-scratchpad" % "3.8-beta2"
  val poiExcelant = "org.apache.poi" % "poi-excelant" % "3.8-beta2"
  val luceneCore = "org.apache.lucene" % "lucene-core" % "3.1.0"
  val luceneAnalyzers = "org.apache.lucene" % "lucene-analyzers" % "3.1.0"
  val luceneHightlighter = "org.apache.lucene" % "lucene-highlighter" % "3.1.0"
  val jgit = "org.eclipse.jgit" % "org.eclipse.jgit" % "1.0.0.201106090707-r"
  val log4j = "log4j" % "log4j" % "1.2.16"
  val mimeUtil = "eu.medsea.mimeutil" % "mime-util" % "2.1.3"
  val codec = "commons-codec" % "commons-codec" % "1.3"
  val slf4j = "org.slf4j" % "slf4j-log4j12" % "1.6.1"

  // for development
  val jetty6 = "org.mortbay.jetty" % "jetty" % "6.1.14" % "test"
  val scalaTest = "org.scalatest" % "scalatest_2.9.0" % "1.6.1"

  // test
  val uf_netty = "net.databinder" %% "unfiltered-netty" % "0.3.4"
  val dispatch_http = "net.databinder" %% "dispatch-http" % "0.8.3"
  val dispatch_mime = "net.databinder" %% "dispatch-mime" % "0.8.3"
  def junitXmlListener: TestReportListener = new JUnitXmlTestsListener(outputPath.toString)
  override def testListeners: Seq[TestReportListener] = super.testListeners ++ Seq(junitXmlListener)

  override def includeTest(s: String) = { s.endsWith("Spec") || s.contains("UserGuide") }

  lazy val jar = packageTask(packagePaths, jarPath, packageOptions).dependsOn(compile) describedAs "Creates a jar file."

  override def testOptions = super.testOptions ++ Seq(
    TestArgument("-Drun.mode=hehehe")
  )

  override def javaCompileOptions = super.javaCompileOptions ++ Seq("-source", "1.5","-encoding", "utf8").map(x => JavaCompileOption(x))
}
