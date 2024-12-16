package io.github.hellomaker.launcher.app.system.impl;

import io.github.hellomaker.launcher.app.system.UsageInfo;
import io.github.hellomaker.launcher.app.system.UsageInfoFetcher;

public class DelegateUsageInfoFetcher implements UsageInfoFetcher {

    UsageInfoFetcher wmic = new WmicUsageInfoFetcher();
    UsageInfoFetcher oshi = new OSHIUsageInfoFetcher();

    @Override
    public UsageInfo fetchUsageInfo() {
        UsageInfo wmicInfo = wmic.fetchUsageInfo();
        UsageInfo oshiInfo = oshi.fetchUsageInfo();

        return new UsageInfo(wmicInfo.getCpuUsage(), oshiInfo.getAvailableMemory(), oshiInfo.getMemoryUsagePercentage());
    }
}
