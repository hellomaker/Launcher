package io.github.hellomaker.launcher.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;

public abstract class DiskUsageUtil {

    public static void main(String[] args) {
        // 指定要查询的磁盘分区路径
        String diskPath = "C:\\";
        try {
            diskUsage(diskPath);
        } catch (IOException e) {
            System.err.println("无法读取磁盘信息: " + e.getMessage());
        }
    }

    public static double diskUsage(String pathString) throws IOException {
        Path path = Paths.get(pathString);
        FileStore store = Files.getFileStore(path);

        long totalSpace = store.getTotalSpace() / (1024 * 1024); // 转换为MB
        long usableSpace = store.getUsableSpace() / (1024 * 1024); // 转换为MB
        long usedSpace = totalSpace - usableSpace;

        double usageRate = ((double) usedSpace / totalSpace) * 100;

//        DecimalFormat df = new DecimalFormat("#.##");

//        System.out.println("磁盘: " + pathString);
//        System.out.println("总大小: " + totalSpace + " MB");
//        System.out.println("已用空间: " + usedSpace + " MB");
//        System.out.println("剩余空间: " + usableSpace + " MB");
//        System.out.println("使用率: " + df.format(usageRate) + "%");
        return usageRate;
    }

    public static long usedSpace(String pathString) throws IOException {
//        Path path = Paths.get(pathString);
//        FileStore store = Files.getFileStore(path);
//        long totalSpace = store.getTotalSpace() / (1024 * 1024); // 转换为MB
//        long usableSpace = store.getUsableSpace() / (1024 * 1024); // 转换为MB
//        long usedSpace = totalSpace - usableSpace;
//        return usedSpace;
//        try {
//            Process process = Runtime.getRuntime().exec("cmd /c dir /s /-c " + pathString);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            long totalSize = 0;
//            while ((line = reader.readLine()) != null) {
//                if (line.startsWith("     Total Files Listed:")) {
//                    String[] parts = line.split("\\s+");
//                    totalSize = Long.parseLong(parts[parts.length - 2].replace(",", ""));
//                }
//            }
//            reader.close();
////            System.out.println("Directory size: " + totalSize / 1024 / 1024 + " MB");
//            return totalSize / 1024 / 1024;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
        Path path = Paths.get(pathString);
        return getDirectorySize(path);
    }

    public static long getDirectorySize(Path dir) throws IOException {
        final class SizeVisitor extends SimpleFileVisitor<Path> {
            private long size = 0;

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                size += attrs.size();
                return super.visitFile(file, attrs);
            }

            public long getSize() {
                return size;
            }
        }

        SizeVisitor visitor = new SizeVisitor();
        Files.walkFileTree(dir, visitor);
        return visitor.getSize() / 1024 / 1024;
    }

//    public static void main(String[] args) {
//        try {
//            Path path = Paths.get("C:\\path\\to\\your\\directory"); // 替换为你的目录路径
//            long sizeInBytes = getDirectorySize(path);
//            System.out.println("Directory size: " + sizeInBytes / 1024 / 1024 + " MB");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}