package io.github.hellomaker.launcher.controller;

import io.github.hellomaker.launcher.SceneSwitcher;
import io.github.hellomaker.launcher.app.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;

public class LoginController {

    @FXML
    public PasswordField passwordField;

    final String passwd = "ixlab";

    @FXML
    public void handleLogin(ActionEvent actionEvent) {
        login();
    }

    @FXML
    public void initialize() {
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                login();
            }
        });
    }

    private void login() {
        String text = passwordField.getText();
        if (passwd.equals(text.trim())) {
            passwordField.clear();
            if (Storage.getInstance().isActive()) {
                SceneSwitcher.switchScene("dashboard");
            } else {
                SceneSwitcher.switchScene("active");
            }
        } else {
            passwordField.clear();
            passwordField.setPromptText("密码错误，请重新输入");
        }
    }
}
