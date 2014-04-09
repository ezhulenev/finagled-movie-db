package com.fmdb

import com.fmdb.model.{Person, Genre, Movie}
import scala.concurrent.Future

trait MoviesService {
  def movies(): Future[Vector[Movie]]

  def movies(year: Int): Future[Vector[Movie]]

  def movies(genre: Genre): Future[Vector[Movie]]

  def movies(person: Person): Future[Vector[Movie]]
}