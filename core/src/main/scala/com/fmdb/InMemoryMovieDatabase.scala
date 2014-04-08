package com.fmdb

import com.fmdb.model.{Cast, Person, Genre, Movie}
import org.joda.time.LocalDate

object InMemoryMovieDatabase {

  private val QuentinTarantino = Person("Quentin",   "Tarantino",  new LocalDate(1963,  3, 27))
  private val JamieFoxx        = Person("Jamie",     "Foxx",       new LocalDate(1967, 12, 13))
  private val ChristophWaltz   = Person("Christoph", "Waltz",      new LocalDate(1956, 10,  4))
  private val LeonardoDiCaprio = Person("Leonardo",  "DiCaprio",   new LocalDate(1974, 11, 11))
  private val KerryWashington  = Person("Kerry",     "Washington", new LocalDate(1977,  1, 31))
  private val BradPitt         = Person("Brad",      "Pitt",       new LocalDate(1963, 12, 18))

  private val People =
    Vector(QuentinTarantino, JamieFoxx, ChristophWaltz, LeonardoDiCaprio, KerryWashington, BradPitt)

  private val DjangoUnchained = Movie(
    title = "Django Unchained", genre = Genre.Western, year = 2012,
    directedBy = QuentinTarantino,
    cast = Vector(
      Cast(JamieFoxx, "Django"), Cast(ChristophWaltz, "Dr. King Schultz"),
      Cast(LeonardoDiCaprio, "Calvin Candie"), Cast(KerryWashington, "Broomhilda von Shaft"))
  )

  private val InglouriousBasterds = Movie(
    title = "Inglourious Basterds", genre = Genre.Adventure, year = 2009,
    directedBy = QuentinTarantino,
    cast = Vector(Cast(BradPitt, "Lt. Aldo Raine"), Cast(ChristophWaltz, "Col. Hans Landa"))
  )

  private val Movies = Vector(DjangoUnchained, InglouriousBasterds)

  def people(): Vector[Person] = People

  def movies(): Vector[Movie] = Movies

  def movies(year: Int): Vector[Movie] = Movies.filter(_.year == year)

  def movies(genre: Genre): Vector[Movie] = Movies.filter(_.genre == genre)

  def movies(person: Person): Vector[Movie] =
    Movies.filter(movie => movie.directedBy == person || movie.cast.exists(_.person == person))
}
