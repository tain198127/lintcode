package com.danebrown.juc;

import io.netty.util.concurrent.FastThreadLocalThread;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by danebrown on 2021/8/9
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Log4j2
public class Sync {

    @SneakyThrows
    public static void main(String[] args) {

        PriorityBlockingQueue tstqueue = new PriorityBlockingQueue(20, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return o1.compareTo(o2);
            }
        });

        LinkedBlockingQueue blockingQueue = new LinkedBlockingQueue();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(2,
                2, 1000, TimeUnit.MILLISECONDS,
                tstqueue,
                new ThreadPoolExecutor.AbortPolicy());


        Callable fn = () -> {
            Message msg = new Message();

            FastThreadLocalThread.sleep(ThreadLocalRandom.current().nextInt(10, 1000));
            return msg;
        };
        for (int i = 0; i < 100; i++) {
            Future<Message> msg = executor.submit(fn);
            Message rst = msg.get();
            log.info("{}", rst);
        }


        executor.shutdown();

    }
    //    public static class CallAbleTast implements Callable{
    //        public Message holder;
    //        @Override
    //        public Message call() throws Exception {
    //            Message msg = new Message();
    //            holder = msg;
    //            FastThreadLocalThread.sleep(ThreadLocalRandom.current().nextInt(10
    //                    ,1000));
    //            return msg;
    //        }
    //
    //    }

    public static class Message implements Comparable {

        private String message;
        private Date createDate;

        public Message() {
            this.createDate = new Date();
            this.message = DateFormatUtils.format(new Date(), "YYYY-MM-dd HH:mm:" + "ss:" + "SSS") + "线程ID:" + Thread.currentThread().getId();
        }

        @Override
        public String toString() {
            return "Message{" + "message='" + message + '\'' + '}';
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Date getCreateDate() {
            return createDate;
        }

        public void setCreateDate(Date createDate) {
            this.createDate = createDate;
        }

        @Override
        public int compareTo(Object o) {
            return this.createDate.compareTo(((Message) o).createDate);
        }
    }
}
