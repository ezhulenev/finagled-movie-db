package com.fmdb.distributed.envelope;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

// Custom protocol based on tutorial:
// http://biasedbit.com/netty-tutorial-replaying-decoder/

public class EnvelopeDecoder extends ReplayingDecoder<DecodingState> {
    private Envelope envelope;

    public EnvelopeDecoder() {
        reset();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, DecodingState state) throws Exception {
        switch (state) {
            case VERSION:
                this.envelope.setVersion(buffer.readLong());
                checkpoint(DecodingState.PAYLOAD_LENGTH);

            case PAYLOAD_LENGTH:
                int size = buffer.readInt();
                if (size <= 0) {
                    throw new RuntimeException("Invalid payload size " + size);
                }
                byte[] payload = new byte[size];
                this.envelope.setPayload(payload);

            case PAYLOAD:
                buffer.readBytes(this.envelope.getPayload(), 0, this.envelope.getPayload().length);
                try {
                    return this.envelope;
                } finally {
                    this.reset();
                }

            default:
                throw new Exception("Unknown decoding state: " + state);
        }

    }

    private void reset() {
        checkpoint(DecodingState.VERSION);
        this.envelope = new Envelope();
    }


}