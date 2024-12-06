package io.github.hellomaker.launcher.app.system.info;

public class MotherboardInfo { // 新增主板信息类
    private String manufacturer;
    private String model;
    private String serialNumber;
    private String biosVersion;

    // Getters and Setters for the fields

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBiosVersion() {
        return biosVersion;
    }

    public void setBiosVersion(String biosVersion) {
        this.biosVersion = biosVersion;
    }

    // Override toString for better output representation


    @Override
    public String toString() {
        return "MotherboardInfo{" +
                "manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", biosVersion='" + biosVersion + '\'' +
                '}';
    }
}