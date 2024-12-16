package io.github.hellomaker.launcher.pool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hellomaker
 */
public class MyThreadPool {
    LinkedBlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>(30);

    static class MyThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        MyThreadFactory() {
            @SuppressWarnings("removal")
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "my-pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    };

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 16, 60 * 20, TimeUnit.SECONDS,
            taskQueue, new MyThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

    public void submit(Runnable runnable) {
        threadPoolExecutor.submit(runnable);
    }

    public void close() {
        threadPoolExecutor.shutdown();
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    /**
     * 功能描述: 私有构造
     * @Param: []
     * @Return:
     * @Author: xianzhikun
     * @Date: 2023/4/27 9:41
     */
    private MyThreadPool(){}

    public static MyThreadPool getInstance() {
        if (ins == null) {
            synchronized (MyThreadPool.class) {
                if (ins == null) {
                    ins = new MyThreadPool();
                }
            }
        }
        return ins;
    }

    private static MyThreadPool ins;
}
