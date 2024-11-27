package io.github.hellomaker.launcher.controller;

import io.github.hellomaker.launcher.Storage;
import io.github.hellomaker.launcher.app.AppRunner;
import io.github.hellomaker.launcher.app.ProcessUtil;
import io.github.hellomaker.launcher.verify.VerifyInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

/**
 * @author hellomaker
 */
@Slf4j
public class LicenseController {

    @FXML
    Text serialNumber;

    @FXML
    Text validDate;

//    @FXML
//    Text validMenuNameList;

    @FXML
    Label validMenuNameList2;

    @FXML
    Text status;

    @FXML
    public void run() {
        try {
            if (Storage.getInstance().isActive()) {
                VerifyInfo verifyInfo = Storage.getInstance().getVerifyInfo();
                String pidByPort = ProcessUtil.findPidByPort(8888);
                ProcessUtil.taskKill(pidByPort);
                log.info("close 8888 : pid : " + pidByPort + " success.");
                if (verifyInfo.getValidMenuIdList() != null) {
                    String collect = verifyInfo
                            .getValidMenuIdList()
                            .stream()
                            .map(String::valueOf)  // 将每个 Long 转换为 String
                            .collect(Collectors.joining(","));// 用逗号连接
                    Storage.getInstance().run(collect);
                } else {
                    Storage.getInstance().run();
                }
            }
        } catch (Exception e) {
            log.error("启动错误：", e);
        }
    }

    @FXML
    public void stop() {
        Storage.getInstance().stopApp();
    }

    @FXML
    public void initialize() {
//        validMenuNameList.wrappingWidthProperty().bind(validMenuNameList.layoutBoundsProperty().get());
    }

    @FXML
    public void reActive(ActionEvent event) {
        Storage.getInstance().saveActiveCode("");
        Storage.getInstance().isActive();
    }

}
