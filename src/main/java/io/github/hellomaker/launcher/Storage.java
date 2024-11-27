package io.github.hellomaker.launcher;

import io.github.hellomaker.launcher.app.AppRunner;
import io.github.hellomaker.launcher.app.ProcessUtil;
import io.github.hellomaker.launcher.app.StatusEnum;
import io.github.hellomaker.launcher.common.SymmetricEncryption;
import io.github.hellomaker.launcher.verify.SystemUtil;
import io.github.hellomaker.launcher.verify.VerifyInfo;
import io.github.hellomaker.launcher.verify.storage.SaferStorage;
import io.github.hellomaker.launcher.verify.storage.SaferStorageImpl;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

/**
 * @author hellomaker
 */
@Slf4j
public class Storage {

    final String storePath = "active/active.dat";
    private String mainBoardSerialNumber;
    private SaferStorage activeCodeStorage;

    static Storage ins;
    static {
        ins = new Storage();
        ins.activeCodeStorage = new SaferStorageImpl();
        ins.mainBoardSerialNumber = SystemUtil.getMainBoardSerialNumber();
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

    public String getMainBoardSerialNumber() {
        return mainBoardSerialNumber;
    }

    public void stopApp() {
        String pidByPort = null;
        try {
            pidByPort = ProcessUtil.findPidByPort(8888);
            ProcessUtil.taskKill(pidByPort);
            status.set(StatusEnum.NOT_RUNNING);
        } catch (Exception e) {
            log.error("程序退出，执行任务错误：", e);
        }
    }

    public boolean isActive() {
        boolean isActive = false;
        String activeCode = getActiveCode();
        if (activeCode != null) {
            String encodeString = SymmetricEncryption.encodeSerialNumber(ins.mainBoardSerialNumber);
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
            String encodeString = SymmetricEncryption.encodeSerialNumber(ins.mainBoardSerialNumber);
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
            String pidByPort = ProcessUtil.findPidByPort(8888);
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
            AppRunner.run("hellomaker", args);
            status.set(StatusEnum.IN_RUNNING);
        } catch (Exception e) {
            log.error("启动错误：", e);
            status.set(StatusEnum.RUNNING_FAILED);
        }
    }



}