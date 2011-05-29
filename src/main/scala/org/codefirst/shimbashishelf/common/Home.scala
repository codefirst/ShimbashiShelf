package org.codefirst.shimbashishelf.common
import java.io.File

object Home{
  def file(path : String, getenv : String => String = System.getenv) =
    tee( home(path, getenv) ) { case file =>
      new File(file.getParent()).mkdirs() }

  def dir(path : String, getenv : String => String = System.getenv) =
    tee( home(path, getenv) ) (_.mkdirs())

  private def home(path : String, getenv : String => String) = {
    val home = getenv("SHIMBASHI_SHELF_HOME")
    if( home != null && home != "")
      new File(home, path)
    else
      new File(path)
  }
}
