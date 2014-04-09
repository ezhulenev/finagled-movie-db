package com.fmdb.distributed

import com.fmdb.distributed.envelope.Envelope

import org.jboss.netty.handler.codec.oneone.{OneToOneEncoder, OneToOneDecoder}
import org.jboss.netty.channel.{Channel, ChannelHandlerContext}

import sbinary.Operations._
import sbinary.{Writes, Reads}
import scala.reflect._

package object envelopeCodec {
  def versionCheckingEnvelopeToContentDecoder[T <: AnyRef : Reads](protocolVersion: Long) = new OneToOneDecoder {
    def decode(ctx: ChannelHandlerContext, channel: Channel, msg: AnyRef) = {
      if (!msg.isInstanceOf[Envelope]) {
        msg
      } else {
        val envelope = msg.asInstanceOf[Envelope]
        assert(envelope.getVersion == protocolVersion,
          s"Wrong protocol version '${envelope.getVersion}', expected '$protocolVersion'}")

        val bytes = envelope.getPayload
        fromByteArray[T](bytes)
      }
    }
  }

  def typeSafeEncoder[T : Writes : ClassTag] = new OneToOneEncoder {
    def encode(ctx: ChannelHandlerContext, channel: Channel, msg: AnyRef) = encodeTypeSafely[T](msg)
  }

  def encodeTypeSafely[T : Writes : ClassTag](msg: AnyRef) = {
    if (!classTag[T].runtimeClass.isAssignableFrom(msg.getClass)) {
      msg
    } else {
      toByteArray(msg.asInstanceOf[T])
    }
  }
}