package unfiltered.scalatest.jetty

import _root_.unfiltered.scalatest.Hosted
import org.scalatest.{FeatureSpec, BeforeAndAfterAll}

trait Served extends FeatureSpec with BeforeAndAfterAll with Hosted {

  import unfiltered.jetty._
  def setup: (Server => Server)

  lazy val server : Server = setup(Http(port))

  override protected def beforeAll {
    server.start()
  }

  override protected def afterAll {
    server.stop()
    server.destroy()
  }
}
