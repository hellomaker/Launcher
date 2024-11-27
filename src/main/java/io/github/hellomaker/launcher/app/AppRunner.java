package io.github.hellomaker.launcher.app;

import io.github.hellomaker.launcher.pool.MyThreadPool;

import java.io.*;

public abstract class AppRunner {
    static final String[] args = {

    };

    static final String jarPath = "E:\\Desktop\\materialManager\\git\\myjar\\jar/encrypt_starter-0.0.0.4.jar";

    public static void run(String passwd, String... args) throws Exception {
        MyThreadPool.getInstance().submit(
            () -> {
                runInternal(passwd, args);
            }
        );
    }

//    private void runWithBat(String passwd) {
//        // 要执行的命令
//        String command = "java --add-opens java.base/jdk.internal.loader=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.net=ALL-UNNAMED -Dfile.encoding=utf-8  -jar " + jarPath;
//        try {
//            // 获取 Runtime 实例
//            Runtime runtime = Runtime.getRuntime();
//
//            // 执行命令
//            Process process = runtime.exec(command);
//
//            //填充参数
//            paddingArgs(process, passwd);
//
//            // 获取命令的输出流
////            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//
//            // 读取命令的输出
//            String line;
////            while ((line = reader.readLine()) != null) {
////                System.out.println(line);
////            }
//
//            // 等待命令执行完成
//            int exitCode = process.waitFor();
//            System.out.println("Command executed with exit code: " + exitCode);
//
//            // 关闭资源
////            reader.close();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    private static void runInternal(String passwd, String... args) {
        // 要执行的命令
        StringBuilder command = new StringBuilder("java --add-opens java.base/jdk.internal.loader=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.net=ALL-UNNAMED -Dfile.encoding=utf-8  -jar " + jarPath);
        for (String arg : args) {
            command.append(" ").append(arg);
        }
        try {
            // 获取 Runtime 实例
            Runtime runtime = Runtime.getRuntime();

            // 执行命令
            Process process = runtime.exec(command.toString());

            //填充参数
            paddingArgs(process, passwd);

            // 获取命令的输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // 读取命令的输出
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待命令执行完成
            int exitCode = process.waitFor();
            System.out.println("Command executed with exit code: " + exitCode);

            // 关闭资源
            reader.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void paddingArgs(Process process, String passwd) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        writer.write("AES/CBC/PKCS5Padding");
        writer.newLine();
        writer.flush();
        writer.write("128");
        writer.newLine();
        writer.flush();
        writer.write("128");
        writer.newLine();
        writer.flush();
        writer.write(passwd);
        writer.newLine();
        writer.flush();
        writer.close();
    }


}