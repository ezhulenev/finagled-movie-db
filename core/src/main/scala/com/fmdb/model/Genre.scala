package com.fmdb.model

sealed trait Genre

object Genre {
  case object Action      extends Genre
  case object Adventure   extends Genre
  case object Animation   extends Genre
  case object Biography   extends Genre
  case object Comedy      extends Genre
  case object Crime       extends Genre
  case object Documentary extends Genre
  case object Drama       extends Genre
  case object Thriller    extends Genre
  case object Western     extends Genre
}