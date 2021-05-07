package com.danebrown.reactor;

import com.google.common.base.Strings;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.SynchronousSink;
import reactor.util.context.Context;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.stream.Stream;

/**
 * Created by danebrown on 2021/4/1
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
public class ReactorOneByOne {
    /**
     * Backpressure 是在数据流从上游生产者向下游消费者传输的过程中，
     *         上游生产速度大于下游消费速度，导致下游的 Buffer 溢出，
     *         这种现象就叫做 Backpressure 出现。
     *         Backpressure 这个概念源自工程概念中的
     *         Backpressure：在管道运输中，气流或液流由于管道突然变细、
     *         急弯等原因导致由某处出现了下游向上游的逆向压力，
     *         这种情况称作「back pressure」。
     *         这是一个很直观的词：向后的、往回的压力——back pressure。
     *         重点在【BUFFER溢出】
     *
     * 在spring的reactor中， backpressure表示消费者能够反向告知生产者生产内容的速度的能力
     *
     */

    /**
     * 为什么要用reactor
     * reactor是以事件的方式处理callback/future，看起来更简洁
     * 因为如果使用callback，那么如果出现在callback里面的callback里面的callback....就很容形成套娃。
     * 使用future的话，如果需要进行编排的话，依旧非常复杂
     */
    public static void range(){
        Flux<Integer> range = Flux.range(1,3);
        range.subscribe(i->System.out.println(i));
    }
    public static void compluxSubscribe(){
        Flux<Integer> range = Flux.range(1,3);
        range  =range.map(integer -> {
            if(integer <=3 ) return integer;
            else throw new RuntimeException("数组错误");
        });
        range.subscribe(new Consumer<Integer>() {
            //正常consumer
            @Override
            public void accept(Integer integer) {
                System.out.println(integer);
            }
        }, new Consumer<Throwable>() {
            //处理异常数据consumer
            @Override
            public void accept(Throwable throwable) {
                System.err.println(throwable.getMessage());
            }
        }, new Runnable() {
            //处理完成的consumer
            @Override
            public void run() {
                System.err.println("处理完毕");
            }
        });
    }
    //generate 测试
    public static void generateTest(){
        Flux<String> flux = Flux.generate(new Callable<AtomicInteger>() {
            //初始化状态
            @Override
            public AtomicInteger call() throws Exception {
                //初始状态
               return new AtomicInteger();
            }
        }, new BiFunction<AtomicInteger, SynchronousSink<String>, AtomicInteger>() {
            // 实际的generate方法
            @Override
            public AtomicInteger apply(AtomicInteger state, SynchronousSink<String> sink) {
                sink.next("3 *" + state + "=" + 3 * state.get());
                if (state.get() >= 10)
                    sink.complete();
                state.incrementAndGet();
                return state;
            }
        }, new Consumer<AtomicInteger>() {
            //结束状态的consumer
            @Override
            public void accept(AtomicInteger integer) {
                System.out.println("complete state is :"+integer.get());
            }
        });
        flux.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println("generate:-->"+s);
            }
        });
    }

    public static void createTest(){
        Flux<String> flux = Flux.create(new Consumer<FluxSink<String>>() {
            @Override
            public void accept(FluxSink<String> sink) {
                sink.onRequest(new LongConsumer() {
                    //请求来的时候
                    @Override
                    public void accept(long value) {
                        sink.next("createAccept:message ID is:"+String.valueOf(value));
                    }
                }).onCancel(new Disposable() {
                    //请求cancel的时候
                    @Override
                    public void dispose() {
                        System.out.println("cancel");
                    }
                }).onDispose(new Disposable() {
                    //请求dispose的时候
                    @Override
                    public void dispose() {
                        System.out.println("dispose");
                    }
                });
                for(int i =0;i<10;i++){

                    sink.next(String.valueOf(i));
                }
            }
        }, FluxSink.OverflowStrategy.ERROR ).doOnNext(s -> {
            System.err.println("createTest:doNext-->"+s);
        });

        flux.subscribe(s-> System.out.println(s));
    }

    public static void handleTest(){
        Flux<String> flux = Flux.create(new Consumer<FluxSink<String>>() {
            @Override
            public void accept(FluxSink<String> stringFluxSink) {
                for(int i =0;i< 10;i ++){
                    stringFluxSink.next(String.valueOf(i));
                }
                stringFluxSink.next("");
            }
        }).handle(new BiConsumer<String, SynchronousSink<String>>() {
            @Override
            public void accept(String s, SynchronousSink<String> stringSynchronousSink) {
                System.out.println("BiConsumer-->accept:"+s);
                if(!Strings.isNullOrEmpty(s)){
                    stringSynchronousSink.next(s);
                }
                else{
                    System.err.println("handle:空字符串");
                }
            }
        });
        flux.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println("handleTest-->subscribe:"+s);
            }
        });
    }
    /**
     * 问题，generate,create,push有什么不同，handle跟他们的不同
     * handle更加灵活，可以基于现有的数据做处理。
     */

    /**
     * 问题
     * @param args
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {
//        ReactorOneByOne.range();
//        ReactorOneByOne.compluxSubscribe();
//        ReactorOneByOne.generateTest();
//        ReactorOneByOne.createTest();
        ReactorOneByOne.handleTest();
        System.in.read();
    }

}
