package com.fmdb

import com.fmdb.monolithic.PeopleServiceImpl
import com.fmdb.monolithic.MoviesServiceImpl
import scala.concurrent.Await
import scala.concurrent.duration._

object MonolithicExample extends App {
  // Lets create services
  object Services extends PeopleServiceImpl with MoviesServiceImpl

  // Lets' get all movies for Leonardo DiCaprio
  val leo = Await.result(Services.people(), 1.second).find(_.firstName == "Leonardo").get
  val leoMovies = Await.result(Services.movies(leo), 1.second)

  println(s"Movies with ${leo.firstName} ${leo.secondName}:")
  leoMovies.map(m => s" - ${m.title}, ${m.year}").foreach(println)

  // Shutdown
  System.exit(0)
}