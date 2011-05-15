import sbt._

class ShimbashiShelfProjectPlugins(info: ProjectInfo) extends PluginDefinition(info) {
  val repo = "Christoph's Maven Repo" at "http://maven.henkelmann.eu/"
  val junitXml = "eu.henkelmann" % "junit_xml_listener" % "0.2"
  val scctRepo = "scct-repo" at "http://mtkopone.github.com/scct/maven-repo/"
  lazy val scctPlugin = "reaktor" % "sbt-scct-for-2.8" % "0.1-SNAPSHOT"
}
