package com.example.embeddingsencoderavro.decoder;

import com.example.embeddingsencoderavro.schema.Embedding;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;

public abstract class DataDecoder<T> {
    protected abstract Embedding readData(T data) throws IOException;

    protected  int decodeInt(BufferedInputStream is) throws IOException {
        int n = 0;
        int b;
        int shift = 0;
        do {
            b = is.read();
            if (b >= 0) {
                n |= (b & 0x7F) << shift;
                if ((b & 0x80) == 0) {
                    return n;
                }
            } else {
                throw new EOFException();
            }
            shift += 7;
        } while (shift < 32);
        throw new RuntimeException("Invalid int encoding");

    };

    public float readFloat(BufferedInputStream is) throws IOException {
        byte[] buf = new byte[4];
        is.read(buf);
        int n = (((int) buf[0]) & 0xff) | ((((int) buf[1]) & 0xff) << 8) | ((((int) buf[2]) & 0xff) << 16)
                | ((((int) buf[3]) & 0xff) << 24);
        return Float.intBitsToFloat(n);
    }

    protected abstract void readBytes(byte[] bytes, int start, int len) throws IOException;

    public abstract BufferedInputStream getIs();

    public abstract void close() throws IOException;
}
