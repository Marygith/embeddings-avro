package com.example.embeddingsencoderavro.example;

import com.example.embeddingsencoderavro.schema.Embedding;
import com.example.embeddingsencoderavro.util.Constants;
import org.apache.avro.util.Utf8;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class EmbeddingGenerator {
    private int embeddingSize = Constants.EMBEDDING_SIZE;

    private int randomSeedCounter = 0;
    private float[][] embeddingAsArray;


    public EmbeddingGenerator() {
        generateMap();
    }

    private final Map<String, Float> map = new HashMap<>();
    private final Map<CharSequence, Float> avroMap = new HashMap<>();

    private final Map<Utf8, Float> utf8AvroMap = new HashMap<>();
    private int id;

    private long avroId;
    private final Random rand = new Random(312);
    private final List<List<Float>> embeddingAsList = new ArrayList<>();

    private final AtomicInteger idCounter = new AtomicInteger();

    public void generateData() {
        generateEmbedding();
        generateEmbeddingAsList();
        generateId();
        generateMap();
        generateAvroId();
    }

    private void generateEmbedding() {
        embeddingAsArray = new float[embeddingsAMount][embeddingSize];
        for (int i = 0; i < embeddingsAMount; i++) {
            for (int k = 0; k < embeddingSize; k++) {
                embeddingAsArray[i][k] = rand.nextFloat();
            }
        }
    }

    private void generateMap() {
        map.clear();
        avroMap.clear();
        utf8AvroMap.clear();
        int mapSize = ThreadLocalRandom.current().nextInt(0, 1000);
        for (int i = 0; i < mapSize; i++) {
            map.put(RandomStringUtils.random(ThreadLocalRandom.current().nextInt(0, 50)), rand.nextFloat());
        }
        avroMap.putAll(map);
        utf8AvroMap.putAll(map.entrySet().stream().collect(Collectors.toMap(e -> new Utf8(e.getKey()), Map.Entry::getValue)));
    }

    private void generateId() {
        id = idCounter.incrementAndGet();
    }

    private void generateAvroId() {
        avroId =  UUID.randomUUID().getMostSignificantBits();
    }

    private void generateEmbeddingAsList() {
        embeddingAsList.clear();
        for (int i = 0; i < embeddingsAMount; i++) {
            embeddingAsList.add(new ArrayList<>());
            for (int k = 0; k < embeddingSize; k++) {
                embeddingAsList.get(i).add(rand.nextFloat());
            }
        }
    }

    public List<Embedding> generateNEmbeddings(int n) {
        generateData();
        List<Embedding> embeddings = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            generateData();
            embeddings.add(new Embedding(getEmbeddingAsArray(), embeddingSize, id, map));
        }
        return embeddings;
    }

    public int getEmbeddingsAMount() {
        return embeddingsAMount;
    }

    public int getEmbeddingSize() {
        return embeddingSize;
    }

    public float[][] getEmbeddingAsArray() {
        return embeddingAsArray;
    }

    public List<List<Float>> getEmbeddingAsList() {
        return embeddingAsList;
    }

    private int embeddingsAMount = 1;

    public void setEmbeddingsAMount(int embeddingsAMount) {
        this.embeddingsAMount = embeddingsAMount;
    }


    public int getId() {
        return id;
    }

    public long getAvroId() {
        return avroId;
    }
    public Map<String, Float> getMap() {
        return map;
    }

    public Map<CharSequence, Float> getAvroMap() {
        return avroMap;
    }

    public Map<Utf8, Float> getUtf8AvroMap() {
        return utf8AvroMap;
    }

}
