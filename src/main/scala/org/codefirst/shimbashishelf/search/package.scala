package org.codefirst.shimbashishelf
import org.codefirst.shimbashishelf.common.Home
package object search {
  //@ indexを格納するファイル名
  lazy val INDEX_PATH : String   = Home.dir("index").getAbsolutePath()
  lazy val STATUS_PATH : String  = Home.file("index_status.json").getAbsolutePath()
}
