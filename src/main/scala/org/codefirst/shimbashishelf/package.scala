package org.codefirst

package object shimbashishelf {
  //@ indexを格納するファイル名
  val INDEX_PATH : String  = "index"

  val STATUS_PATH : String  = "status.json"

  def using[A <: { def close() : Unit }, B](resource : A)(body : A => B) : B =
    try     { body(resource) }
    finally { resource.close() }
}
