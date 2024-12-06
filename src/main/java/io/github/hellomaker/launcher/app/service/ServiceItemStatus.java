package io.github.hellomaker.launcher.app.service;

import io.github.hellomaker.launcher.app.StatusEnum;
import io.github.hellomaker.launcher.app.StatusListen;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.function.Consumer;

public class ServiceItemStatus implements StatusListen {

    ServiceItem serviceItem;

    public ServiceItem getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(ServiceItem serviceItem) {
        this.serviceItem = serviceItem;
    }

    ObjectProperty<StatusEnum> status = new SimpleObjectProperty<>(StatusEnum.NOT_RUNNING);


    @Override
    public void addStatusListener(Consumer<StatusEnum> changeListener) {
        status.addListener((observableValue, statusEnum, t1) -> changeListener.accept(t1));
    }

    @Override
    public void setStatus(StatusEnum status) {
        this.status.set(status);
    }


}
