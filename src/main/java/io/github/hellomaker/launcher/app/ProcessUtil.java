package io.github.hellomaker.launcher.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author hellomaker
 */
public abstract class ProcessUtil {

    public static void taskKill(String processId) {
        try {
            // 要终止的进程 ID
//            String processId = "12345"; // 替换为实际的进程 ID

            // 使用 taskkill 命令终止进程
            Process process = Runtime.getRuntime().exec("taskkill /F /PID " + processId);

            // 获取命令的输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // 读取并打印命令的输出
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待命令执行完成
            int exitCode = process.waitFor();
            System.out.println("Command executed with exit code: " + exitCode);

            // 关闭资源
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String findPidByPort(int port) throws Exception {
        // 使用 netstat 和 findstr 查找占用端口的 PID
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "netstat -ano | findstr :" + port);
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            // 解析输出，获取最后一列的 PID
            String[] parts = line.split("\\s+");
            if (parts.length > 4) {
                return parts[parts.length - 1];
            }
        }
        reader.close();
        return null;
    }

    private static void killProcess(String pid) throws Exception {
        // 使用 taskkill 终止进程
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "taskkill /F /PID " + pid);
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }

}
