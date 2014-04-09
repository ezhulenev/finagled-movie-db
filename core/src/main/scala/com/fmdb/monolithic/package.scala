package com.fmdb

import com.google.common.util.concurrent.ThreadFactoryBuilder
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

package object monolithic {

  private[monolithic] lazy val executor = {
    val threadFactory = new ThreadFactoryBuilder().setNameFormat("database-access-pool-%d").setDaemon(true).build()
    Executors.newCachedThreadPool(threadFactory)
  }

  private[monolithic] implicit val ec = ExecutionContext.fromExecutor(executor)
}
