package com.example.embeddingsencoderavro.exceptions;

public class EmbeddingNotFoundException extends RuntimeException{

    @Override
    public String getMessage() {
        return "Embedding with given id was not found";
    }
}
