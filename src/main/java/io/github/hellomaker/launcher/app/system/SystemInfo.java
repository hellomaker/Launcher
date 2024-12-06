package io.github.hellomaker.launcher.app.system;

import io.github.hellomaker.launcher.app.system.info.CPUInfo;
import io.github.hellomaker.launcher.app.system.info.DiskInfo;
import io.github.hellomaker.launcher.app.system.info.MemoryInfo;
import io.github.hellomaker.launcher.app.system.info.MotherboardInfo;

import java.util.ArrayList;
import java.util.List;

public class SystemInfo {
    private String caption;
    private String version;
    private String buildNumber;
    
    private List<CPUInfo> cpuInfos = new ArrayList<>();
    private List<MemoryInfo> memoryInfos = new ArrayList<>();
    private List<DiskInfo> diskInfos = new ArrayList<>();
    private MotherboardInfo motherboardInfo; // 添加主板信息

    // Getters and Setters for the fields

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public List<CPUInfo> getCpuInfos() {
        return cpuInfos;
    }

    public void setCpuInfos(List<CPUInfo> cpuInfos) {
        this.cpuInfos = cpuInfos;
    }

    public List<MemoryInfo> getMemoryInfos() {
        return memoryInfos;
    }

    public void setMemoryInfos(List<MemoryInfo> memoryInfos) {
        this.memoryInfos = memoryInfos;
    }

    public List<DiskInfo> getDiskInfos() {
        return diskInfos;
    }

    public void setDiskInfos(List<DiskInfo> diskInfos) {
        this.diskInfos = diskInfos;
    }

    public MotherboardInfo getMotherboardInfo() {
        return motherboardInfo;
    }

    public void setMotherboardInfo(MotherboardInfo motherboardInfo) {
        this.motherboardInfo = motherboardInfo;
    }

    @Override
    public String toString() {
        return "SystemInfo{" +
                "caption='" + caption + '\'' +
                ", version='" + version + '\'' +
                ", buildNumber='" + buildNumber + '\'' +
                ", cpuInfos=" + cpuInfos +
                ", memoryInfos=" + memoryInfos +
                ", diskInfos=" + diskInfos +
                ", motherboardInfo=" + motherboardInfo +
                '}';
    }
}





