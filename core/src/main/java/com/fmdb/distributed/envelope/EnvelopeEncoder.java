package com.fmdb.distributed.envelope;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class EnvelopeEncoder extends OneToOneEncoder {
    private final Long version;

    public EnvelopeEncoder(Long version) {
        this.version = version;
    }

    private ChannelBuffer wrapPayload(byte[] payload) throws IllegalArgumentException {
        ChannelBuffer buffer = ChannelBuffers.buffer(8 + 4 + payload.length);
        buffer.writeLong(version);
        buffer.writeInt(payload.length);
        buffer.writeBytes(payload);

        return buffer;
    }

    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        if (msg instanceof byte[]) {
            return wrapPayload((byte[]) msg);
        } else {
            return msg;
        }
    }
}
