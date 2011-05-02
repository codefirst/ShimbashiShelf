import sbt._

class ShimbashiShelfProject(info: ProjectInfo) extends DefaultWebProject(info)
{
  val pdfbox = "pdfbox" % "pdfbox" % "0.7.3"
  val poi = "org.apache.poi" % "poi" % "3.7"
  val poiOoxml = "org.apache.poi" % "poi-ooxml" % "3.7"
  val poiScratchpad = "poi" % "poi-scratchpad" % "3.1-FINAL"
  val luceneCore = "org.apache.lucene" % "lucene-core" % "3.1.0"
  val luceneAnalyzers = "org.apache.lucene" % "lucene-analyzers" % "3.1.0"
}
