package com.example.embeddingsencoderavro.encoder;

import com.example.embeddingsencoderavro.schema.Embedding;
import com.example.embeddingsencoderavro.util.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class EmbeddingWriter extends DataWriter<Embedding> {


    @Override
    public void create(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        dataEncoder = new EmbeddingEncoder(fos);
        dataEncoder.writeBytes(Constants.MAGIC, 0, 4);
    }

    @Override
    public void addData(Embedding data) throws IOException {
        dataEncoder.writeData(data);
    }

    @Override
    public void close() throws IOException {
        dataEncoder.close();
    }

}
