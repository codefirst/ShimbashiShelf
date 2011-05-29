package org.codefirst.shimbashishelf

package object common {
  def tee[T](x : T)(f : T => Unit) : T = {
    try {f(x) } catch { case _ => () }
    x
  }
}
