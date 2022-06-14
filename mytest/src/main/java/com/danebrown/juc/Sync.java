package com.danebrown.juc;

import io.netty.util.concurrent.FastThreadLocalThread;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by danebrown on 2021/8/9
 * mail: tain198127@163.com
 *
 * @author danebrown
 */
@Log4j2
public class Sync {
    public static volatile AtomicLong indexer = new AtomicLong(0);
    public static void main(String[] args) throws Exception {

        PriorityBlockingQueue<Message> tstqueue = new PriorityBlockingQueue(20,
                new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return o2.compareTo(o1);
            }
        });

        LinkedBlockingQueue blockingQueue = new LinkedBlockingQueue();
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(1000);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(16,
                16, 1000, TimeUnit.MILLISECONDS,
                arrayBlockingQueue,
                new ThreadPoolExecutor.AbortPolicy());


        Callable fn = () -> {
            Message msg = new Message();

            FastThreadLocalThread.sleep(ThreadLocalRandom.current().nextInt(10, 1000));
            return msg;
        };
        List<Future<Message>> list = new ArrayList<>();
        for (int i = 0; i < 900; i++) {
//            Future<Message> msg = executor.submit(fn);
            Future<Message> msg = executor.submit(new MessageCaller());
            list.add(i,msg);

        }
        for(int i = 0; i < list.size();i++){
            Message rst = list.get(i).get();
//            tstqueue.put(rst);
//            if(rst.idx != tstqueue.peek().idx){
//                throw new Exception("顺序出错");
//            }
            log.info("{}", rst);
        }

        executor.shutdown();

    }
    public static class MessageCaller implements Callable<Message>{

        @Override
        public Message call() throws Exception {
            Message msg = new Message();

            FastThreadLocalThread.sleep(ThreadLocalRandom.current().nextInt(10, 1000));
            return msg;        }
    }

    public static class Message implements Comparable {

        private String message;
        private Date createDate;
        private long idx;
        public Message() {
            this.createDate = new Date();
            idx = indexer.incrementAndGet();
            this.message = DateFormatUtils.format(createDate, "YYYY-MM-dd " +
                    "HH:mm:" + "ss:" + "SSS") + "线程ID:" + Thread.currentThread().getId()+"; 序号ID:"+idx;
        }

        @Override
        public String toString() {
            return "Message{" + "message='" + message + '\'' + ", createDate=" + createDate + ", idx=" + idx + '}';
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
