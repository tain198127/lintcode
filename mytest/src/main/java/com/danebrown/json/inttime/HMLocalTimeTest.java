package com.danebrown.json.inttime;

import com.danebrown.groovy.ScriptPerformanceCompare;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.All)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 2)
@Threads(4)
@Fork(1)
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class HMLocalTimeTest {
    private List<Long> data = new ArrayList<>();
    private List<String> strData = new ArrayList<>();
    ThreadLocalRandom random = ThreadLocalRandom.current();
    // 在每个试验开始前初始化数据
    @Setup(Level.Trial)
    public void setUp() {
        data = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            long hour =  random.nextInt(0,23) * 10000;
            long min  = random.nextInt(0,59) * 100;
            long sec  = random.nextInt(0,59);
            long time = hour + min + sec;
            strData.add(String.valueOf(time));
            data.add(time);
        }
    }
    public static ObjectMapper mapper = new ObjectMapper();
    @Benchmark
    public void getTimeString(Blackhole blackhole) throws JsonProcessingException {
        int idx = random.nextInt(0,data.size());

        LocalTimeTest.TimeDto dto = new LocalTimeTest.TimeDto(data.get(idx));


        String json = mapper.writeValueAsString(dto);
        blackhole.consume(json);
    }
    @Benchmark
    public void getTime(Blackhole blackhole) throws JsonProcessingException {
        int idx = random.nextInt(0,data.size());
        String time = strData.get(idx);
        String inputJson = String.format("{\"time\":%s}",time);
        LocalTimeTest.TimeDto parsedDto = mapper.readValue(inputJson, LocalTimeTest.TimeDto.class);
        long t = parsedDto.getTime();
        blackhole.consume(t);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(HMLocalTimeTest.class.getSimpleName())
                .result("result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

}
