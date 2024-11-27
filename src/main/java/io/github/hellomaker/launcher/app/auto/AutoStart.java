package io.github.hellomaker.launcher.app.auto;

import java.io.File;
import java.io.IOException;

/**
 * @author hellomaker
 */
public abstract class AutoStart {

    static final String REG_KEY = "HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run";
    static final String REG_KEY2 = "HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run";

    public static boolean autoStart(boolean isAuto, String appPath, String regAppName) throws IOException {
        File target = new File(appPath);                //是否有目标文件
        if (target.exists()) {
            String cmd = "reg " + (isAuto ? "add ":"delete ") + REG_KEY + " /v "
                    + regAppName + (isAuto ? " /t reg_sz /d " + appPath : " /f");
            Process p = Runtime.getRuntime().exec(cmd);
            try {
                p.getInputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                p.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                p.getErrorStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return !isAuto;
        }
    }

}
