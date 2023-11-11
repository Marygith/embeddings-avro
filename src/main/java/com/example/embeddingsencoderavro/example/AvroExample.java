package com.example.embeddingsencoderavro.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.embeddingsencoderavro.schema.AvroEmbedding;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

public class AvroExample {
    public static long getTimeOfExperiment(EmbeddingGenerator generator, String fileName) {
        try {
            long start = System.currentTimeMillis();
            List<AvroEmbedding> embeddings = new ArrayList<>();
            populateEmbeddings(embeddings, generator);
            File file = new File(fileName);

            DatumWriter<AvroEmbedding> AvroEmbeddingDatumWriter = new SpecificDatumWriter<AvroEmbedding>(AvroEmbedding.class);
            DataFileWriter<AvroEmbedding> dataFileWriter = new DataFileWriter<AvroEmbedding>(AvroEmbeddingDatumWriter);

            dataFileWriter.create(embeddings.get(0).getSchema(), file);
            for (AvroEmbedding e : embeddings) {
                dataFileWriter.append(e);
            }
            dataFileWriter.close();

            DatumReader<AvroEmbedding> AvroEmbeddingDatumReader = new SpecificDatumReader<AvroEmbedding>(AvroEmbedding.class);
            AvroEmbedding avroEmbedding = null;
            try (DataFileReader<AvroEmbedding> dataFileReader = new DataFileReader<AvroEmbedding>(file, AvroEmbeddingDatumReader)) {
                int counter = 0;
                while (dataFileReader.hasNext()) {
                    avroEmbedding = dataFileReader.next(avroEmbedding);
                    if (!embeddings.get(counter).equals(avroEmbedding)) {
                        return -1;
                    }
                    counter++;
                }
            }

            return System.currentTimeMillis() - start;
        } catch (IOException e) {
            return -1;
        }
    }

    private static void populateEmbeddings(List<AvroEmbedding> list, EmbeddingGenerator generator) throws IOException {
        for (int i = 0; i < 50; i++) {
            generator.generateData();
            list.add(new AvroEmbedding(generator.getEmbeddingSize(), generator.getAvroId(), generator.getEmbeddingAsList()));
        }
    }
}
