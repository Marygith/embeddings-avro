package com.example.embeddingsencoderavro.decoder;

import com.example.embeddingsencoderavro.schema.Embedding;
import org.apache.avro.util.Utf8;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.embeddingsencoderavro.util.Constants.MAGIC;

public class EmbeddingDecoder extends DataDecoder<Embedding> {

    private final BufferedInputStream is;
    private final byte[] buf = new byte[4];

    public EmbeddingDecoder(File file) {
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            if (is.available() < MAGIC.length) {
                System.out.println("very baad");
            }
            byte[] magic = new byte[MAGIC.length];
            readBytes(magic, 0, MAGIC.length);
            if (!Arrays.equals(MAGIC, magic))
                throw new RuntimeException("Not an Avro data file.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Embedding readData(Embedding embedding) throws IOException {
        int id = readInt(is);
        int amount = readUnsignedInt(is);
//        int amount = readInt();
        float[][] vectors = new float[amount][embedding.getEmbeddingSize()];
        int size = embedding.getEmbeddingSize();
        for (int i = 0; i < amount; i++) {
            vectors[i] = readFloatArray(size);
            if (readUnsignedInt(is) != 0) throw new RuntimeException();
        }
        if (readUnsignedInt(is) != 0) throw new RuntimeException();
        Map<String, Float> map = new HashMap<>();
        int mapSize = readUnsignedInt(is);
        for(int i = 0; i < mapSize; i++) {
            map.put(readString(), readFloat(is));
        }
        embedding.setMap(map);
        embedding.setEmbedding(vectors);
        embedding.setId(id);
        return embedding;
    }

    @Override
    protected void readBytes(byte[] bytes, int start, int length) throws IOException {
        if (length < 0)
            throw new RuntimeException("Malformed data. Length is negative: " + length);
        is.read(bytes);
    }

    public int readInt(BufferedInputStream is) throws IOException {
        int n = decodeInt(is);
        return (n >>> 1) ^ -(n & 1);
    }
    public int readUnsignedInt(BufferedInputStream is) throws IOException {
        return decodeInt(is);
    }

    private float[] readFloatArray(int size) throws IOException {
        float[] arr = new float[size];
        for (int i = 0; i < size; i++) {
            arr[i] = readFloat(is);
        }
        return arr;
    }

    @Override
    public BufferedInputStream getIs() {
        return is;
    }

    @Override
    public void close() throws IOException {
        is.close();
    }

    private String readString() throws IOException {
        int length = readUnsignedInt(is);
        byte[] arr = is.readNBytes(length);
        return new String(arr);
    }
}
