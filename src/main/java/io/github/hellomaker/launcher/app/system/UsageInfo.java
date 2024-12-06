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

    @Override
    public String toString() {
        return "UsageInfo{" +
                "cpuUsage=" + cpuUsage + "%" +
                ", availableMemory=" + availableMemory / (1024 * 1024) + " MB" +
                ", memoryUsagePercentage=" + memoryUsagePercentage + "%" +
                '}';
    }
}