package io.github.hellomaker.launcher.controller;

import io.github.hellomaker.launcher.app.Storage;
import io.github.hellomaker.launcher.app.AppConst;
import io.github.hellomaker.launcher.app.ProcessUtil;
import io.github.hellomaker.launcher.app.StatusEnum;
import io.github.hellomaker.launcher.common.TimeUtil;
import io.github.hellomaker.launcher.pool.MyThreadPool;
import io.github.hellomaker.launcher.verify.VerifyInfo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

/**
 * @author hellomaker
 */
public class LicenseController {

    protected Logger log = LoggerFactory.getLogger(LicenseController.class);

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
                String pidByPort = ProcessUtil.findPidByPort(AppConst.APP_PORT);
                ProcessUtil.taskKill(pidByPort);
                log.info("close " + AppConst.APP_PORT + " : pid : " + pidByPort + " success.");
                if (verifyInfo.getValidSubSystemNameList() != null) {
                    String collect = verifyInfo
                            .getValidSubSystemNameList()
                            .stream()
                            .collect(Collectors.joining(","));// 用逗号连接
                    Storage.getInstance().run(collect);
                } else {
                    Storage.getInstance().run();
                }
                log.info("启动成功");
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
        MyThreadPool.getInstance().submit(this::init);
    }

    private void init() {

        Storage.getInstance().addActiveListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                VerifyInfo verifyInfo = Storage.getInstance().getVerifyInfo();
                if (verifyInfo != null) {
                    Platform.runLater(() -> {
                        //                    Text serialNumber = (Text) licensePane.lookup("#serialNumber");
                        serialNumber.setText(verifyInfo.getSerialNumber());
//                    Text validDate = (Text) licensePane.lookup("#validDate");
                        validDate.setText(verifyInfo.getValidDate());

                        if (verifyInfo.getValidSubSystemNameList() != null) {
//                        Text validMenuNameList = (Text) licensePane.lookup("#validMenuNameList");
//                        // 将每个 Long 转换为 String
//                        validMenuNameList.setText(String.join(",", verifyInfo
//                                .getValidMenuNameList()));

//                        Label validMenuNameList2 = (Label) licensePane.lookup("#validMenuNameList2");
                            validMenuNameList2.setText(String.join(",", verifyInfo
                                    .getValidSubSystemNameList()));
                        }
                    });
                }
            }
        });

//        TimeUtil.printMillsToLastPoint("license 1  ");

        Storage.getInstance().addStatusListener(statusEvent -> {
            Platform.runLater(() -> {
//                Text statusText = (Text) licensePane.lookup("#status");
                if (statusEvent == StatusEnum.NOT_RUNNING) {
                    status.setText("未运行");
                } else if (statusEvent == StatusEnum.RUNNING) {
                    status.setText("启动中");
                } else if (statusEvent == StatusEnum.IN_RUNNING) {
                    status.setText("正在运行");
                } else {
                    status.setText("启动失败");
                }
            });
        });

//        TimeUtil.printMillsToLastPoint("license 2  ");
//        TimeUtil.closePoint();
    }

    @FXML
    public void reActive(ActionEvent event) {
        Storage.getInstance().saveActiveCode("");
        Storage.getInstance().isActive();
    }

}
