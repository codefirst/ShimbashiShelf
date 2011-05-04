import sbt._

class ShimbashiShelfProjectPlugins(info: ProjectInfo) extends PluginDefinition(info) {
    val repo = "Christoph's Maven Repo" at "http://maven.henkelmann.eu/"
    val junitXml = "eu.henkelmann" % "junit_xml_listener" % "0.2"
}
