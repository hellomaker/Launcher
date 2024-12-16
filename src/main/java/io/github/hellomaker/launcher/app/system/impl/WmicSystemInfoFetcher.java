package io.github.hellomaker.launcher.app.system.impl;

import io.github.hellomaker.launcher.app.system.SystemInfo;
import io.github.hellomaker.launcher.app.system.SystemInfoFetcher;
import io.github.hellomaker.launcher.app.system.info.CPUInfo;
import io.github.hellomaker.launcher.app.system.info.DiskInfo;
import io.github.hellomaker.launcher.app.system.info.MemoryInfo;
import io.github.hellomaker.launcher.app.system.info.MotherboardInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WmicSystemInfoFetcher implements SystemInfoFetcher {

    @Override
    public SystemInfo fetchSystemInfo() {
        SystemInfo systemInfo = new SystemInfo();
        MotherboardInfo motherboardInfo = new MotherboardInfo(); // 初始化主板信息对象

        try {
            // 将所有WMIC命令组合为一个命令字符串，包括主板信息
            String command = "wmic os get Caption,Version,BuildNumber /format:list & echo. & " +
                    "wmic cpu get MaxClockSpeed, Name,NumberOfCores,NumberOfLogicalProcessors /format:list & echo. & " +
                    "wmic memorychip get Capacity,Speed /format:list & echo. & " +
                    "wmic diskdrive get InterfaceType, Model,Size /format:list & echo. & " +
                    "wmic baseboard get Manufacturer,Model,SerialNumber /format:list & echo. & " +
                    "wmic bios get SMBIOSBIOSVersion /format:list";

//            Process process = Runtime.getRuntime().exec(command);
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "chcp 65001 && " + command);
            pb.redirectErrorStream(true); // 合并标准输出和错误输出
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            // Pattern to match key-value pairs from wmic output
            Pattern pattern = Pattern.compile("^(\\w+)=(.*)$");

            String line;
            boolean isCpuSection = false;
            boolean isMemorySection = false;
            boolean isDiskSection = false;
            boolean isMotherboardSection = false;
            boolean isBiosSection = false;

            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line.trim());
                if (matcher.matches()) {
                    String key = matcher.group(1).toLowerCase();
                    String value = matcher.group(2);

                    switch (key) {
                        case "caption":
                            systemInfo.setCaption(value);
                            break;
                        case "version":
                            systemInfo.setVersion(value);
                            break;
                        case "buildnumber":
                            systemInfo.setBuildNumber(value);
                            break;
                        case "name":
                            isCpuSection = true;
                            CPUInfo cpuInfo = new CPUInfo();
                            cpuInfo.setName(value);
                            systemInfo.getCpuInfos().add(cpuInfo);
                            break;
                        case "numberofcores":
                        case "numberoflogicalprocessors":
                        case "maxclockspeed":
                            if (isCpuSection && !systemInfo.getCpuInfos().isEmpty()) {
                                CPUInfo lastCpu = systemInfo.getCpuInfos().get(systemInfo.getCpuInfos().size() - 1);
                                if ("numberofcores".equals(key)) {
                                    lastCpu.setNumberOfCores(Integer.parseInt(value));
                                } else if ("numberoflogicalprocessors".equals(key)) {
                                    lastCpu.setNumberOfLogicalProcessors(Integer.parseInt(value));
                                } else if ("maxclockspeed".equals(key)) {
                                    lastCpu.setMaxClockSpeed(Integer.parseInt(value));
                                }
                            }
                            break;
                        case "capacity":
                            isMemorySection = true;
                            MemoryInfo memoryInfo = new MemoryInfo();
                            memoryInfo.setCapacity(Long.parseLong(value));
                            systemInfo.getMemoryInfos().add(memoryInfo);
                            break;
                        case "speed":
                            if (isMemorySection && !systemInfo.getMemoryInfos().isEmpty()) {
                                systemInfo.getMemoryInfos().get(systemInfo.getMemoryInfos().size() - 1).setSpeed(Integer.parseInt(value));
                            }
                            break;
                        case "model":
                            if (isDiskSection) {
                                if (!systemInfo.getDiskInfos().isEmpty()) {
                                    systemInfo.getDiskInfos().get(systemInfo.getDiskInfos().size() - 1).setModel(value);
                                }
                            } else if (isMotherboardSection) {
                                motherboardInfo.setModel(value);
                            }
                            break;
                        case "size":
                            if (isDiskSection && !systemInfo.getDiskInfos().isEmpty()) {
                                systemInfo.getDiskInfos().get(systemInfo.getDiskInfos().size() - 1).setSize(Long.parseLong(value));
                            }
                            break;
                        case "interfacetype":
                            isDiskSection = true;
                            DiskInfo diskInfo = new DiskInfo();
                            diskInfo.setInterfaceType(value);
                            systemInfo.getDiskInfos().add(diskInfo);
                            break;
                        case "manufacturer":
                            isMotherboardSection = true;
                            motherboardInfo.setManufacturer(value);
                            break;
                        case "serialnumber":
                            if (isMotherboardSection) {
                                motherboardInfo.setSerialNumber(value);
                            }
                            break;
//                        case "smbiosbiosversion":
//                            isBiosSection = true;
//                            motherboardInfo.setBiosVersion(value);
//                            break;
                        default:
                            // Ignore other keys or reset section flags
                            isCpuSection = false;
                            isMemorySection = false;
                            isDiskSection = false;
                            isMotherboardSection = false;
                            isBiosSection = false;
                            break;
                    }
                }
            }

            // 设置主板信息到系统信息对象中
            systemInfo.setMotherboardInfo(motherboardInfo);

            // Read error stream to prevent blocking
            errorReader.lines().forEach(System.err::println);

            process.waitFor(); // Wait for the command to complete
        } catch (Exception e) {
            e.printStackTrace();
        }

        return systemInfo;
    }
}
