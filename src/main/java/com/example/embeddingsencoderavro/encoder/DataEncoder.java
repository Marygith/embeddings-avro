package com.example.embeddingsencoderavro.encoder;

import java.io.FileOutputStream;
import java.io.IOException;

public abstract class DataEncoder<T> {

    public abstract void writeData(T data) throws IOException;

    public abstract void close() throws IOException;

    protected void encodeFloat(float f, byte[] buffer) {
        final int bits = Float.floatToRawIntBits(f);
        buffer[3] = (byte) (bits >>> 24);
        buffer[2] = (byte) (bits >>> 16);
        buffer[1] = (byte) (bits >>> 8);
        buffer[0] = (byte) (bits);
    }

    protected int encodeInt(int n, byte[] buf) {
        n = (n << 1) ^ (n >> 31);
        return encodeUnsignedInt(n, buf);
    }

    protected int encodeUnsignedInt(int n, byte[] buf/*, int pos*/) {
        int pos = 0, start = 0;
        if ((n & ~0x7F) != 0) {
            do {
                buf[pos++] = (byte) ((n | 0x80) & 0xFF);
                n >>>= 7;
            } while (n > 0x7F && pos < 4);
        }
        buf[pos++] = (byte) n;
        return pos - start;
    }



//    protected abstract void writeZero() throws IOException;

    protected abstract void writeBytes(byte[] bytes, int start, int len) throws IOException;
}
