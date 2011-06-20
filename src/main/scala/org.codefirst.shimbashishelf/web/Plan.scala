package org.codefirst.shimbashishelf.web

import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._

class Plan extends unfiltered.filter.Planify({
  case Path(Seg("static" :: _)) =>
    Pass
  case req@Path("/") =>
    Ok ~> Scalate(req, "index.scaml")
  case req@Path("/about") =>
    Ok ~> Scalate(req, "about.scaml")
  case req@Path("/search") =>
    Ok ~> Scalate(req, "search.scaml")
  case req@Path("/calendar") =>
    Calendar(req)
  case req@Path("/show") =>
    Ok ~> Scalate(req, "show.scaml")
  case req =>
    NotFound ~> Scalate(req, "404.scaml")
})
