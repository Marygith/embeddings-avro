package com.example.embeddingsencoderavro.encoder;

import com.example.embeddingsencoderavro.schema.Embedding;

import java.io.*;
import java.util.Map;

public class EmbeddingEncoder extends DataEncoder<Embedding> {
    private final byte[] buffer = new byte[8];

    public EmbeddingEncoder(FileOutputStream fos) {
        out = new BufferedOutputStream(fos);
    }

    private final BufferedOutputStream out;

    @Override
    public void writeData(Embedding embedding) throws IOException {

        int amount = embedding.getEmbedding().length;
        float[][] vectors = embedding.getEmbedding();
        writeInt(embedding.getId());
        writeUnsignedInt(amount);
        int size = embedding.getEmbeddingSize();
        for (int i = 0; i < amount; i++) {
            writeFloatArray(vectors[i], size);
            writeZero();
            out.flush();
        }
        writeZero();
        int mapSize = embedding.getMap().size();
        writeUnsignedInt(mapSize);
        for(Map.Entry<String, Float> entry: embedding.getMap().entrySet()) {
            writeString(entry.getKey());
            writeFloat(entry.getValue());
        }
    }

//    @Override
    protected void writeFloat(float data) throws IOException {
        encodeFloat(data, buffer);
        out.write(buffer, 0, 4);
    }

//    @Override
    public void writeInt(int data) throws IOException {
        int val = (data << 1) ^ (data >> 31);
        if (valueIsSmall(val)) return;
        int len = encodeInt(data, buffer);
        out.write(buffer, 0, len);
    }



    public void writeUnsignedInt(int data) throws IOException {
        if (valueIsSmall(data)) return;
        int len = encodeUnsignedInt(data, buffer);
        out.write(buffer, 0, len);
    }

    private boolean valueIsSmall(int data) throws IOException {
        if ((data & ~0x7F) == 0) {
            out.write(data);
            return true;
        } else if ((data & ~0x3FFF) == 0) {
            out.write(0x80 | data);
            out.write(data >>> 7);
            return true;
        }
        return false;
    }


    @Override
    public void close() throws IOException {
        out.flush();
        out.close();
    }

    @Override
    public void writeBytes(byte[] bytes, int start, int len) throws IOException {
        out.write(bytes);
    }

    protected void writeZero() throws IOException {
        out.write(0);
    }

    private void writeFloatArray(float[] arr, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            writeFloat(arr[i]);
        }
    }

    private void writeString(String string) throws IOException {
        byte[] arr = string.getBytes();
        writeUnsignedInt(arr.length);
        out.write(arr);
    }
}
