package io.github.hellomaker.launcher.app;

import io.github.hellomaker.launcher.app.system.UsageInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public abstract class UsageinfoFactory {

    public static UsageInfo fetchUsageInfo()  {
        try {
            // Initialize variables to store CPU and Memory info
            float cpuUsage = -1;
            long totalPhysicalMemory = -1;
            long availableMemory = -1;

            // Execute command to get CPU usage
            Process processCpu = Runtime.getRuntime().exec("wmic path Win32_PerfFormattedData_PerfOS_Processor get PercentProcessorTime /format:value");
            BufferedReader readerCpu = new BufferedReader(new InputStreamReader(processCpu.getInputStream()));
            String lineCpu;
            while ((lineCpu = readerCpu.readLine()) != null) {
                if (lineCpu.startsWith("PercentProcessorTime")) {
                    cpuUsage = Float.parseFloat(lineCpu.split("=")[1]);
                    break;
                }
            }
            readerCpu.close();

            // Execute command to get Total Physical Memory
            Process processTotalMem = Runtime.getRuntime().exec("wmic ComputerSystem get TotalPhysicalMemory /format:value");
            BufferedReader readerTotalMem = new BufferedReader(new InputStreamReader(processTotalMem.getInputStream()));
            String lineTotalMem;
            while ((lineTotalMem = readerTotalMem.readLine()) != null) {
                if (lineTotalMem.startsWith("TotalPhysicalMemory")) {
                    totalPhysicalMemory = Long.parseLong(lineTotalMem.split("=")[1]);
                    break;
                }
            }
            readerTotalMem.close();

            // Execute command to get Available Memory
            Process processAvailableMem = Runtime.getRuntime().exec("wmic OS get FreePhysicalMemory /format:value");
            BufferedReader readerAvailableMem = new BufferedReader(new InputStreamReader(processAvailableMem.getInputStream()));
            String lineAvailableMem;
            while ((lineAvailableMem = readerAvailableMem.readLine()) != null) {
                if (lineAvailableMem.startsWith("FreePhysicalMemory")) {
                    availableMemory = Long.parseLong(lineAvailableMem.split("=")[1]) * 1024; // Convert KB to B
                    break;
                }
            }
            readerAvailableMem.close();

            // Calculate used memory and memory usage percentage
            long usedMemory = totalPhysicalMemory - availableMemory;
            float memoryUsagePercentage = (usedMemory / (float)totalPhysicalMemory) * 100;



            // Create and return UsageInfo object
            return new UsageInfo(cpuUsage, availableMemory, memoryUsagePercentage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(fetchUsageInfo());
    }
}
