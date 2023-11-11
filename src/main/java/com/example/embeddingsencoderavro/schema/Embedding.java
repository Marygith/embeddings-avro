package com.example.embeddingsencoderavro.schema;

import java.util.Arrays;
import java.util.Map;

public class Embedding {

    private int id;
    private final int embeddingSize;
    private float[][] embedding;
    private Map<String, Float> map;
    public Embedding(float[][] embedding, int embeddingSize, int id, Map<String, Float> map) {
        this.embedding = embedding;
        this.embeddingSize = embeddingSize;
        this.id = id;
        this.map = map;
    }


    public int getId() {
        return id;
    }

    public float[][] getEmbedding() {
        return embedding;
    }

    public int getEmbeddingSize() {
        return embeddingSize;
    }

    public void setEmbedding(float[][] embedding) {
        this.embedding = embedding;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String, Float> getMap() {
        return map;
    }

    public void setMap(Map<String, Float> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "Vector: " + Arrays.toString(embedding) + "\n\nVector size = " + embeddingSize;
    }

    @Override
    public boolean equals(Object e) {
        if (!(e instanceof Embedding)) return false;
        float[][] emb = ((Embedding) e).getEmbedding();
        if(((Embedding) e).getId() != id) return false;
        if(!((Embedding) e).getMap().equals(map)) return false;
        for (int i = 0; i < emb.length; i++) {
            if (!Arrays.equals(emb[i], getEmbedding()[i])) {
                return false;
            }
        }
        return true;
    }

//TODO Override hashcode
}
