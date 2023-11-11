package com.example.embeddingsencoderavro.example;

import com.example.embeddingsencoderavro.decoder.DataReader;
import com.example.embeddingsencoderavro.decoder.EmbeddingReader;
import com.example.embeddingsencoderavro.encoder.DataWriter;
import com.example.embeddingsencoderavro.encoder.EmbeddingWriter;
import com.example.embeddingsencoderavro.exceptions.EmbeddingNotFoundException;
import com.example.embeddingsencoderavro.schema.Embedding;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.embeddingsencoderavro.util.Constants.*;

public class EmbeddingsManager {


    public void saveEmbeddings(List<Embedding> embeddings) {
        Map<String, List<Embedding>> fileNameToEmbeddings = embeddings.stream()
                .collect(Collectors.groupingByConcurrent(
                        embedding -> createFileName(hashId(embedding.getId()))
                ));

        fileNameToEmbeddings.forEach(this::saveEmbeddingsToFile);
    }


    public Embedding getEmbeddingById(long id) throws IOException {
        String fileName = createFileName(hashId((int) id)); // TODO Change id type to long
        DataReader<Embedding> reader = new EmbeddingReader(new File(fileName));
        Embedding embedding = new Embedding(null, EMBEDDING_SIZE, (int) id, null);
        while (reader.hasNext()) {
            reader.readData(embedding);
            if (embedding.getId() == id) {
                reader.close();
                return embedding;
            }
        }
        reader.close();

        throw new EmbeddingNotFoundException();
    }

    private long hashId(int id) {
        return id % SHARDING_COEFFICIENT;
    }

    private String createFileName(long base) {
        return PATH_TO_EMBEDDINGS_DIRECTORY + base + FILE_ENDING;
    }


    private void saveEmbeddingsToFile(String fileName, List<Embedding> embeddings) {
        DataWriter<Embedding> writer = new EmbeddingWriter();
        File file = new File(fileName);
        try {
            writer.create(file);
            for (Embedding e : embeddings) {
                writer.addData(e);
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
