package org.codefirst

package object shimbashishelf {
  //@ indexを格納するファイル名
  val INDEX_PATH : String  = "index"

  def using[A <: { def close() : Unit }, B](resource : A)(body : A => B) : Option[B] =
    try     { Some(body(resource)) }
    catch   { case _ => None }
    finally { resource.close() }

}
