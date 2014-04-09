package com.fmdb

import scala.concurrent.Future
import com.fmdb.model.Person

trait PeopleService {
  def people(): Future[Vector[Person]]
}