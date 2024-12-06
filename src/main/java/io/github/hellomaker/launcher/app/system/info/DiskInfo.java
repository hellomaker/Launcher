package io.github.hellomaker.launcher.app.system.info;

public class DiskInfo {
    private String model;
    private long size;
    private String interfaceType;

    // Getters and Setters for the fields

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    @Override
    public String toString() {
        return "DiskInfo{" +
                "model='" + model + '\'' +
                ", size=" + size +
                ", interfaceType='" + interfaceType + '\'' +
                '}';
    }
// Override toString for better output representation
}