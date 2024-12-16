package io.github.hellomaker.launcher.controller.dashboard;

import io.github.hellomaker.launcher.app.DiskUsageUtil;
import io.github.hellomaker.launcher.app.system.SystemInfo;
import io.github.hellomaker.launcher.app.system.UsageInfo;
import io.github.hellomaker.launcher.app.system.UsageInfoListener;
import io.github.hellomaker.launcher.app.system.impl.AutoSingleRegistry;
import io.github.hellomaker.launcher.app.system.impl.WmicSystemInfoFetcher;
import io.github.hellomaker.launcher.app.system.info.CPUInfo;
import io.github.hellomaker.launcher.app.system.info.DiskInfo;
import io.github.hellomaker.launcher.app.system.info.MemoryInfo;
import io.github.hellomaker.launcher.common.MathUtil;
import io.github.hellomaker.launcher.pool.MyThreadPool;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.stream.Collectors;

public class InfoController {
    @FXML
    Label systemVersionLabel;
    @FXML
    Label cpuLabel;
    @FXML
    Label ramLabel;
    @FXML
    Label diskLabel;

    @FXML
    Label ixlabUsage;
    @FXML
    PieChart diskDUsageChart;
//    @FXML
//    PieChart ixlabUsageChart;
    @FXML
    PieChart memoryUsageChart;
    @FXML
    PieChart cpuUsageChart;

    PieChart.Data p1;

    PieChart.Data p2;

    PieChart.Data p3;

    PieChart.Data p4;

    PieChart.Data p5;

    PieChart.Data p6;


