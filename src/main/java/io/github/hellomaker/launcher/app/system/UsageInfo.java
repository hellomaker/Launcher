package io.github.hellomaker.launcher.app.system;

public class UsageInfo {
    private float cpuUsage;
    private long availableMemory; // 内存剩余量（字节）
    private float memoryUsagePercentage; // 内存占用率（百分比）

    public UsageInfo(float cpuUsage, long availableMemory, float memoryUsagePercentage) {
        this.cpuUsage = cpuUsage;
        this.availableMemory = availableMemory;
        this.memoryUsagePercentage = memoryUsagePercentage;
    }

    // Getters and Setters for the fields
    public float getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(float cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public long getAvailableMemory() {
        return availableMemory;
    }

    public void setAvailableMemory(long availableMemory) {
        this.availableMemory = availableMemory;
    }

    public float getMemoryUsagePercentage() {
        return memoryUsagePercentage;
    }

    public void setMemoryUsagePercentage(float memoryUsagePercentage) {
        this.memoryUsagePercentage = memoryUsagePercentage;
    }

    @Override
    public String toString() {
        return "UsageInfo{" +
                "cpuUsage=" + cpuUsage + "%" +
                ", availableMemory=" + availableMemory / (1024 * 1024) + " MB" +
                ", memoryUsagePercentage=" + memoryUsagePercentage + "%" +
                '}';
    }
}