package io.github.hellomaker.launcher.app.service.impl;

import io.github.hellomaker.launcher.app.ProcessUtil;
import io.github.hellomaker.launcher.app.service.ServiceItemStatus;
import io.github.hellomaker.launcher.app.service.ServiceItemStatusStrategy;
import io.github.hellomaker.launcher.app.service.ServiceStatus;
import io.github.hellomaker.launcher.pool.MyThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AutoListenStrategy implements ServiceItemStatusStrategy {

    private static final Logger log = LoggerFactory.getLogger(AutoListenStrategy.class);
    Map<String, ServiceItemStatus> serviceListenMap = new HashMap<>();
    volatile boolean start = false;

    @Override
    public void addListen(ServiceItemStatus serviceItemStatus) {
        serviceListenMap.put(serviceItemStatus.getServiceItem().getItemName()
                + serviceItemStatus.getServiceItem().getServiceName(), serviceItemStatus);
    }

    @Override
    public void start() {
        if (start) {
            return;
        } else {
            start = true;
        }
        MyThreadPool.getInstance().submit(() -> {
            while (start) {
                for (Map.Entry<String, ServiceItemStatus> stringServiceItemStatusEntry : serviceListenMap.entrySet()) {
                    ServiceItemStatus serviceItemStatus = stringServiceItemStatusEntry.getValue();
                    String serviceName = serviceItemStatus.getServiceItem().getServiceName();
                    try {
                        ServiceStatus serviceStatus = ProcessUtil.checkServiceStatus(serviceName);
                        serviceItemStatus.setStatus(serviceStatus.getStatusEnum());
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                        log.error(e.getMessage());
                    }
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void stop() {
        start = false;
    }


}
