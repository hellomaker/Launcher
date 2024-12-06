package io.github.hellomaker.launcher.app.service.impl;

import io.github.hellomaker.launcher.app.StatusEnum;
import io.github.hellomaker.launcher.app.service.ServiceItem;
import io.github.hellomaker.launcher.app.service.ServiceItemRegister;
import io.github.hellomaker.launcher.app.service.ServiceItemStatus;
import io.github.hellomaker.launcher.app.service.ServiceItemStatusStrategy;

import java.util.function.Consumer;

/**
 * @author hellomaker
 */
public class ServiceItemRegisterImpl implements ServiceItemRegister {

    private final ServiceItemStatusStrategy serviceItemStatusStrategy;
    public ServiceItemRegisterImpl(ServiceItemStatusStrategy serviceItemStatusStrategy) {
        this.serviceItemStatusStrategy = serviceItemStatusStrategy;
    }

    @Override
    public void register(ServiceItem item, Consumer<StatusEnum> changeListener) {
        ServiceItemStatus status = new ServiceItemStatus();
        status.setServiceItem(item);
        status.addStatusListener(changeListener);
        serviceItemStatusStrategy.addListen(status);
    }

    @Override
    public void start() {
        serviceItemStatusStrategy.start();
    }

    @Override
    public void stop() {
        serviceItemStatusStrategy.stop();
    }
}
