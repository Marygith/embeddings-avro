package com.example.embeddingsencoderavro.example;

import com.example.embeddingsencoderavro.decoder.DataReader;
import com.example.embeddingsencoderavro.decoder.EmbeddingReader;
import com.example.embeddingsencoderavro.encoder.DataWriter;
import com.example.embeddingsencoderavro.encoder.EmbeddingWriter;
import com.example.embeddingsencoderavro.schema.Embedding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Example {

    public static long getTimeOfExperiment(EmbeddingGenerator generator, String fileName) {
        try {

            List<Embedding> embeddings = new ArrayList<>();
            populateEmbeddings(embeddings, generator);
            long start = System.currentTimeMillis();
            DataWriter<Embedding> writer = new EmbeddingWriter();

            File file = new File(fileName);
            writer.create(file);
            for (Embedding e : embeddings) {
                writer.addData(e);
            }
            writer.close();

            DataReader<Embedding> reader = new EmbeddingReader(new File(fileName));
            Embedding embedding = new Embedding(null, generator.getEmbeddingSize(), generator.getId(), generator.getMap());


            int counter = 0;
            while (reader.hasNext()) {
                reader.readData(embedding);
                if (!embedding.equals(embeddings.get(counter))) {
                    return -1;
                }
                counter++;
            }
            reader.close();
            return System.currentTimeMillis() - start;
        } catch (IOException e) {
            return -1;
        }

    }

    private static void populateEmbeddings(List<Embedding> list, EmbeddingGenerator generator) throws IOException {
        for (int i = 0; i < 50; i++) {
            generator.generateData();
            list.add(new Embedding(generator.getEmbeddingAsArray(), generator.getEmbeddingSize(), generator.getId(), generator.getMap()));
        }
    }
}
