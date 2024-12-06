package io.github.hellomaker.launcher.app.service;

import io.github.hellomaker.launcher.app.StatusEnum;

// 定义 ServiceStatus 类来封装服务状态信息
public class ServiceStatus {
    private String serviceName;
    private String status;
    private String startType;
    private StatusEnum statusEnum;


    // Getters and Setters
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartType() {
        return startType;
    }

    public void setStartType(String startType) {
        this.startType = startType;
    }

    public StatusEnum getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(StatusEnum statusEnum) {
        this.statusEnum = statusEnum;
    }

    @Override
    public String toString() {
        return "ServiceStatus{" +
                "serviceName='" + serviceName + '\'' +
                ", status='" + status + '\'' +
                ", startType='" + startType + '\'' +
                ", statusEnum=" + statusEnum +
                '}';
    }
}
