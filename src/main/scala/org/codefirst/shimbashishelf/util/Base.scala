package org.codefirst.shimbashishelf.util

object Base{
  def safe[A](f : => Option[A]) : Option[A] =
    try { f } catch { case _ => None }

  def sure[A](f : => A) : Option[A] =
    try { Some(f) } catch { case _ => None }

  def notNull[A <: AnyRef](x : A, default : => A) : A =
    if(x eq null) default else x

  def using[A <: { def close() : Unit }, B](resource : A)(body : A => B) : B =
    try     { body(resource) }
    finally { resource.close() }
}
