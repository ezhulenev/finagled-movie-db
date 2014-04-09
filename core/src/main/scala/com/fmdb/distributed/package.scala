package com.fmdb

import scala.concurrent.{ExecutionContext, Future => ScalaFuture, Promise => ScalaPromise}
import com.twitter.util.{Throw, Return, Future => TwitterFuture, Promise => TwitterPromise}
import scala.util.{Failure, Success}

package object distributed {

  implicit class RichScalaFuture[A](scalaFuture: ScalaFuture[A])(implicit ec: ExecutionContext) {
    def toTwitter: TwitterFuture[A] = {
      val promise = new TwitterPromise[A]
      scalaFuture.onComplete {
        case Success(a) => promise.setValue(a)
        case Failure(err) => promise.setException(err)
      }
      promise
    }
  }

  implicit class RichTwitterFuture[A](twitterFuture: TwitterFuture[A]) {
    def toScala: ScalaFuture[A] = {
      val promise = ScalaPromise[A]()
      twitterFuture respond {
        case Return(a) => promise success a
        case Throw(e) => promise failure e
      }
      promise.future
    }
  }

}
