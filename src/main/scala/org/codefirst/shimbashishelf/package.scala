package org.codefirst

import org.eclipse.jgit.revwalk._
import org.eclipse.jgit.treewalk._
import collection.JavaConversions._
import scala.collection.mutable._

package object shimbashishelf {
  //@ indexを格納するファイル名
  val INDEX_PATH : String  = "index"

  val STATUS_PATH : String  = "status.json"

  def using[A <: { def close() : Unit }, B](resource : A)(body : A => B) : B =
    try     { body(resource) }
    finally { resource.close() }
}
