package io.github.hellomaker.launcher;

import io.github.hellomaker.launcher.app.AppConst;
import io.github.hellomaker.launcher.app.AppRunner;
import io.github.hellomaker.launcher.app.ProcessUtil;
import io.github.hellomaker.launcher.app.StatusEnum;
import io.github.hellomaker.launcher.app.system.SystemInfo;
import io.github.hellomaker.launcher.common.SymmetricEncryption;
import io.github.hellomaker.launcher.verify.VerifyInfo;
import io.github.hellomaker.launcher.verify.storage.SaferStorage;
import io.github.hellomaker.launcher.verify.storage.SaferStorageImpl;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * @author hellomaker
 */
public class Storage {

    static Logger log = LoggerFactory.getLogger(Storage.class);

    final String storePath = "active/active.dat";
//    private String mainBoardSerialNumber;
    private SystemInfo systemInfo;
    private SaferStorage activeCodeStorage;

    static Storage ins;
    static {
        ins = new Storage();
        ins.activeCodeStorage = new SaferStorageImpl();
//        ins.mainBoardSerialNumber = SystemInfoFactory.getMainBoardSerialNumber();
        ins.systemInfo = SystemInfoFactory.fetchSystemInfo();
//        String activeCode = ins.getActiveCode();
//        boolean isActive = false;
//        if (activeCode != null) {
//            String encodeString = SymmetricEncryption.encodeSerialNumber(ins.mainBoardSerialNumber);
//            VerifyInfo verifyInfo = SymmetricEncryption.verifyInfo(encodeString, activeCode);
//            isActive = SymmetricEncryption.verifyNumber(encodeString, activeCode);
//            if (isActive) {
//                System.out.println("已激活：" + verifyInfo);
//            }
//        }
    }
    public static Storage getInstance() {
        return ins;
    }
    private Storage() {}

    public void saveActiveCode(String activeCode) {
        this.activeCodeStorage.saveTextToFile(activeCode, storePath);
    }

    private String getActiveCode() {
        String text = activeCodeStorage.getTextFromFile(storePath);
        return StringUtils.isEmpty(text) ? null : text;
    }

//    public String getMainBoardSerialNumber() {
//        return mainBoardSerialNumber;
//    }


    public SystemInfo getSystemInfo() {
        return systemInfo;
    }

    public String getEncodeSerialNumber() {
        return SymmetricEncryption.encodeSerialNumber(ins.systemInfo.getMotherboardInfo().getSerialNumber());
    }

    public void stopApp() {
        String pidByPort = null;
        try {
            pidByPort = ProcessUtil.findPidByPort(AppConst.APP_PORT);
            ProcessUtil.taskKill(pidByPort);
            status.set(StatusEnum.NOT_RUNNING);
        } catch (Exception e) {
            log.error("程序退出，执行任务错误：", e);
        }
    }

    public void whatStatus() {
        try {
            String pidByPort = ProcessUtil.findPidByPort(AppConst.APP_PORT);
//            Platform.runLater(() -> {
                if (StringUtils.isEmpty(pidByPort)) {
                    status.set(StatusEnum.NOT_RUNNING);
                } else {
                    status.set(StatusEnum.IN_RUNNING);
                }
//            });
        } catch (Exception e) {

        }
    }

    public boolean isActive() {
        boolean isActive = false;
        String activeCode = getActiveCode();
        if (activeCode != null) {
            String encodeString = SymmetricEncryption.encodeSerialNumber(ins.systemInfo.getMotherboardInfo().getSerialNumber());
            VerifyInfo verifyInfo = SymmetricEncryption.verifyInfo(encodeString, activeCode);
            isActive = SymmetricEncryption.verifyNumber(encodeString, activeCode);
            if (isActive) {
                System.out.println("已激活：" + verifyInfo);
            }
        }
        this.isActive.set(isActive);
        return isActive;
    }

    public VerifyInfo getVerifyInfo() {
        String activeCode = getActiveCode();
        if (activeCode != null) {
            String encodeString = SymmetricEncryption.encodeSerialNumber(ins.systemInfo.getMotherboardInfo().getSerialNumber());
            return SymmetricEncryption.verifyInfo(encodeString, activeCode);
        }
        return null;
    }

    BooleanProperty isActive = new SimpleBooleanProperty(false);
    public void addActiveListener(ChangeListener<Boolean> changeListener) {
        isActive.addListener(changeListener);
    }

    //0 未运行，1 正在运行，2 已运行，3 运行失败
    ObjectProperty<StatusEnum> status = new SimpleObjectProperty<>(StatusEnum.NOT_RUNNING);

    public void addStatusListener(Consumer<StatusEnum> changeListener) {
        status.addListener((observableValue, statusEnum, t1) -> changeListener.accept(t1));
        try {
            String pidByPort = ProcessUtil.findPidByPort(AppConst.APP_PORT);
            if (StringUtils.isEmpty(pidByPort)) {
                status.set(StatusEnum.NOT_RUNNING);
            } else {
                status.set(StatusEnum.IN_RUNNING);
            }
        } catch (Exception e) {

        }
    }

    public void run(String... args) {
        try {
            AppRunner.run("6z_yWCo$jVVweh_5", args);
            status.set(StatusEnum.IN_RUNNING);
        } catch (Exception e) {
            log.error("启动错误：", e);
            status.set(StatusEnum.RUNNING_FAILED);
        }
    }



}
