package io.github.hellomaker.launcher.app.service;

import io.github.hellomaker.launcher.app.StatusEnum;

import java.util.function.Consumer;

public interface ServiceItemRegister {

    void register(ServiceItem item, Consumer<StatusEnum> changeListener);

    void start();

    void stop();

}
