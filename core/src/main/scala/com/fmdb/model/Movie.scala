package com.fmdb.model

case class Cast(person: Person, as: String)

case class Movie(title: String, genre: Genre, year: Int, directedBy: Person, cast: Vector[Cast])