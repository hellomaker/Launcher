package io.github.hellomaker.launcher.app;

import io.github.hellomaker.launcher.app.system.*;
import io.github.hellomaker.launcher.app.system.info.CPUInfo;
import io.github.hellomaker.launcher.app.system.info.DiskInfo;
import io.github.hellomaker.launcher.app.system.info.MemoryInfo;
import io.github.hellomaker.launcher.app.system.info.MotherboardInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hellomaker
 */
public abstract class SystemInfoFactory {

    public static String getMainBoardSerialNumber() {
        String os = System.getProperty("os.name");
        String command;
        if (os.contains("Windows")) {
            command = "wmic baseboard get SerialNumber";
        } else if (os.contains("Linux")) {
            command = "sudo dmidecode -t 2 | grep Serial";
        } else {
            // 其他操作系统类型的处理
            // ...
            return "";
        }
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            return null;
        }
        InputStream inputStream = process.getInputStream();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            String serialNumber = null;
            while ((line = reader.readLine()) != null) {
                if (os.contains("Windows")) {
//                    if (serialNumber == null) {
                    if (!line.trim().isEmpty()) {
                        serialNumber = line.trim();
                    }

//                    } else {
//                        break;
//                    }
                } else if (os.contains("Linux")) {
                    if (line.contains("Serial")) {
                        serialNumber = line.split(":")[1].trim();
                        break;
                    }
                } else {
                    // 其他操作系统类型的处理
                    // ...
                    return null;
                }
            }
            try {
                int exitCode = process.waitFor();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return serialNumber;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static SystemInfo fetchSystemInfo() {
        SystemInfo systemInfo = new SystemInfo();
        MotherboardInfo motherboardInfo = new MotherboardInfo(); // 初始化主板信息对象

        try {
            // 将所有WMIC命令组合为一个命令字符串，包括主板信息
            String command = "cmd /c \"wmic os get Caption,Version,BuildNumber /format:list & echo. & " +
                    "wmic cpu get Name,NumberOfCores,NumberOfLogicalProcessors,MaxClockSpeed /format:list & echo. & " +
                    "wmic memorychip get Capacity,Speed /format:list & echo. & " +
                    "wmic diskdrive get Model,Size,InterfaceType /format:list & echo. & " +
                    "wmic baseboard get Manufacturer,Model,SerialNumber /format:list & echo. & " +
                    "wmic bios get SMBIOSBIOSVersion /format:list\"";

            Process process = Runtime.getRuntime().exec(command);
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
                                DiskInfo diskInfo = new DiskInfo();
                                diskInfo.setModel(value);
                                systemInfo.getDiskInfos().add(diskInfo);
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
                            if (isDiskSection && !systemInfo.getDiskInfos().isEmpty()) {
                                systemInfo.getDiskInfos().get(systemInfo.getDiskInfos().size() - 1).setInterfaceType(value);
                            }
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
