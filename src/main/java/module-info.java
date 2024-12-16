module io.github.hellomaker.launcher {
    requires javafx.fxml;
    requires org.apache.commons.lang3;
    requires com.alibaba.fastjson2;
    requires commons.io;
    requires com.dustinredmond.fxtrayicon;
    requires javafx.controls;
    requires org.slf4j;
    requires com.github.oshi;
    exports io.github.hellomaker.launcher.verify;
    exports io.github.hellomaker.launcher.app;
    opens io.github.hellomaker.launcher to javafx.fxml;
    exports io.github.hellomaker.launcher;
    exports io.github.hellomaker.launcher.controller;
    exports io.github.hellomaker.launcher.controller.dashboard;
    opens io.github.hellomaker.launcher.controller to javafx.fxml;
    opens io.github.hellomaker.launcher.controller.dashboard to javafx.fxml;
    exports io.github.hellomaker.launcher.app.system;
    exports io.github.hellomaker.launcher.app.system.info;
    exports io.github.hellomaker.launcher.app.service;
    opens io.github.hellomaker.launcher.app to javafx.fxml;
}