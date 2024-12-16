package io.github.hellomaker.launcher.app.service;

public interface ServiceItemStatusStrategy {

    void addListen(ServiceItemStatus serviceItemStatus);

    void start();

    void stop();

}
