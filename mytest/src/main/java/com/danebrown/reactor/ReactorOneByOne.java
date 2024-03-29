package com.danebrown.reactor;

import com.google.common.base.Strings;
import io.netty.util.concurrent.FastThreadLocalThread;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import reactor.core.Disposable;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.*;

/**
 * Created by danebrown on 2021/4/1
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Log4j2
public class ReactorOneByOne implements ApplicationListener<ApplicationReadyEvent> {

    private static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.warn("ApplicationReadyEvent");
        this.starting();
    }

    @PostConstruct
    public void postConstruct() {
        log.warn("postConstruct");
    }

    public void initMethod() {
        log.warn("ReactorOneByOne-->initMethod");

    }

    public void destroyMethod() {
        log.warn("ReactorOneByOne-->destroyMethod");

    }

    @PreDestroy
    public void preDestroy() {
        log.warn("preDestroy");

    }

    public void starting() {
        taskExecutor.execute(() -> {
            try {
                FastThreadLocalThread.sleep(3000);
                init();
            } catch (InvocationTargetException | IllegalAccessException | InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * Backpressure 是在数据流从上游生产者向下游消费者传输的过程中，
     * 上游生产速度大于下游消费速度，导致下游的 Buffer 溢出，
     * 这种现象就叫做 Backpressure 。
     * Backpressure 这个概念源自工程概念中的
     * Backpressure：在管道运输中，气流或液流由于管道突然变细、
     * 急弯等原因导致由某处出现了下游向上游的逆向压力，
     * 这种情况称作「back pressure」。
     * 这是一个很直观的词：向后的、往回的压力——back pressure。
     * 重点在【BUFFER溢出】
     * <p>
     * 在spring的reactor中， backpressure表示消费者能够反向告知生产者生产内容的速度的能力
     */


    /**
     * 为什么要用reactor
     * reactor是以事件的方式处理callback/future，看起来更简洁
     * 因为如果使用callback，那么如果出现在callback里面的callback里面的callback....就很容形成套娃。
     * 使用future的话，如果需要进行编排的话，依旧非常复杂
     */

    public void init() throws InvocationTargetException, IllegalAccessException {
        Scanner scan = new Scanner(System.in);
        while (true) {
            Method[] methods = this.getClass().getMethods();
            Map<ConsoleMenu, Method> consoleMenuList = new HashMap<>();
            Arrays.stream(methods).forEach(method -> {
                ConsoleMenu consoleMenu = method.getAnnotation(ConsoleMenu.class);
                if (consoleMenu != null) {
                    consoleMenuList.put(consoleMenu, method);
                }
            });
            consoleMenuList.keySet().stream().sorted(Comparator.comparingInt(ConsoleMenu::order)).forEach(consoleMenu -> {
                System.out.println(String.format("%d : %s [%s]", consoleMenu.order(), consoleMenu.name(), consoleMenu.desc()));
            });


            if (scan.hasNextLine()) {
                int i = Integer.parseInt(scan.nextLine());
                if (i == 0) {
                    System.out.println("停机退出");
                    break;
                }
                Optional<ConsoleMenu> optionalConsoleMenu = consoleMenuList.keySet().stream().filter(item -> item.order() == i).findFirst();
                if (optionalConsoleMenu.isPresent()) {
                    ConsoleMenu consoleMenu = optionalConsoleMenu.get();
                    Method method = consoleMenuList.get(consoleMenu);
                    method.invoke(this);
                }

            }
        }
        scan.close();
        System.exit(0);


    }

    @ConsoleMenu(order = 1, name = "range", desc = "测试range方法")
    public void range() {
        Flux<Integer> range = Flux.range(1, 3).log("range").checkpoint("range");
        log.warn("上一步其实已经调用Flux.range创建了一个序列");
        range.subscribe(i -> log.info("调用lesubscribe:{}", i));
    }

    @ConsoleMenu(order = 2, name = "compluxSubscribe", desc = "测试subscribe")
    public void compluxSubscribe() {
        //生成一个序列
        Flux<Integer> range = Flux.range(1, 3);
        //转换
        range = range.map(integer -> {
            if (integer <= 3)
                return integer;
            else
                throw new RuntimeException("数组错误");
        }).checkpoint();
        log.warn("在做了range以后，马上调用了一次Map");
        range.subscribe(new Consumer<Integer>() {
            //正常consumer
            @Override
            public void accept(Integer integer) {
                log.info("{}", integer);
            }
        }, new Consumer<Throwable>() {
            //处理异常数据consumer
            @Override
            public void accept(Throwable throwable) {
                log.error(throwable.getMessage());
            }
        }, new Runnable() {
            //处理完成的consumer
            @Override
            public void run() {
                log.trace("处理完毕");
            }
        });
    }

    //generate 测试
    @ConsoleMenu(order = 3, name = "generateTest", desc = "测试flux的generate")
    public void generateTest() {
        //生成
        Flux<String> flux = Flux.generate(new Callable<AtomicInteger>() {
            //初始化状态
            @Override
            public AtomicInteger call() throws Exception {
                //初始状态
                log.info("这里做了初始化");
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
                log.info("complete state is :" + integer.get());
            }
        });
        log.info("上面做了generate的初始化，实际generate和结束状态的consumer");
        //消费
        flux.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println("generate:-->" + s);
            }
        });
    }

    @ConsoleMenu(order = 4, name = "createTest", desc = "测试flux的create")
    public void createTest() {
        //创建
        Flux<String> flux = Flux.create(new Consumer<FluxSink<String>>() {
            @Override
            public void accept(FluxSink<String> sink) {
                sink.onRequest(new LongConsumer() {
                    //请求来的时候
                    @Override
                    public void accept(long value) {
                        sink.next("createAccept:message ID is:" + String.valueOf(value));
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
                for (int i = 0; i < 10; i++) {

                    sink.next(String.valueOf(i));
                }
            }
        }, FluxSink.OverflowStrategy.ERROR).doOnNext(s -> {
            System.err.println("createTest:doNext-->" + s);
        });

        flux.subscribe(s -> System.out.println(s));
    }

    @ConsoleMenu(order = 5, name = "handleTest", desc = "测试flux的handle方法")
    public void handleTest() {
        Flux<String> flux = Flux.create(new Consumer<FluxSink<String>>() {
            @Override
            public void accept(FluxSink<String> stringFluxSink) {
                for (int i = 0; i < 10; i++) {
                    stringFluxSink.next(String.valueOf(i));
                }
                stringFluxSink.next("");
            }
        }).handle(new BiConsumer<String, SynchronousSink<String>>() {
            @Override
            public void accept(String s, SynchronousSink<String> stringSynchronousSink) {
                System.out.println("BiConsumer-->accept:" + s);
                if (!Strings.isNullOrEmpty(s)) {
                    stringSynchronousSink.next(s);
                } else {
                    System.err.println("handle:空字符串");
                }
            }
        });
        flux.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println("handleTest-->subscribe:" + s);
            }
        });
    }

    /**
     * 问题，generate,create,push有什么不同，handle跟他们的不同
     * handle更加灵活，可以基于现有的数据做处理。
     */
    @ConsoleMenu(order = 6, name = "handleTest", desc = "测试flux的publishOn方法")
    public void publicOnTest() {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        Consumer<Integer> consumer = (s -> {
            log.info("consumer-->{}:{}", s, Thread.currentThread().getName());
        });

        Flux.range(1, 5).doOnNext(consumer).map(i -> {
            log.info("Inside map the thread is {}", Thread.currentThread().getName());
            return i * 10;
        }).publishOn(Schedulers.newBoundedElastic(10, 10, "First_PublishOn()_thread")).doOnNext(consumer).publishOn(Schedulers.newBoundedElastic(10, 10, "Second_PublishOn()_thread")).doOnNext(consumer).subscribeOn(Schedulers.newBoundedElastic(10, 10, "subscribeOn_thread")).subscribe(integer -> countDownLatch.countDown())

        ;

        try {
            countDownLatch.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @ConsoleMenu(order = 7, name = "monoBackPressure", desc = "测试mono的背压")
    public void monoBackPressure() {
        Lock lock = new ReentrantLock();
        Condition timeout = lock.newCondition();
        ExecutorService executorService = Executors.newFixedThreadPool(16);
        int count = 1;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            int finalI = i;
            executorService.submit(() -> {

                Mono

                        .defer((Supplier<Mono<?>>) () -> {
                            log.info("fromCallable:{}", finalI);
                            return Mono.just(finalI);
                        }).checkpoint("defer" + "-checkpoint").publishOn(Schedulers.newParallel("测试")).checkpoint("publishOn-checkpoint").log().checkpoint("doOnNext-checkpoint").doOnError(throwable -> log.error("命中错误:{}", throwable)).checkpoint("doOnError-checkpoint").doOnSuccess(integer -> {
                    log.info("success:{}", integer);
                    countDownLatch.countDown();
                }).checkpoint("doOnSuccess-checkpoint").doOnSubscribe(subscription -> {
                    log.info("doOnSubscribe:{}", subscription);
                }).doOnNext(integer -> {
                    lock.lock();

                    try {
                        timeout.await(200, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {

                        log.error("被人家弄得中断了呢", new RuntimeException(e));
                        //                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                    log.info("next:{}", integer);
                }).timeout(Duration.ofMillis(10)).doFinally(signalType -> log.info("结束:{}", signalType)).subscribe(integer -> {
                    try {
                        log.info("打印结果" + integer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        log.info("subscribe 结束");
                    }
                });
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @SneakyThrows
    @ConsoleMenu(order = 8, name = "deferTest", desc = "defer的测试")
    public void deferTest() {
        System.out.println("开始" + DateFormatUtils.format(new Date(), DATE_FORMAT));
        Flux<String> flux = Flux.defer(() -> s -> {
            System.out.println("defer-begin");

            s.onNext("defer" + DateFormatUtils.format(new Date(), DATE_FORMAT));
            s.onNext("defer" + DateFormatUtils.format(new Date(), DATE_FORMAT));
            System.out.println("defer-end");
        }).map(s->{
            return s.toString()+"map";
        });

        Flux<Integer> maped = flux.doOnNext(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println("flux的doOnNext|" + s);
            }
        })

                .flatMap(s -> {
            return Flux.just(s.length());
        }).doOnNext(item -> {
            System.out.println("flatMap之后的" + item);
        });

        System.out.println("准备subscribe" + DateFormatUtils.format(new Date(), DATE_FORMAT));
        Thread.sleep(1000);

        maped.subscribe();
        //        maped.publish();
        //        maped.blockFirst();
        //        maped.publish().blockFirst();
        //        FastThreadLocalThread.sleep(500);


    }

    @ConsoleMenu(order = 9, name = "backPressureTest", desc = "背压测试")
    public void backPressureTest() {
        Flux<String> source = Flux.push(stringFluxSink -> {
            stringFluxSink.next(String.valueOf(System.currentTimeMillis()));

            stringFluxSink.next(String.valueOf(System.currentTimeMillis()));
            stringFluxSink.next(String.valueOf(System.currentTimeMillis()));
        }, FluxSink.OverflowStrategy.ERROR);
        source.map(String::toUpperCase).doOnNext(s -> {
            System.out.println(s);
        })

                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {

                        request(1);
                    }

                    @Override
                    protected void hookOnNext(String value) {
                        request(1);
                    }
                })

        ;
    }

    @ConsoleMenu(order = 10, name = "mapTest", desc = "map测试")
    public void mapTest() {
        Flux<Integer> flux = Flux.just("A", "B", "C", "D")
                //                .flatMap(id->{
                //
                //                    int ascii = (int)id.toCharArray()[0];
                //                    System.out.println("flatMap ascii-->"+id+"-->to");
                //                    return Flux.just(ascii);
                //                                    })
                .map(id -> {
                    int ascii = (int) id.toCharArray()[0];
                    System.out.println("map ascii-->" + id + "-->to");
                    return ascii;
                }).doOnNext(i -> {
                    System.out.println("doOnNext-->" + i);
                });
        flux.subscribe();
    }
    @ConsoleMenu(order = 11, name = "Multiplexing", desc = "多路复用")
    public void Multiplexing() throws ExecutionException, InterruptedException {
        CompletableFuture<Flux<String>> fluxFuture = CompletableFuture.supplyAsync(() -> {
            Flux<String> flux = Flux.push(
                    stringFlx->{
                        stringFlx.next("123"+"["+Thread.currentThread().getId()+"]");
                        stringFlx.next("456"+"["+Thread.currentThread().getId()+"]");
                        stringFlx.next("789"+"["+Thread.currentThread().getId()+"]");
                    }, FluxSink.OverflowStrategy.ERROR
            );
            return flux;
        });
        fluxFuture.whenCompleteAsync((v,e)->{
            System.out.println(Thread.currentThread().getId());
            v.map(s -> s+"apply:"+Thread.currentThread().getId())
                    .doOnNext(c->{
                        System.out.println(c+"T:"+Thread.currentThread().getId());
                    }).subscribe();
        });
//       fluxFuture.thenApply((t)->
//               
//           t.map(s -> s+"apply:"+Thread.currentThread().getId())
//                   .doOnNext(c->{
//                       System.out.println(c+"T:"+Thread.currentThread().getId());
//                   })
//       );
       fluxFuture.join().subscribe();
    }


}
