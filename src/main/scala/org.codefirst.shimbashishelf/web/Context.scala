package org.codefirst.shimbashishelf.web

import org.fusesource.scalate.{Binding,TemplateSource}
import org.fusesource.scalate.support.TemplatePackage
import org.fusesource.scalate.layout.DefaultLayoutStrategy
import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._
import unfiltered.filter.InittedFilter

import org.fusesource.scalate._
import org.fusesource.scalate.servlet._

import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.codefirst.shimbashishelf.util.Base._

object Template {
  type Render[B] = (String, (String,Any)*) => Responder[B]
}

trait Template extends InittedFilter {
  def builder[A,B](req: HttpRequest[A], res: HttpResponse[B], engine: TemplateEngine) =
    (req.underlying, res.underlying) match {
      case (hreq : HttpServletRequest, hres : HttpServletResponse) =>
        new ServletRenderContext(engine, hreq, hres, config.getServletContext)
      case _ =>
        new DefaultRenderContext(unfiltered.util.Optional(req.uri).getOrElse("").split('?')(0), engine, res.getWriter)
    }

  lazy val engine = {
    val e = new ServletTemplateEngine(config)
    e.allowCaching = true
    e.allowReload = false
    e.mode = "production"
    e
  }

  def render[A,B](request: HttpRequest[A])(template: String, attributes:(String,Any)*) : Responder[B] = {
    val path =
      "/WEB-INF/scalate/%s".format(template)

    HtmlContent ~> Scalate(request, path, attributes : _*)(
      engine = engine,
      contextBuilder = builder _ )
  }
}

case class Context[A,B](request : HttpRequest[A], render : Template.Render[B]) {
  request.underlying match {
    case hreq : HttpServletRequest =>
      hreq.setCharacterEncoding("UTF-8")
  }
  def string(name : String) : Option[String] = {
    val xs = request.parameterValues(name)
    if(xs eq null)
      None
    else
      xs.headOption
  }

  def int(name : String, value : Int = 0) : Int = {
    string(name).flatMap(s => sure(s.toInt)).getOrElse(value)
  }
}

