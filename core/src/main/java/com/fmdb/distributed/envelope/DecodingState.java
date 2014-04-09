package com.fmdb.distributed.envelope;

public enum DecodingState {
    VERSION,
    PAYLOAD_LENGTH,
    PAYLOAD
}
