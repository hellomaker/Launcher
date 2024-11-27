package io.github.hellomaker.launcher.pool;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hellomaker
 */
public class MyThreadPool {
    LinkedBlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>(30);
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5, 60 * 20, TimeUnit.SECONDS,
            taskQueue, Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

    public void submit(Runnable runnable) {
        threadPoolExecutor.submit(runnable);
    }

    public void close() {
        threadPoolExecutor.shutdown();
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
