package io.github.hellomaker.launcher.app;

import io.github.hellomaker.launcher.app.service.ServiceStatus;

import java.io.BufferedReader;
import java.io.IOException;
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
//            Process process = Runtime.getRuntime().exec("taskkill /F /PID " + processId);

            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "taskkill /F /PID " + processId);
            pb.redirectErrorStream(true);
            Process process = pb.start();

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

    /**
     * Starts a Windows service.
     *
     * @param serviceName The name of the service to start.
     * @throws IOException If an I/O error occurs.
     */
    public static void startService(String serviceName) throws IOException, InterruptedException {
        execCommand("net start " + serviceName);
    }

    /**
     * Stops a Windows service.
     *
     * @param serviceName The name of the service to stop.
     * @throws IOException If an I/O error occurs.
     */
    public static void stopService(String serviceName) throws IOException, InterruptedException {
        execCommand("net stop " + serviceName);
    }

    /**
     * Restarts a Windows service.
     *
     * @param serviceName The name of the service to restart.
     * @throws IOException If an I/O error occurs.
     */
    public static void restartService(String serviceName) throws IOException, InterruptedException {
        stopService(serviceName);
        Thread.sleep(1000); // 等待一段时间确保服务完全停止
        startService(serviceName);
    }

    /**
     * Sets a Windows service to start automatically on boot.
     *
     * @param serviceName The name of the service to set as automatic.
     * @throws IOException If an I/O error occurs.
     */
    public static void setAutoStart(String serviceName) throws IOException, InterruptedException {
        execCommand("sc config " + serviceName + " start= auto");
    }

    /**
     * Sets a Windows service to not start automatically on boot (manual start).
     *
     * @param serviceName The name of the service to set as manual.
     * @throws IOException If an I/O error occurs.
     */
    public static void setManualStart(String serviceName) throws IOException, InterruptedException {
        execCommand("sc config " + serviceName + " start= demand");
    }

    /**
     * Disables a Windows service from starting at all.
     *
     * @param serviceName The name of the service to disable.
     * @throws IOException If an I/O error occurs.
     */
    public static void disableService(String serviceName) throws IOException, InterruptedException {
        execCommand("sc config " + serviceName + " start= disabled");
    }

    /**
     * Checks the status of a Windows service and returns it as an object.
     *
     * @param serviceName The name of the service to check.
     * @return A ServiceStatus object containing the service's information.
     * @throws IOException If an I/O error occurs.
     */
    public static ServiceStatus checkServiceStatus(String serviceName) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("sc query " + serviceName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            ServiceStatus serviceStatus = new ServiceStatus();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("SERVICE_NAME:")) {
                    serviceStatus.setServiceName(line.split(":")[1].trim());
                } else if (line.contains("STATE")) {
                    String[] parts = line.split(":");
                    if (parts.length > 1) {
                        String stateCode = parts[1].trim().split("\\s+")[0];
                        switch (stateCode) {
                            case "1":
                                serviceStatus.setStatus("Stopped");
                                serviceStatus.setStatusEnum(StatusEnum.NOT_RUNNING);
                                break;
                            case "2":
                                serviceStatus.setStatus("Start Pending");
                                serviceStatus.setStatusEnum(StatusEnum.RUNNING);
                                break;
                            case "3":
                                serviceStatus.setStatus("Stop Pending");
                                serviceStatus.setStatusEnum(StatusEnum.NOT_RUNNING);
                                break;
                            case "4":
                                serviceStatus.setStatus("Running");
                                serviceStatus.setStatusEnum(StatusEnum.IN_RUNNING);
                                break;
                            case "5":
                                serviceStatus.setStatus("Continue Pending");
                                serviceStatus.setStatusEnum(StatusEnum.NOT_RUNNING);
                                break;
                            case "6":
                                serviceStatus.setStatus("Pause Pending");
                                serviceStatus.setStatusEnum(StatusEnum.NOT_RUNNING);
                                break;
                            case "7":
                                serviceStatus.setStatus("Paused");
                                serviceStatus.setStatusEnum(StatusEnum.NOT_RUNNING);
                                break;
                            default:
                                serviceStatus.setStatusEnum(StatusEnum.NOT_RUNNING);
                                serviceStatus.setStatus("Unknown");
                        }
                    }
                } else if (line.contains("START_TYPE")) {
                    String[] parts = line.split(":");
                    if (parts.length > 1) {
                        String type = parts[1].trim().split("\\s+")[0];
                        switch (type) {
                            case "0x0":
                                serviceStatus.setStartType("Boot");
                                break;
                            case "0x1":
                                serviceStatus.setStartType("System");
                                break;
                            case "0x2":
                                serviceStatus.setStartType("Automatic");
                                break;
                            case "0x3":
                                serviceStatus.setStartType("Manual");
                                break;
                            case "0x4":
                                serviceStatus.setStartType("Disabled");
                                break;
                            default:
                                serviceStatus.setStartType("Unknown");
                        }
                    }
                }
            }
            process.waitFor();
            return serviceStatus;
        }
    }

    /**
     * Executes a system command and prints its output.
     *
     * @param command The command to execute.
     * @throws IOException If an I/O error occurs.
     */
    public static void execCommand(String command) throws IOException, InterruptedException {
//        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "chcp 65001 && " + command);
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", command);
        pb.redirectErrorStream(true); // 合并标准输出和错误输出
        Process process = pb.start();
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//        }
//        Process process = Runtime.getRuntime().exec("chcp 65001 &&" + command);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }
        }
        int exitCode = process.waitFor();
//        if (exitCode != 0) {
//            throw new IOException("Command failed with exit code: " + exitCode);
//        }
    }

    // 测试方法
    public static void main(String[] args) {
        try {
            // 示例：检查一个名为 'YourServiceName' 的服务状态
//            System.out.println(checkServiceStatus("redis"));

            execCommand("explorer C:/");

            // 示例：重启一个名为 'YourServiceName' 的服务，并设置为自动启动
//            restartService("YourServiceName");
//            setAutoStart("YourServiceName");

            // 替换 'YourServiceName' 为实际的服务名称。
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
