package io.github.hellomaker.launcher.app;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

public class DiskUsage {

    public static void main(String[] args) {
        // 指定要查询的磁盘分区路径
        String diskPath = "C:\\";
        try {
            printDiskUsage(diskPath);
        } catch (IOException e) {
            System.err.println("无法读取磁盘信息: " + e.getMessage());
        }
    }

    private static void printDiskUsage(String pathString) throws IOException {
        Path path = Paths.get(pathString);
        FileStore store = Files.getFileStore(path);

        long totalSpace = store.getTotalSpace() / (1024 * 1024); // 转换为MB
        long usableSpace = store.getUsableSpace() / (1024 * 1024); // 转换为MB
        long usedSpace = totalSpace - usableSpace;

        double usageRate = ((double) usedSpace / totalSpace) * 100;

        DecimalFormat df = new DecimalFormat("#.##");

        System.out.println("磁盘: " + pathString);
        System.out.println("总大小: " + totalSpace + " MB");
        System.out.println("已用空间: " + usedSpace + " MB");
        System.out.println("剩余空间: " + usableSpace + " MB");
        System.out.println("使用率: " + df.format(usageRate) + "%");
    }
}