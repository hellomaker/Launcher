package io.github.hellomaker.launcher.app.system.impl;

import io.github.hellomaker.launcher.app.system.UsageInfo;
import io.github.hellomaker.launcher.app.system.UsageInfoFetcher;
import io.github.hellomaker.launcher.app.system.UsageInfoListener;
import io.github.hellomaker.launcher.app.system.UsageInfoListenerRegistry;
import io.github.hellomaker.launcher.pool.MyThreadPool;

import java.util.ArrayList;
import java.util.List;

public class AutoSingleRegistry implements UsageInfoListenerRegistry {

    private AutoSingleRegistry(){};

    static AutoSingleRegistry instance;
    public static AutoSingleRegistry getInstance(){
        if(instance == null){
            synchronized(AutoSingleRegistry.class){
                if(instance == null){
                    instance = new AutoSingleRegistry();
                    instance.listeners = new ArrayList<>();
                    instance.fetcher = new DelegateUsageInfoFetcher();
                    instance.start();
                }
            }
        }
        return instance;
    }

    List<UsageInfoListener> listeners;
    volatile boolean running = false;


    @Override
    public void addUsageInfoListener(UsageInfoListener listener) {
        listeners.add(listener);
    }

    UsageInfoFetcher fetcher;

    @Override
    public void start() {
        if (running) {
            return;
        }
        instance.running = Boolean.TRUE;
        MyThreadPool.getInstance().submit(() -> {
            while (running) {
                UsageInfo usageInfo = fetcher.fetchUsageInfo();
                listeners.forEach((e) -> {
                    e.usageInfoChange(usageInfo);
                });
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void stop() {
        running = Boolean.FALSE;
    }
}
