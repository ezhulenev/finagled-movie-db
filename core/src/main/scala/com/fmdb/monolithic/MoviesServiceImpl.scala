package com.fmdb.monolithic

import com.fmdb.{MovieDatabaseAccess, MoviesService}
import com.fmdb.model.{Person, Genre}
import scala.concurrent.future

/**
 * Wrap blocking access layer into reactive API
 */
trait MoviesServiceImpl extends MoviesService {
  override def movies(person: Person) = future { MovieDatabaseAccess.movies(person) }

  override def movies(genre: Genre) = future { MovieDatabaseAccess.movies(genre) }

  override def movies(year: Int) = future { MovieDatabaseAccess.movies(year) }

  override def movies() = future { MovieDatabaseAccess.movies() }
}