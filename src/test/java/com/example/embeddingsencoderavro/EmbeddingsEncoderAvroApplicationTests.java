package com.example.embeddingsencoderavro;

import com.example.embeddingsencoderavro.example.*;
import com.example.embeddingsencoderavro.schema.Embedding;
import com.example.embeddingsencoderavro.util.Constants;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import static com.example.embeddingsencoderavro.util.Constants.PATH_TO_EMBEDDINGS_DIRECTORY;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;


class EmbeddingsEncoderAvroApplicationTests {

    private final EmbeddingGenerator generator = new EmbeddingGenerator();

    private final String exampleFileName = PATH_TO_EMBEDDINGS_DIRECTORY + "embeddings.hasd";
    private final String avroFileName = PATH_TO_EMBEDDINGS_DIRECTORY + "embeddings.avro";

    private final EmbeddingsManager embeddingsManager = new EmbeddingsManager();

    @Test
    void evaluateMethodsWith_10000() {
            generator.setEmbeddingsAMount(10000);
//            long customMethodTime = Example.getTimeOfExperiment(generator, exampleFileName);
            long avroMethodTime = AvroExample.getTimeOfExperiment(generator, avroFileName);
//            assertTrue(customMethodTime != -1);
            assertTrue(avroMethodTime != -1);
            System.out.println("Avro method was completed in " + avroMethodTime + " millis");
//            System.out.println("Custom method was completed in " + customMethodTime + " millis");
    }

    @Test
    void save_100_EmbeddingsToDisk() {
        try {

            File directory = new File(PATH_TO_EMBEDDINGS_DIRECTORY);
            cleanDirectory(directory);
            assertTrue(checkThatDirectoryIsEmpty(PATH_TO_EMBEDDINGS_DIRECTORY));
            List<Embedding> embeddings = generator.generateNEmbeddings(100);
            embeddingsManager.saveEmbeddings(embeddings);
            assertFalse(checkThatDirectoryIsEmpty(PATH_TO_EMBEDDINGS_DIRECTORY));
                for (Embedding embedding : embeddings) {
                Embedding embeddingFromDisk = embeddingsManager.getEmbeddingById(embedding.getId());
                assertEquals(embeddingFromDisk, embedding);
            }
            cleanDirectory(directory);
        } catch (Exception e) {
            System.out.println("Oops, smth went wrong :( ");
            System.out.println(e.getMessage());
            fail();
        }
    }

    @Test
    void save_1000_EmbeddingsToDisk() {
        try{
            File directory = new File(PATH_TO_EMBEDDINGS_DIRECTORY);
            cleanDirectory(directory);
            assertTrue(checkThatDirectoryIsEmpty(PATH_TO_EMBEDDINGS_DIRECTORY));
            List<Embedding> embeddings = generator.generateNEmbeddings(1000);
            embeddingsManager.saveEmbeddings(embeddings);
            assertFalse(checkThatDirectoryIsEmpty(PATH_TO_EMBEDDINGS_DIRECTORY));
            for (Embedding embedding : embeddings) {
                Embedding embeddingFromDisk = embeddingsManager.getEmbeddingById(embedding.getId());
                assertEquals(embeddingFromDisk, embedding);
            }
            cleanDirectory(directory);
        } catch (Exception e) {
            System.out.println("Oops, smth went wrong :( ");
            System.out.println(e.getMessage());
            fail();
        }
    }

    private boolean checkThatDirectoryIsEmpty(String directoryName) {
        try (Stream<Path> stream = Files.list(Path.of(PATH_TO_EMBEDDINGS_DIRECTORY))){
            return stream.findAny().isEmpty();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void cleanDirectory(File directory) {
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
