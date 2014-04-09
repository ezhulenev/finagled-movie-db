package com.fmdb.monolithic

import com.fmdb.{MovieDatabaseAccess, PeopleService}
import scala.concurrent.future

/**
 * Wrap blocking access layer into reactive API
 */
trait PeopleServiceImpl extends PeopleService {
  override def people() = future { MovieDatabaseAccess.people() }
}
