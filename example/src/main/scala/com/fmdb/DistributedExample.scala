package com.fmdb

import com.fmdb.distributed.{PeopleServiceImpl, FmdbServer}
import java.net.{SocketAddress, InetSocketAddress}
import com.fmdb.monolithic.MoviesServiceImpl
import scala.concurrent.duration._
import scala.concurrent.Await

object DistributedExample extends App {

  val address = new InetSocketAddress(10000)

  // Lets start server
  val server = FmdbServer.serve(address)

  // Lets create services

  object Services extends PeopleServiceImpl with MoviesServiceImpl {
    val serverAddress: SocketAddress = address
  }

  // Lets' get all movies for Leonardo DiCaprio

  val leo = Await.result(Services.people(), 1.second).find(_.firstName == "Leonardo").get
  val leoMovies = Await.result(Services.movies(leo), 1.second)


  println(s"Movies with $leo:")
  leoMovies.foreach(println)

  // Shutdown
  server.close()
  System.exit(0)
}