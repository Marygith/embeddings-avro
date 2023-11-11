package com.example.embeddingsencoderavro.decoder;

import com.example.embeddingsencoderavro.schema.Embedding;

import java.io.File;
import java.io.IOException;


public class EmbeddingReader extends DataReader<Embedding> {


    public EmbeddingReader(File file) {
        dataDecoder = new EmbeddingDecoder(file);
    }

    @Override
    public void readData(Embedding data) throws IOException {
        dataDecoder.readData(data);
    }

    @Override
    public boolean hasNext() throws IOException {
        return dataDecoder.getIs().available() > 0;
    }

    @Override
    public void close() throws IOException {
        dataDecoder.close();
    }

}
