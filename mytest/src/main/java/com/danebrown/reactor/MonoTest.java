package com.danebrown.reactor;

import com.danebrown.reactor.dto.Employer;
import com.danebrown.reactor.dto.User;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.CharacterCodingException;
import java.sql.BatchUpdateException;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by danebrown on 2020/8/14
 * mail: tain198127@163.com
 */
@Log4j2
@Data
@Component
public class MonoTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        MonoTest monoTest = new MonoTest();
        monoTest.monoInvoke();
        monoTest.monoSubscribe();
    }

    private User generateUser() {
        User firstUser = new User();
        firstUser.setName("test");
        firstUser.setAddress("beijing");
        firstUser.setAge(30);
        return firstUser;
    }

    public void monoInvoke() throws InterruptedException, ExecutionException, TimeoutException {
        User firstUser = generateUser();
        User secondUser = generateUser();
        Mono<Employer> mono = Mono.just(firstUser)
                .map(user -> {
                    Employer employer = Employer.builder().build();
                    employer.setId(UUID.randomUUID().toString());
                    return employer;
                })
                .doOnNext(employer -> {
                    log.info("next1:{}", employer);
                    employer.setBirthday(new Date());
                })
                .doOnNext(employer -> {
                    log.info("next2:{}", employer);
                })
                .checkpoint("checkpoint")
                .log()
                .doFinally(signalType -> {
                    log.info("signalType:{}", signalType);
                })
                .name("test")
                .doOnNext(employer -> {
                    int stat = Math.abs(employer.getId().hashCode()) % 4;
                    log.info("hash code:{}",stat);
                    switch (stat) {
                        case 0: {
                            try {
                                Thread.sleep(3000L);
                            } catch (InterruptedException e) {
                                log.error("{}", e.getMessage());
                            }
                        }
                        break;
                        case 1: {
                            int a = 1 / 0;
                        }
                        break;
                        case 2: {
                            throw new SecurityException("bat action");
                        }

                        default: {
                            log.info("peaceful night");
                        }
                        break;
                    }

                })
                .timeout(Duration.ofMillis(1000L), Mono.just(Employer.builder().id("timeout").build()))
                .onErrorReturn(SecurityException.class, Employer.builder().id("SecurityException").build())
                .onErrorReturn(RuntimeException.class, Employer.builder().id("any runtime exp").build())
                .onErrorStop()
                .doOnNext(employer -> {
                    log.info("after chaos test:{}", employer);
                });
        log.info("final {}", mono.block());
    }

    public void monoSubscribe() {
        User firstUser = generateUser();
        Mono.just(firstUser)

                .subscribe(
                        user -> {
                            System.out.println(user);
                        },
                        user -> {
                            System.err.println(user);
                        },
                        () -> {
                            System.out.println("Complete");
                        }
                );
    }
}
