package utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadPoolExecutor extends ThreadPoolExecutor {

    public static MyThreadPoolExecutor getPool (String name) {
        return new MyThreadPoolExecutor(2,
                5,
                0L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(3),
                new MyThreadFactory(name),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
    }

    public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public static class MyThreadFactory implements ThreadFactory {

        private String mPrefix;
        private AtomicInteger mThreadNum;

        private static final AtomicInteger mPoolNum = new AtomicInteger(1);

        public MyThreadFactory() {
            this("default");
        }

        public MyThreadFactory(String prefix) {
            mPrefix = "pool-" + prefix + "-thread-";
            mThreadNum = new AtomicInteger(1);
        }

        @Override
        public Thread newThread(Runnable r) {
            String name = mPrefix + mThreadNum.getAndIncrement();
            return new Thread(r, name);
        }
    }
}
