package org.codefirst.shimbashishelf.web
import java.net.{URLEncoder, URLDecoder}

object Helper {
  def url_for(xs : String*) =
    ("/" + xs.map(encode(_)).mkString("/")).replaceAll("/+", "/")

  def link_to(title : String, url : String*) =
    <a href={url_for(url : _*)}>{title}</a>

  def encode(s : String) : String=
    URLEncoder.encode(s, "UTF-8").replace("%2F","/")

  def decode(s : String) : String =
    URLDecoder.decode(s, "UTF-8")
}
