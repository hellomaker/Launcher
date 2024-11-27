package io.github.hellomaker.launcher.verify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author hellomaker
 */
public class SystemUtil {

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

}
