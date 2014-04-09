package com.fmdb.distributed

import java.net.SocketAddress
import com.twitter.finagle.Name

trait FmdbServerConfig {
  def serverAddress: SocketAddress

  protected lazy val client = Fmdb.Client.newService(Name.bound(serverAddress), "fmdbClient")
}
