package com.fmdb.distributed

import com.twitter.finagle.Service
import com.twitter.util.Future
import com.fmdb.MovieDatabaseAccess
import java.net.SocketAddress

object FmdbServer {
  import Fmdb._
  import ServiceProtocol._

  private[this] val service = new Service[FmdbReq, FmdbRep] {
    override def apply(request: FmdbReq): Future[FmdbRep] =
      Future { handleRequest(request) }

    private def handleRequest(req: FmdbReq): FmdbRep = req match {
      case GetPeople                           => GotPeople(MovieDatabaseAccess.people())
      case GetMovies(None, None, None)         => GotMovies(MovieDatabaseAccess.movies())
      case GetMovies(Some(year), None, None)   => GotMovies(MovieDatabaseAccess.movies(year))
      case GetMovies(None, Some(genre), None)  => GotMovies(MovieDatabaseAccess.movies(genre))
      case GetMovies(None, None, Some(person)) => GotMovies(MovieDatabaseAccess.movies(person))

      case _ => sys.error(s"Unexpected service request: $req")
    }
  }

  def serve(address: SocketAddress) = Server.serve(address, service)
}