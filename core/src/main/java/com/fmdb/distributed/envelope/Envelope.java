package com.fmdb.distributed.envelope;

public class Envelope {
    private Long version;
    private byte[] payload;

    public Envelope() {

    }

    public Envelope(Long version, byte[] payload) {
        this.version = version;
        this.payload = payload;
    }


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
