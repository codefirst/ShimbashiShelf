package org.codefirst.shimbashishelf.web

object Helper {
  def url_for(xs : String*) =
    ("/" + xs.mkString("/")).replaceAll("/+", "/")

  def link_to(title : String, url : String*) =
    <a href={url_for(url : _*)}>{title}</a>
}
