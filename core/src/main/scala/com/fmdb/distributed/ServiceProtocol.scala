package com.fmdb.distributed

import com.fmdb.model.{Movie, Person, Genre}

private[distributed] object ServiceProtocol extends ModelProtocol {

  import sbinary.DefaultProtocol._
  import sbinary.Operations._
  import sbinary._

  sealed trait FmdbReq

  case object GetPeople extends FmdbReq

  case class GetMovies(year: Option[Int] = None, genre: Option[Genre] = None, person: Option[Person] = None) extends FmdbReq


  sealed trait FmdbRep

  case class GotPeople(people: Vector[Person]) extends FmdbRep

  case class GotMovies(movies: Vector[Movie]) extends FmdbRep


  implicit object requestFormat extends Format[FmdbReq] {
    override def reads(in: Input) = read[Byte](in) match {
      case 0 => GetPeople
      case 1 => GetMovies(read[Option[Int]](in), read[Option[Genre]](in), read[Option[Person]](in))
    }

    override def writes(out: Output, req: FmdbReq) {
      req match {
        case GetPeople =>
          write[Byte](out, 0)

        case GetMovies(year, genre, person) =>
           write[Byte](out, 1)
           write[Option[Int]](out, year)
           write[Option[Genre]](out, genre)
           write[Option[Person]](out, person)
      }
    }
  }

  implicit object responseFormat extends Format[FmdbRep] {
    override def reads(in: Input) = read[Byte](in) match {
      case 0 => GotPeople(read[List[Person]](in).toVector)
      case 1 => GotMovies(read[List[Movie]](in).toVector)
    }

    override def writes(out: Output, rep: FmdbRep) {
      rep match {
        case GotPeople(people) =>
          write[Byte](out, 0)
          write[List[Person]](out, people.toList)

        case GotMovies(movies) =>
          write[Byte](out, 1)
          write[List[Movie]](out, movies.toList)
      }
    }
  }
}
