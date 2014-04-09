package com.fmdb.distributed

import com.fmdb.PeopleService
import scala.concurrent.Future
import com.fmdb.model.Person

trait PeopleServiceImpl extends PeopleService with FmdbServerConfig {
  import ServiceProtocol._

  def people(): Future[Vector[Person]] = client(GetPeople).map {
    case GotPeople(people) => people
    case err => sys.error(s"Unexpected server response: $err")
  }.toScala

}
