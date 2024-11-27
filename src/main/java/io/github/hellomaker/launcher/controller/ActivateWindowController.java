package io.github.hellomaker.launcher.controller;

import io.github.hellomaker.launcher.Storage;
import io.github.hellomaker.launcher.common.DateUtil;
import io.github.hellomaker.launcher.common.SymmetricEncryption;
import io.github.hellomaker.launcher.verify.SystemUtil;
import io.github.hellomaker.launcher.verify.VerifyInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.List;

public class ActivateWindowController {

    @FXML
    private TextArea verifyNumber;

    @FXML
    private Button activateButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Text serialNumber;

    @FXML
    void handleActivateButtonClick(ActionEvent event) {
        String activationCode = verifyNumber.getText().trim();
        String mainBoardSerialNumber = Storage.getInstance().getMainBoardSerialNumber();
        if (mainBoardSerialNumber == null) {
            return;
        }
        String encodeString = SymmetricEncryption.encodeSerialNumber(mainBoardSerialNumber);
        VerifyInfo verifyInfo = SymmetricEncryption.verifyInfo(encodeString, activationCode);
        if (verifyInfo != null) {
            Storage.getInstance().saveActiveCode(activationCode);
            verifyNumber.setText("激活成功！");
            Storage.getInstance().isActive();
        } else {
            verifyNumber.setText("激活失败！");
        }
        // 这里可以添加实际的激活逻辑，比如验证激活码
        System.out.println("Activating with activation code: " + activationCode);
    }

    @FXML
    void handleCancelButtonClick(ActionEvent event) {
        // 清空激活码输入区
        verifyNumber.clear();
    }

    @FXML
    public void initialize() {
        String mainBoardSerialNumber = Storage.getInstance().getMainBoardSerialNumber();
        if (mainBoardSerialNumber == null) {
            return;
        }
        String encodeString = SymmetricEncryption.encodeSerialNumber(mainBoardSerialNumber);
        serialNumber.setText(encodeString);

        VerifyInfo verifyInfo = new VerifyInfo();
        verifyInfo.setSerialNumber(encodeString);
        verifyInfo.setValidDate("2030-01-01");
//        verifyInfo.setValidMenuIdList(List.of(1012L,1013L,1014L,1015L,1016L,1017L,1018L,1019L,1020L,1021L,1022L,1023L,1024L,1025L));
        verifyInfo.setValidMenuIdList(List.of(1023L,1024L,1025L,1026L));
//        verifyInfo.setValidMenuNameList(List.of("库存管理系统","库存管理系统dev","人事档案（dev）","人事档案（本地测试）","人事档案（远程测试）","库存管理（多语言）","库存管理系统（本地）","库存管理系统 local","人事档案（dev）","电子表单","设备档案","库存管理(开发版)","设备档案","设备档案(整合)"));
        verifyInfo.setValidMenuNameList(List.of("库存管理(开发版)","设备档案","设备档案(整合)","电子表单"));
        verifyInfo.setValidTimes(-1L);

        String verifyNumber = SymmetricEncryption.encodeVerifyNumber(verifyInfo);
        System.out.println("verify code : " + verifyNumber);
        System.out.println("verify : " + SymmetricEncryption.verifyNumber(encodeString, "a" + verifyNumber.substring(1)));
        System.out.println("verify : " + SymmetricEncryption.verifyNumber(encodeString + "q", verifyNumber));
        System.out.println("verify : " + SymmetricEncryption.verifyNumber(encodeString, verifyNumber));
        System.out.println("verify info : " + SymmetricEncryption.verifyInfo(encodeString, verifyNumber));
    }
}