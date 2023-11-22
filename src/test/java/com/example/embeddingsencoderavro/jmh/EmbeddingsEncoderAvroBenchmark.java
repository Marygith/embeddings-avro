package com.example.embeddingsencoderavro.jmh;

import com.example.embeddingsencoderavro.example.AvroExample;
import com.example.embeddingsencoderavro.example.EmbeddingGenerator;
import com.example.embeddingsencoderavro.example.Example;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@Fork(value = 1, warmups = 1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 1, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 1, time = 200, timeUnit = TimeUnit.MILLISECONDS)
public class EmbeddingsEncoderAvroBenchmark {


    private final String exampleFileName = "embeddings.hasd";
    private final String avroFileName = "embeddings.avro";
    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void testCustom(ExecutionPlan plan) {

        plan.getGenerator().setEmbeddingsAMount(plan.embeddingsAmount);
        Example.getTimeOfExperiment(plan.getGenerator(), exampleFileName);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void testAvro(ExecutionPlan plan) {

        plan.getGenerator().setEmbeddingsAMount(plan.embeddingsAmount);
        AvroExample.getTimeOfExperiment(plan.getGenerator(), avroFileName);
    }
}
