import sbt._

class ShimbashiShelfProject(info: ProjectInfo) extends DefaultWebProject(info)
{
  val pdfbox = "pdfbox" % "pdfbox" % "0.7.3"
  val poi = "org.apache.poi" % "poi" % "3.7"
  val poiOoxml = "org.apache.poi" % "poi-ooxml" % "3.7"
  val poiScratchpad = "poi" % "poi-scratchpad" % "3.1-FINAL"
  val luceneCore = "org.apache.lucene" % "lucene-core" % "3.1.0"
  val luceneAnalyzers = "org.apache.lucene" % "lucene-analyzers" % "3.1.0"
  val specs2 = "org.specs2" % "specs2_2.8.1" % "1.2"

  def specs2Framework = new TestFramework("org.specs2.runner.SpecsFramework")
  override def testFrameworks = super.testFrameworks ++ Seq(specs2Framework)

  override def includeTest(s: String) = { s.endsWith("Spec") || s.contains("UserGuide") }

  override def mainClass = Some("org.codefirst.shimbashishelf.ShimbashiShelf")

  lazy val jar = packageTask(packagePaths, jarPath, packageOptions).dependsOn(compile) describedAs "Creates a jar file."
}
