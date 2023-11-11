package com.example.embeddingsencoderavro.jmh;

import com.example.embeddingsencoderavro.example.EmbeddingGenerator;
import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
public class ExecutionPlan {

    private final EmbeddingGenerator generator = new EmbeddingGenerator();
    @Param({ "10",  "100",  "1000", "10000"})
    public int embeddingsAmount;

    @Setup(Level.Invocation)
    public void setUp() {
        generator.generateData();
    }

    public EmbeddingGenerator getGenerator() {
        return generator;
    }
}
