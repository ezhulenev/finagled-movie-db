package com.fmdb.monolithic

import org.scalatest.FlatSpec
import scala.concurrent.Await
import scala.concurrent.duration._

class MoviesServiceSpec extends FlatSpec {

  "Movies Service" should "provide access to all movies" in {
    val service = new MoviesServiceImpl {}
    val movies = Await.result(service.movies(), 1.second)
    assert(movies.size == 2)
  }
}
