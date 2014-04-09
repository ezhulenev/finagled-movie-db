package com.fmdb

import org.scalatest.FlatSpec
import com.fmdb.model.Genre

class MovieDatabaseAccessSpec extends FlatSpec {

  "In Memory Movies Database" should "correctly return movies for Quentin Tarantino" in {
    val tarantino = MovieDatabaseAccess.people().find(_.firstName == "Quentin").head
    val tarantinoMovies = MovieDatabaseAccess.movies(tarantino)

    assert(tarantinoMovies.size == 2)
  }

  it should "find Adventure movies" in {
    val adventureMovies = MovieDatabaseAccess.movies(Genre.Adventure)
    assert(adventureMovies.size == 1)
    assert(adventureMovies.head.title == "Inglourious Basterds")
  }
}
