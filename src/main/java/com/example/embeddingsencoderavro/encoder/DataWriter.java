package com.example.embeddingsencoderavro.encoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class DataWriter<T> {

//    protected FileOutputStream fileOutputStream;
    protected DataEncoder<T> dataEncoder;
    public abstract void create(File file) throws IOException;

    public abstract void addData(T data) throws IOException;

    public abstract void close() throws IOException;


}
