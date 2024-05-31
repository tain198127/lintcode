package com.danebrown.jvm;

import cn.hutool.core.lang.Holder;
import cn.hutool.core.lang.UUID;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.All)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 2)
@Threads(4)
//@Fork(1,jvmArgsAppend = {"Xmx1024m","Xms1024m","-XX:+UseG1GC","-XX:+PrintGCDetails","-XX:+PrintGCDateStamps","-XX:+PrintHeapAtGC","-XX:+PrintTenuringDistribution","-XX:+PrintGCApplicationStoppedTime","-XX:+PrintGCApplicationConcurrentTime","-XX:+PrintAdaptiveSizePolicy","-XX:+PrintReferenceGC","-XX:+PrintFlagsFinal","-XX:+PrintFlagsInitial","-XX:+PrintCommandLineFlags","-XX:+PrintNMTStatistics","-XX:+PrintSafepointStatistics","-XX:+PrintCompilation","-XX:+PrintStubCode","-XX:+PrintExceptionDetails","-XX:+PrintOopAddress","-XX:+PrintParallelOldGCPhaseTimes","-XX:+PrintStringTableStatistics","-XX:+PrintVMOptions","-XX:+PrintCompilation","-XX:+PrintInlining","-XX:+PrintAssembly","-XX:+PrintCodeCache","-XX:+PrintNMethods","-XX:+PrintNativeNMethods","-XX:+PrintSignatureHandlers","-XX:+PrintSharedSpaces","-XX:+PrintCodeCacheOnCompilation","-XX:+PrintSafepointStatisticsCount","-XX:+PrintSafepointStatisticsTimeout","-XX:+PrintSafepointStatisticsCount","-XX:+PrintSafepointStatisticsTimeout","-XX:+PrintSafepointStatisticsCount","-XX:+PrintSafepointStatisticsTimeout","-XX:+PrintSafepointStatisticsCount","-XX:+PrintSafepointStatisticsTimeout","-XX:+PrintSafepointStatisticsCount","-XX:+PrintSafepointStatisticsTimeout","-XX:+PrintSafepointStatisticsCount","-XX:+PrintSafepointStatisticsTimeout"})
@Fork(value=1,jvmArgs = {"-Xmx1024m","-Xms1024m","-XX:+UseG1GC"})
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class SecurityRandom {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(SecurityRandom.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
    
    @Benchmark
    @Fork(value = 1, warmups = 1,jvmArgsAppend = {"-Djava.security.egd=file:/dev/./urandom"})
    public static String securityRandomWithEgd(Blackhole blackhole) {
        SecureRandom ng = new SecureRandom();
        byte[] randomBytes = new byte[16];
        ng.nextBytes(randomBytes);
        randomBytes[6] &= 0x0f;  /* clear version       */
        randomBytes[6]  |=0x40;  /* set to version 4     */
        randomBytes[8] &= 0x3f;  /* clear variant       */
        randomBytes[8]  |=0x80;  /* set to IETF variant  */
        return UUID.nameUUIDFromBytes(randomBytes).toString();
    }
    @Benchmark
    @Fork(value = 1, warmups = 1)
    public static String securityRandom(Blackhole blackhole) {
        SecureRandom ng = new SecureRandom();
        byte[] randomBytes = new byte[16];
        ng.nextBytes(randomBytes);
        randomBytes[6] &= 0x0f;  /* clear version       */
        randomBytes[6]  |=0x40;  /* set to version 4     */
        randomBytes[8] &= 0x3f;  /* clear variant       */
        randomBytes[8]  |=0x80;  /* set to IETF variant  */
        return UUID.nameUUIDFromBytes(randomBytes).toString();
    }
    
    
    
}
