package io.github.hellomaker.launcher.app.service;

import io.github.hellomaker.launcher.app.AppConst;
import io.github.hellomaker.launcher.app.ProcessUtil;
import io.github.hellomaker.launcher.app.StatusEnum;
import io.github.hellomaker.launcher.app.StatusListen;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

public class ServiceItem  {

    public String getServiceName() {
        return serviceName;
    }

    public ServiceItem(String serviceName, String itemName) {
        this.serviceName = serviceName;
        this.itemName = itemName;
    }

    String serviceName;
    String itemName;

    public String getItemName() {
        return itemName;
    }

}
