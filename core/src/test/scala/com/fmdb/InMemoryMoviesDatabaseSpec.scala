package com.fmdb

import org.scalatest.FlatSpec
import com.fmdb.model.Genre

class InMemoryMoviesDatabaseSpec extends FlatSpec {

  "In Memory Movies Database" should "correctly return movies for Quentin Tarantino" in {
    val tarantino = InMemoryMovieDatabase.people().find(_.firstName == "Quentin").head
    val tarantinoMovies = InMemoryMovieDatabase.movies(tarantino)

    assert(tarantinoMovies.size == 2)
  }

  it should "find Adventure movies" in {
    val adventureMovies = InMemoryMovieDatabase.movies(Genre.Adventure)
    assert(adventureMovies.size == 1)
    assert(adventureMovies.head.title == "Inglourious Basterds")
  }
}
