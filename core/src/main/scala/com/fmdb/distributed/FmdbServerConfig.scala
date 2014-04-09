package com.fmdb.distributed

import java.net.SocketAddress
import com.twitter.finagle.Name
import com.twitter.finagle.service.{TimeoutFilter, RetryPolicy, RetryingFilter}
import com.twitter.finagle.util.DefaultTimer
import com.twitter.util.Duration

trait FmdbServerConfig {
  def serverAddress: SocketAddress

  import ServiceProtocol._

  private[this] val retry = new RetryingFilter[FmdbReq, FmdbRep](
    retryPolicy = RetryPolicy.tries(3),
    timer = DefaultTimer.twitter
  )

  private[this] val timeout = new TimeoutFilter[FmdbReq, FmdbRep](
    timeout = Duration.fromSeconds(10),
    timer = DefaultTimer.twitter
  )

  protected lazy val client =
    retry      andThen
    timeout    andThen
    Fmdb.Client.newService(Name.bound(serverAddress), "fmdbClient")
}
