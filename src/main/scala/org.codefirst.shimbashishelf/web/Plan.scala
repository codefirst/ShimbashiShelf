package org.codefirst.shimbashishelf.web

import unfiltered.request._
import unfiltered.response._
import unfiltered._

class Plan extends unfiltered.filter.Planify({
   case _ => ResponseString("hello world")
})

