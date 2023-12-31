package com.example.embeddingsencoderavro.decoder;



import java.io.File;

import java.io.IOException;

public abstract class DataReader<T> {

    protected DataDecoder<T> dataDecoder;

    public abstract void readData(T data) throws IOException;

    public abstract boolean hasNext() throws IOException;


    public abstract void close() throws IOException;

}
