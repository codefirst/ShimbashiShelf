package org.codefirst.shimbashishelf.web

import java.io.{File => JFile}
import scala.xml._
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{GivenWhenThen, FeatureSpec,BeforeAndAfterEach}
import unfiltered.scalatest.jetty.Served
import org.apache.http.util.EntityUtils
import org.apache.http.{HttpResponse,HttpEntity}

import org.codefirst.shimbashishelf.common.Home
import org.codefirst.shimbashishelf.filesystem.FileSystem
import org.codefirst.shimbashishelf.util.FileUtil

class PlanSpec extends FeatureSpec with Served  with GivenWhenThen with BeforeAndAfterEach with ShouldMatchers {
  import dispatch._
  import dispatch.mime.Mime._

  def setup = {
    System.setProperty("run.mode", "test")
    _.filter(new Plan).resources(new JFile("src/main/webapp").toURI.toURL)
  }

  val http = new Http

  def status( host : Request ) = {
    def f(x : (Int, HttpResponse, Option[HttpEntity])) : Int = x match {
      case (status, r, _) => (status : Int)
    }
    http.x(Handler(host, { (code, res, ent) => code }))
  }

  def body( host : Request ) =
    http.x(Handler(host, { (code, res, ent) =>
      code should be(200)
      ent match {
        case Some(body) =>
          val xml = EntityUtils.toString(body)
          XML.loadString( xml )
        case _ =>
          fail()
      }}))

  def extract(node : Node, tag : String, klass : String) =
    node \\ tag filter ( _ \ "@class" contains Text(klass))

  def givenEmpty {
    given( "空のレポジトリ・インデックス" )
    FileUtil.delete( FileSystem.gitPath   )
    FileUtil.delete( FileSystem.indexPath )
  }

  def givenFile {
    given( "ファイルがアップロードされている" )
    val sampleFile = new JFile("src/test/scala/org.codefirst.shimbashishelf/web/sample.txt")
    status( host / "upload" <<* ("file", sampleFile)) should be (200)
  }

  feature( "トップページ" ) {
    scenario( "/へのアクセス" ){
      status( host ) should be(200)
    }
  }

  feature( "aboutページ" ) {
    scenario( "aboutへのアクセス" ) {
      status( host / "about" ) should be(200)
    }
  }

  feature("viewページ") {
    def files(xml : Node) =
      extract(xml, "ul", "files") \\ "li"

    scenario( "空ファイルの表示" ) {
      givenEmpty
      when( "/viewへのアクセス" )
      then( "ファイルリストが空" )
        files( body( host / "view" )) should be ('empty)
    }

    scenario( "ディレクトリの作成" ) {
      givenEmpty
      when( "/mkdirへのPOST" )
        status( host / "mkdir" << Map("cwd" -> "", "name" -> "hoge" ))
      then( "ファイルが1個ある" )
        files( body( host / "view" )) should have length (1)
    }

    scenario( "ファイルのアップロード" ) {
      givenEmpty
      givenFile
      when( "/viewへのアクセス" )
      then("ファイルが1個ある")
        files( body( host / "view" )) should have length (1)
    }

    scenario( "個別ファイル表示" ) {
      givenEmpty
      givenFile
      when( "/viewへのアクセス" )
        val html = body( host / "view" / "sample.txt")
      then( "本文表示" )
        (extract(html, "div", "show-body") \\ "pre").text should include regex("hello")
      and( "パスが表示される" )
        (extract(html, "input", "info") \\ "@value").text should include regex("sample.txt")
    }

    scenario( "ダウンロードできる" ) {
      givenEmpty
      givenFile
      when( "/downloadへのアクセス" )
        val html = http.x( Handler(host / "download" / "sample.txt", {
          (code, response, body) =>
            then( "codeが200" )
              code should be(200)

            and( "ヘッダが正常" )
              response.getLastHeader("Content-Disposition").getValue() should be("""attachment; filename="sample.txt"""")
              response.getLastHeader("Content-Type").getValue() should include regex("application/octet-stream")

            and( "本体" )
              EntityUtils.toString(body.get) should include regex("hello world")
        }))
    }
  }

  feature( "検索" ) {
    scenario( "インデックスが空でもエラーがでない" ) {
      givenEmpty
      when( "/seacrhへのアクセス" )
        val html = body( host / "search?q=hello" )
      then( "件数が0" )
        (extract( html, "div", "search-result") \ "div") should have length(0)
    }

    scenario( "インデックス済なら検索できる" ) {
      givenEmpty
      givenFile
      when( "/seacrhへのアクセス" )
        val html = body( host / "search?q=hello" )
      then( "件数が1" )
        (extract( html, "div", "search-result") \ "div") should have length(1)
      and( "リンクがある" )
        (extract( html, "div", "search-result") \ "div" \\ "a" \ "@href").toString() should be("/view/sample.txt")
    }
  }

  feature( "カレンダー" ) {
    scenario( "/calendarが表示できる" ) {
      givenEmpty
      when( "/calendarへのアクセス" )
        val code = status( host / "calendar" )
      then( "成功する" )
        code should be(200)
    }

    scenario( "/calendarにリンクが表示される" ) {
      givenEmpty
      givenFile
      when( "/calendarへのアクセス")
        val html  = body( host / "calendar" )
        val files = extract( html, "td", "files" )
      then( "ファイルへのリンクがある" )
        (files \\ "a") should not be('empty)
        (files \\ "a" \ "@href").toString() should be("/view/sample.txt")
    }
  }

  feature("チケット refs: #674") {
    def givenJaFile {
      given( "ファイルがアップロードされている" )
      val sampleFile = new JFile("src/test/scala/org.codefirst.shimbashishelf/web/sample-ja.txt")
      status( host / "upload" <<* ("file", sampleFile)) should be (200)
    }

    scenario( "日本語を含んだファイルもアップロードできる" ) {
      givenEmpty
      givenJaFile
      when( "/viewへのアクセス" )
        val html = body( host / "view" / "sample-ja.txt")
      then( "本文表示" )
        (extract(html, "div", "show-body") \\ "pre").text should include regex("世界中のコーダ")
    }
  }

  feature("チケット refs: #676") {
    scenario( "ファイル名にスペースを含んでいてもOK" ) {
      givenEmpty
      when("ファイル名にスペースを含んだファイルをアップロード")
      val sampleFile = new JFile("src/test/scala/org.codefirst.shimbashishelf/web/foo bar.txt")
      then( "404じゃない" )
      status( host / "upload" <<* ("file", sampleFile)) should be (200)
    }
  }
}
