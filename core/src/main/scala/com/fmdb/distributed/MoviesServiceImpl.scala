package com.fmdb.distributed

import com.fmdb.MoviesService
import scala.concurrent.Future
import com.fmdb.model.{Person, Genre, Movie}

trait MoviesServiceImpl extends MoviesService with FmdbServerConfig {
  import ServiceProtocol._

  private[this] val parseResponse: PartialFunction[FmdbRep, Vector[Movie]] = {
    case GotMovies(movies) => movies
    case err => sys.error(s"Unexpected server response: $err")
  }

  override def movies(): Future[Vector[Movie]] =
    client(GetMovies()).map(parseResponse).toScala

  override def movies(person: Person): Future[Vector[Movie]] =
    client(GetMovies(person = Some(person))).map(parseResponse).toScala

  override def movies(genre: Genre): Future[Vector[Movie]] =
    client(GetMovies(genre = Some(genre))).map(parseResponse).toScala

  override def movies(year: Int): Future[Vector[Movie]] =
    client(GetMovies(year = Some(year))).map(parseResponse).toScala
}
