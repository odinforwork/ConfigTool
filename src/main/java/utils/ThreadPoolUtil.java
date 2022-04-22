package utils;

import lombok.Getter;

import java.util.concurrent.*;

public class ThreadPoolUtil {

    private static volatile ThreadPoolUtil sInstance;
    @Getter
    private final ExecutorService mPool;

    public static ThreadPoolUtil getInstance() {
        if(sInstance == null) {
            synchronized (ThreadPoolUtil.class) {
                if(sInstance == null) {
                    sInstance = new ThreadPoolUtil();
                }
            }
        }

        return sInstance;
    }

    private ThreadPoolUtil() {
        mPool = new ThreadPoolExecutor(
                2,
                3,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }
}
