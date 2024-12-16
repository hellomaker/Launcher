package io.github.hellomaker.launcher.app.system.impl;

import io.github.hellomaker.launcher.app.system.UsageInfo;
import io.github.hellomaker.launcher.app.system.UsageInfoFetcher;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.FormatUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author hellomaker
 */
public class OSHIUsageInfoFetcher implements UsageInfoFetcher {

    private static final SystemInfo systemInfo = new SystemInfo();
    private static final HardwareAbstractionLayer hardware = systemInfo.getHardware();
    private static final CentralProcessor processor = hardware.getProcessor();
    private static final GlobalMemory memory = hardware.getMemory();

    @Override
    public UsageInfo fetchUsageInfo() {

        // 获取内存使用情况
        long totalMemory = memory.getTotal();
        long availableMemory = memory.getAvailable();
        float memoryUsagePercentage = (float) (totalMemory - availableMemory) / totalMemory * 100;


//        System.out.println("Memory Usage: " + String.format("%.2f", memoryUsagePercentage) + "%");
//        System.out.println("Memory Usage: " + memoryUsagePercentage + "%");

        SystemInfo systemInfo = new SystemInfo();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        long[][] prevProcTicks = processor.getProcessorCpuLoadTicks();

        List<String> oshi = new ArrayList<>();
        oshi.add("CPU, IOWait, and IRQ ticks @ 0 sec:" + Arrays.toString(prevTicks));
        // Wait a second...
//        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        oshi.add("CPU, IOWait, and IRQ ticks @ 1 sec:" + Arrays.toString(ticks));
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long sys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long totalCpu = user + nice + sys + idle + iowait + irq + softirq + steal;

        oshi.add(String.format(Locale.ROOT,
                "User: %.1f%% Nice: %.1f%% System: %.1f%% Idle: %.1f%% IOwait: %.1f%% IRQ: %.1f%% SoftIRQ: %.1f%% Steal: %.1f%%",
                100d * user / totalCpu, 100d * nice / totalCpu, 100d * sys / totalCpu, 100d * idle / totalCpu,
                100d * iowait / totalCpu, 100d * irq / totalCpu, 100d * softirq / totalCpu, 100d * steal / totalCpu));
        oshi.add(String.format(Locale.ROOT, "CPU load: %.1f%%",
                processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100));
        double[] loadAverage = processor.getSystemLoadAverage(3);
        oshi.add("CPU load averages:"
                + (loadAverage[0] < 0 ? " N/A" : String.format(Locale.ROOT, " %.2f", loadAverage[0]))
                + (loadAverage[1] < 0 ? " N/A" : String.format(Locale.ROOT, " %.2f", loadAverage[1]))
                + (loadAverage[2] < 0 ? " N/A" : String.format(Locale.ROOT, " %.2f", loadAverage[2])));
        // per core CPU
        StringBuilder procCpu = new StringBuilder("CPU load per processor:");
        double[] load = processor.getProcessorCpuLoadBetweenTicks(prevProcTicks);
        for (double avg : load) {
            procCpu.append(String.format(Locale.ROOT, " %.1f%%", avg * 100));
        }
        oshi.add(procCpu.toString());
        long freq = processor.getProcessorIdentifier().getVendorFreq();
        if (freq > 0) {
            oshi.add("Vendor Frequency: " + FormatUtil.formatHertz(freq));
        }
        freq = processor.getMaxFreq();
        if (freq > 0) {
            oshi.add("Max Frequency: " + FormatUtil.formatHertz(freq));
        }
        long[] freqs = processor.getCurrentFreq();
        if (freqs[0] > 0) {
            StringBuilder sb = new StringBuilder("Current Frequencies: ");
            for (int i = 0; i < freqs.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(FormatUtil.formatHertz(freqs[i]));
            }
            oshi.add(sb.toString());
        }
//        if (!processor.getFeatureFlags().isEmpty()) {
//            oshi.add("CPU Features:");
//            for (String features : processor.getFeatureFlags()) {
//                oshi.add("  " + features);
//            }
//        }

//        System.out.println("Cpu Usage: " + cpuUsage + "%");
//        System.out.println(oshi);
        return new UsageInfo(1, availableMemory, memoryUsagePercentage);
    }

}
