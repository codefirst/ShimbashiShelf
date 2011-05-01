import sbt._

class ShimbashiShelfProject(info: ProjectInfo) extends DefaultWebProject(info)
{
  val pdfbox = "pdfbox" % "pdfbox" % "0.7.3"
  val poi = "org.apache.poi" % "poi" % "3.7"
  val poiOoxml = "org.apache.poi" % "poi-ooxml" % "3.7"
  val poiScratchpad = "poi" % "poi-scratchpad" % "3.1-FINAL"

  // alternately use derby
  // val derby = "org.apache.derby" % "derby" % "10.2.2.0" % "runtime"
  // val servlet = "javax.servlet" % "servlet-api" % "2.5" % "provided"
  // val junit = "junit" % "junit" % "3.8.1" % "test"    
}
