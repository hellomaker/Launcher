package io.github.hellomaker.launcher.app.system.info;

public class CPUInfo {
    private String name;
    private int numberOfCores;
    private int numberOfLogicalProcessors;
    private int maxClockSpeed;

    // Getters and Setters for the fields

    // Override toString for better output representation


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfCores() {
        return numberOfCores;
    }

    public void setNumberOfCores(int numberOfCores) {
        this.numberOfCores = numberOfCores;
    }

    public int getNumberOfLogicalProcessors() {
        return numberOfLogicalProcessors;
    }

    public void setNumberOfLogicalProcessors(int numberOfLogicalProcessors) {
        this.numberOfLogicalProcessors = numberOfLogicalProcessors;
    }

    public int getMaxClockSpeed() {
        return maxClockSpeed;
    }

    public void setMaxClockSpeed(int maxClockSpeed) {
        this.maxClockSpeed = maxClockSpeed;
    }

    @Override
    public String toString() {
        return "CPUInfo{" +
            "name='" + name + '\'' +
            ", numberOfCores=" + numberOfCores +
            ", numberOfLogicalProcessors=" + numberOfLogicalProcessors +
            ", maxClockSpeed=" + maxClockSpeed +
            '}';
    }
}
