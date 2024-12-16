package io.github.hellomaker.launcher.app.system;

public interface UsageInfoListenerRegistry {

    void addUsageInfoListener(UsageInfoListener listener);

    void start();

    void stop();

}
