module io.github.hellomaker.launcher {
    requires javafx.fxml;
    requires org.slf4j;
    requires com.dlsc.formsfx;
    requires org.apache.commons.lang3;
    requires static lombok;
    requires com.alibaba.fastjson2;
    requires commons.io;
    requires com.dustinredmond.fxtrayicon;
    requires javafx.controls;
    exports io.github.hellomaker.launcher.verify;
    exports io.github.hellomaker.launcher.app;
    opens io.github.hellomaker.launcher to javafx.fxml;
    exports io.github.hellomaker.launcher;
    exports io.github.hellomaker.launcher.controller;
    opens io.github.hellomaker.launcher.controller to javafx.fxml;

}