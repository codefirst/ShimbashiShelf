package org.codefirst.shimbashishelf

import org.eclipse.jgit.api._
import org.eclipse.jgit.lib._
import java.io.File
import java.util.Calendar
import java.text.SimpleDateFormat

class VersionControl(repositoryDir : File) {
  val repository : Repository = new RepositoryBuilder().setGitDir(new File(repositoryDir.getAbsolutePath() + File.separatorChar + Constants.DOT_GIT)).readEnvironment().findGitDir().build()

  def commit(file : File) : Boolean = {
    if (!new File(repositoryDir.getAbsolutePath() + File.separatorChar + Constants.DOT_GIT).exists()) {
      println(new File(repositoryDir.getAbsolutePath() + File.separatorChar + Constants.DOT_GIT).getAbsolutePath() + " created")
      Git.init().setDirectory(repositoryDir).call()
    }

    val git : Git = new Git(repository)
    println(file.getPath())

    // TODO: getIndex() is deprecated, but AddCommand does not work... 
    val index = repository.getIndex()
    index.add(repositoryDir, file)
    index.write()

    val format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    // TODO: Author configuration 
    git.commit().setAuthor("ShimbashiShelf", "ShimbashiShelf@codefirst.org").setMessage(format.format(Calendar.getInstance().getTime())).call()
    return true
  }

}
