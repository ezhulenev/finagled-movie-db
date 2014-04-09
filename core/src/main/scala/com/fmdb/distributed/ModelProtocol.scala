package com.fmdb.distributed

import com.fmdb.model.{Movie, Cast, Person, Genre}
import org.joda.time.LocalDate
import org.joda.time.format.ISODateTimeFormat

/**
 * SBinary format for model objects
 */
private[distributed] trait ModelProtocol {

  import sbinary.DefaultProtocol._
  import sbinary.Operations._
  import sbinary._

  private[this] val DateFmt = ISODateTimeFormat.date()

  implicit object dateFormat extends Format[LocalDate] {
    def reads(in: Input): LocalDate = new LocalDate(DateFmt.parseLocalDate(read[String](in)))

    def writes(out: Output, value: LocalDate) {
      write[String](out, DateFmt.print(value))
    }
  }

  implicit object genreFormat extends Format[Genre] {
    import Genre._

    override def reads(in: Input): Genre = read[Byte](in) match {
      case 0 => Action
      case 1 => Adventure
      case 2 => Animation
      case 3 => Biography
      case 4 => Comedy
      case 5 => Crime
      case 6 => Documentary
      case 7 => Drama
      case 8 => Thriller
      case 9 => Western
    }

    override def writes(out: Output, value: Genre) = {
      val genreCode: Byte = value match {
        case Action      => 0
        case Adventure   => 1
        case Animation   => 2
        case Biography   => 3
        case Comedy      => 4
        case Crime       => 5
        case Documentary => 6
        case Drama       => 7
        case Thriller    => 8
        case Western     => 9
      }
      write[Byte](out, genreCode)
    }
  }

  implicit object personFormat extends Format[Person] {
    override def reads(in: Input) =
      Person(read[String](in), read[String](in), read[LocalDate](in))

    override def writes(out: Output, person: Person) {
      write[String](out, person.firstName)
      write[String](out, person.secondName)
      write[LocalDate](out, person.born)
    }
  }

  implicit object castFormat extends Format[Cast] {
    override def reads(in: Input) =
      Cast(read[Person](in), read[String](in))

    override def writes(out: Output, cast: Cast) = {
      write[Person](out, cast.person)
      write[String](out, cast.as)
    }
  }

  implicit object movieFormat extends Format[Movie] {
    override def reads(in: Input) =
      Movie(read[String](in), read[Genre](in), read[Int](in), read[Person](in), read[List[Cast]](in).toVector)

    override def writes(out: Output, movie: Movie) {
      write[String](out, movie.title)
      write[Genre](out, movie.genre)
      write[Int](out, movie.year)
      write[Person](out, movie.directedBy)
      write[List[Cast]](out, movie.cast.toList)
    }
  }
}