    @FXML
    public void initialize() {

//                    diskCUsageChart.getData().add(new PieChart.Data(usageInfo.getCpuUsage() + "%", usageInfo.getCpuUsage()));
//                    diskDUsageChart.getData().add(new PieChart.Data("D盘占用率", usageInfo.getCpuUsage()));

        ObservableList<PieChart.Data> memoryUsageChartData = memoryUsageChart.getData();
        float memoryUsage = 0;
        float remainMemoryUsage = 100 - memoryUsage;

        float cpuUsage = 0;
        float remainCpuUsage = 100 - cpuUsage;

        p1 = new PieChart.Data("内存占用" + memoryUsage + "%", memoryUsage);

        memoryUsageChartData.add(p1);

        p2 = new PieChart.Data("内存剩余" + remainMemoryUsage + "%", remainMemoryUsage);
        memoryUsageChartData.add(p2);


        ObservableList<PieChart.Data> cpuUsageChartData = cpuUsageChart.getData();
        p3 = new PieChart.Data("cpu占用" + cpuUsage + "%", cpuUsage);
        cpuUsageChartData.add(p3);
        p4 = new PieChart.Data("cpu剩余" + remainCpuUsage + "%", remainCpuUsage);

        cpuUsageChartData.add(p4);

        double diskDUsage = 30;
        double remainDUsage = 100 - diskDUsage;
//        try {
//            diskDUsage = DiskUsageUtil.diskUsage("D:\\");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        BigDecimal diskDUsageDec = BigDecimal.valueOf(diskDUsage).setScale(2, RoundingMode.HALF_UP);
//        BigDecimal remainDUsageDec = BigDecimal.valueOf(100).subtract(diskDUsageDec).setScale(2, RoundingMode.HALF_UP);
        ObservableList<PieChart.Data> dDiskUsageChartData = diskDUsageChart.getData();
        p5 = new PieChart.Data("D盘占用" + diskDUsage + "%", diskDUsage);
        dDiskUsageChartData.add(p5);
        p6 = new PieChart.Data("D盘剩余" + remainDUsage + "%", remainDUsage);
        dDiskUsageChartData.add(p6);

        MyThreadPool.getInstance().submit(() -> {
            WmicSystemInfoFetcher systemInfoFetcher = new WmicSystemInfoFetcher();
            SystemInfo systemInfo = systemInfoFetcher.fetchSystemInfo();
            Platform.runLater(() -> {
                systemVersionLabel.setText(systemInfo.getCaption());
                cpuLabel.setText(systemInfo.getCpuInfos().stream().map((e) -> e.getName() + " " + e.getNumberOfCores() + "核 " + e.getNumberOfLogicalProcessors() + "线程").collect(Collectors.joining(",")));
                ramLabel.setText(systemInfo.getMemoryInfos().stream().map((e) -> e.getCapacity() / 1024 / 1024 / 1024 + "GB").map(String::valueOf).collect(Collectors.joining("+")));
                diskLabel.setText(systemInfo.getDiskInfos().stream().map((e) -> e.getSize() / 1024 / 1024 / 1024 + "GB").collect(Collectors.joining("+")));
            });
        });

//        ObservableList<PieChart.Data> ixlabUsageChartData = ixlabUsageChart.getData();
//        p5 = new PieChart.Data("ixlab文件夹占用" + cpuUsage + "%", cpuUsage);
//        dDiskUsageChartData.add(p5);
//        p6 = new PieChart.Data("D盘剩余" + remainCpuUsage + "%", remainCpuUsage);
//        dDiskUsageChartData.add(p6);

        p1.getNode().setStyle("-fx-pie-color: #2196F3;");
        p2.getNode().setStyle("-fx-pie-color: white;");
        p3.getNode().setStyle("-fx-pie-color: #2196F3;");
        p4.getNode().setStyle("-fx-pie-color: white;");
        p5.getNode().setStyle("-fx-pie-color: #2196F3;");
        p6.getNode().setStyle("-fx-pie-color: white;");

        AutoSingleRegistry.getInstance().addUsageInfoListener(new UsageInfoListener() {
            @Override
            public void usageInfoChange(UsageInfo usageInfo) {
                double diskDUsage = -1;
                long ixlabUsed = -1;
                if (diskQueryLimiter <= 1) {
                    try {
                        diskDUsage = DiskUsageUtil.diskUsage("D://");
                        ixlabUsed = DiskUsageUtil.usedSpace("D://ixlab/");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    diskQueryLimiter = 100;
                } else {
                    diskQueryLimiter--;
                }
                double finalDiskDUsage = diskDUsage;
                long finalIxlabUsed = ixlabUsed;
                Platform.runLater(() -> {

//                    diskCUsageChart.getData().add(new PieChart.Data(usageInfo.getCpuUsage() + "%", usageInfo.getCpuUsage()));
//                    diskDUsageChart.getData().add(new PieChart.Data("D盘占用率", usageInfo.getCpuUsage()));

//                    ObservableList<PieChart.Data> memoryUsageChartData = memoryUsageChart.getData();
                    BigDecimal one = new BigDecimal("100");
                    BigDecimal memoryUsage = BigDecimal.valueOf(usageInfo.getMemoryUsagePercentage()).setScale(2, RoundingMode.HALF_UP);
//                            MathUtil.twoPointDouble(usageInfo.getMemoryUsagePercentage());
                    BigDecimal remainMemoryUsage = one.subtract(memoryUsage).setScale(2, RoundingMode.HALF_UP);

                    BigDecimal cpuUsage = BigDecimal.valueOf(usageInfo.getCpuUsage()).setScale(2, RoundingMode.HALF_UP);
//                            MathUtil.twoPointDouble(usageInfo.getCpuUsage());
                    BigDecimal remainCpuUsage = one.subtract(cpuUsage).setScale(2, RoundingMode.HALF_UP);

                    p1.setName("内存占用" + memoryUsage + "%");
                    p1.setPieValue(memoryUsage.doubleValue());

                    p2.setName("内存剩余" + remainMemoryUsage + "%");
                    p2.setPieValue(remainMemoryUsage.doubleValue());

                    if (cpuUsage.doubleValue() > 0) {
                        p3.setName("cpu占用" + cpuUsage + "%");
                        p3.setPieValue(cpuUsage.doubleValue());

                        p4.setName("cpu剩余" + remainCpuUsage + "%");
                        p4.setPieValue(remainCpuUsage.doubleValue());
                    }

                    if (finalDiskDUsage > 0) {
                        BigDecimal diskDUsageDec = BigDecimal.valueOf(finalDiskDUsage).setScale(0, RoundingMode.HALF_UP);
                        BigDecimal remainDUsageDec = one.subtract(diskDUsageDec).setScale(0, RoundingMode.HALF_UP);
                        double v = diskDUsageDec.doubleValue();
                        p5.setName("D盘占用" + v + "%");
                        p5.setPieValue(v);
                        double v1 = remainDUsageDec.doubleValue();
                        p6.setName("D盘剩余" + v1 + "%");
                        p6.setPieValue(v1);

                        BigDecimal gb = BigDecimal.valueOf(finalIxlabUsed).divide(BigDecimal.valueOf(1024)).setScale(3, RoundingMode.HALF_UP);
                        ixlabUsage.setText(gb + "GB");
                    }
                });
            }
        });

        cpuUsageChart.setLabelsVisible(true);
        diskDUsageChart.setLabelsVisible(true);
        memoryUsageChart.setLabelsVisible(true);

        cpuUsageChart.setLabelLineLength(10);
        diskDUsageChart.setLabelLineLength(10);
        memoryUsageChart.setLabelLineLength(10);

        cpuUsageChart.setStartAngle(90);
        diskDUsageChart.setStartAngle(90);
        memoryUsageChart.setStartAngle(90);
    }

    int diskQueryLimiter = 1;

}
