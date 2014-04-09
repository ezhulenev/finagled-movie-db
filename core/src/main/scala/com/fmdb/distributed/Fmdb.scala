package com.fmdb.distributed

import org.jboss.netty.channel.{Channels, ChannelPipelineFactory}
import com.twitter.finagle.netty3.{Netty3Listener, Netty3Transporter}
import com.twitter.finagle.client.{Bridge, DefaultClient}
import com.twitter.finagle.dispatch.{SerialServerDispatcher, SerialClientDispatcher}
import com.twitter.finagle.server.DefaultServer
import com.fmdb.distributed.envelope.{EnvelopeEncoder, EnvelopeDecoder}

private[distributed] object Fmdb {
  import ServiceProtocol._
  import envelopeCodec._

  private[this] val ProtocolVersion: Long = 1l

  private[this] val ReqDecoder = versionCheckingEnvelopeToContentDecoder[FmdbReq](ProtocolVersion)

  private[this] val RepDecoder = versionCheckingEnvelopeToContentDecoder[FmdbRep](ProtocolVersion)

  private[this] val ReqEncoder = typeSafeEncoder[FmdbReq]

  private[this] val RepEncoder = typeSafeEncoder[FmdbRep]


  private[this] object FmdbServerPipeline extends ChannelPipelineFactory {
    def getPipeline = {
      val pipeline = Channels.pipeline()
      pipeline.addLast("envDecoder", new EnvelopeDecoder)
      pipeline.addLast("reqDecoder", ReqDecoder)
      pipeline.addLast("envEncoder", new EnvelopeEncoder(ProtocolVersion))
      pipeline.addLast("repEncoder", RepEncoder)
      pipeline
    }
  }

  private[this] object FmdbClientPipeline extends ChannelPipelineFactory {
    def getPipeline = {
      val pipeline = Channels.pipeline()
      pipeline.addLast("envEncode", new EnvelopeEncoder(ProtocolVersion))
      pipeline.addLast("reqEncode", ReqEncoder)
      pipeline.addLast("envDecode", new EnvelopeDecoder)
      pipeline.addLast("repDecode", RepDecoder)
      pipeline
    }
  }

  // Client objects

  private[this] object FmdbClientTransporter extends Netty3Transporter[FmdbReq, FmdbRep](
    "fmdbClientTransporter", FmdbClientPipeline
  )

  private[distributed] object Client extends DefaultClient[FmdbReq, FmdbRep](
    "fmdbClient", endpointer = {
      val bridge =
        Bridge[FmdbReq, FmdbRep, FmdbReq, FmdbRep](FmdbClientTransporter, new SerialClientDispatcher(_))

      (addr, stats) => bridge(addr, stats)
    }
  )

  // Server objects

  private[this] object FmdbListener extends Netty3Listener[FmdbRep, FmdbReq](
    "fmdbListener", FmdbServerPipeline
  )

  protected[distributed] object Server extends DefaultServer[FmdbReq, FmdbRep, FmdbRep, FmdbReq](
    "fmdbServer", FmdbListener, new SerialServerDispatcher(_, _)
  )
}
