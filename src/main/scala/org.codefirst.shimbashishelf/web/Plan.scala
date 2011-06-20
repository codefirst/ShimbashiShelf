package org.codefirst.shimbashishelf.web

import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._

class Plan extends unfiltered.filter.Planify({
  case Path(Seg("static" :: name)) =>
    Pass
  case req => Ok ~> Scalate(req, "index.scaml")
})

