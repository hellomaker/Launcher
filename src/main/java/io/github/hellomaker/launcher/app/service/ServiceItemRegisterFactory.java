package io.github.hellomaker.launcher.app.service;

import io.github.hellomaker.launcher.app.service.impl.AutoListenStrategy;
import io.github.hellomaker.launcher.app.service.impl.ServiceItemRegisterImpl;

import java.util.HashMap;
import java.util.Map;

public abstract class ServiceItemRegisterFactory {

    static Map<String, ServiceItemRegister> registers = new HashMap<>();
    static {
        registers.put("auto", new ServiceItemRegisterImpl(new AutoListenStrategy()));
    }

    public static ServiceItemRegister getRegister(String registerName) {
        return registers.getOrDefault(registerName, null);
    }

}
