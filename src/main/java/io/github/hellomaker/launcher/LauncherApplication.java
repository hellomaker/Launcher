package io.github.hellomaker.launcher;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import io.github.hellomaker.launcher.app.Storage;
import io.github.hellomaker.launcher.common.TimeUtil;
import io.github.hellomaker.launcher.pool.MyThreadPool;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author hellomaker
 */
//@Slf4j
public class LauncherApplication extends Application {

    Logger log = LoggerFactory.getLogger(LauncherApplication.class);

    @Override
    public void start(Stage stage) throws IOException {

        TimeUtil.startPoint();

//        AnchorPane licensePane = new FXMLLoader(LauncherApplication.class.getResource("license.fxml")).load();
        AnchorPane activePane = new FXMLLoader(LauncherApplication.class.getResource("active.fxml")).load();
        HBox dashboardPane = new FXMLLoader(LauncherApplication.class.getResource("dashboard.fxml")).load();
//        Scene licenseScene = new Scene(licensePane, 520, 380);
        Scene activeScene = new Scene(activePane, 750, 600);
        Scene dashboardScene = new Scene(dashboardPane, 750, 600);

        VBox loginPane = new FXMLLoader(LauncherApplication.class.getResource("login.fxml")).load();
        Scene loginScene = new Scene(loginPane, 380, 220);

        SceneSwitcher.registerScene("active", activeScene, stage);
        SceneSwitcher.registerScene("dashboard", dashboardScene, stage);
        SceneSwitcher.registerScene("login", loginScene, stage);

//        licensePane.setStyle("-fx-background-color: #F7F8FA;");
        activePane.setStyle("-fx-background-color: #F7F8FA;");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("JVM正在关闭，执行清理工作...");
            // 在这里添加您需要在JVM关闭时执行的代码
            // 例如：保存数据、释放资源等
            try {
//                Storage.getInstance().stopApp();
                log.info("shutdown应用进程成功");
            } catch (Exception e) {
                log.error("程序退出，执行任务错误：", e);
            }
        }));

//        stage.setOnCloseRequest(event -> {
//            MyThreadPool.getInstance().close();
//            log.info("close thread pool success..");
//            showIcon(stage);
//        });

        Storage.getInstance().addActiveListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                Platform.runLater(() -> {
//                    stage.setScene(licenseScene);
//                    stage.setScene(dashboardScene);
                    SceneSwitcher.switchScene("dashboard");
                });
            } else {
                Platform.runLater(() -> {
                    TextArea verifyNumber = (TextArea) activePane.lookup("#verifyNumber");
                    verifyNumber.setText("");
//                    stage.setScene(activeScene);
                    SceneSwitcher.switchScene("active");
                });
            }
        });

        TimeUtil.printMillsToLastPoint("加载fxml以及 添加监听器 ");
        Storage.getInstance().whatStatus();
//        Storage.getInstance().isActive();
        if (Storage.getInstance().isActive()) {
//            stage.setScene(dashboardScene);
            SceneSwitcher.switchScene("dashboard");
        } else {
//            stage.setScene(activeScene);
            SceneSwitcher.switchScene("active");
        }

        stage.setTitle("iXLABLauncher");
//        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

//        createTrayIcon(primaryStage);
//        primaryStage.getIcons().add(new Image("file:res/logo.png"));
//设置为false时点击关闭按钮程序不会退出

        Platform.setImplicitExit(false);
        showIcon(stage);

        stage.setOnCloseRequest((e) -> {
            stage.hide();
            //TODO 取消退出
//            MyThreadPool.getInstance().close();
//            System.exit(0);
        });

        TimeUtil.printMillsToStartPoint("启动总 ");
    }

    public static void main(String[] args) {
        launch();
    }

    private void showIcon(Stage stage) {
        try {
            if (FXTrayIcon.isSupported()) {
                FXTrayIcon icon = new FXTrayIcon(stage, getClass().getResource("logo.png"));
                MenuItem exit = new MenuItem("exit");
                icon.setIconSize(60, 24);
                exit.setOnAction(e ->

//            new Alert(Alert.AlertType.INFORMATION, "We just ran some JavaFX code from an AWT MenuItem!").showAndWait()
                        {
                            stage.hide();
                            MyThreadPool.getInstance().close();
                            System.exit(0);
                        }
                );
                icon.addMenuItem(exit);
                icon.setOnAction((e) -> {
//                    stage.show();
//                icon.hide();
                    SceneSwitcher.switchScene("login");
                });
                icon.show();
            } else {
                stage.setOnCloseRequest(e -> {
                    stage.hide();
                    MyThreadPool.getInstance().close();
                    System.exit(0);
                });
            }
        } catch (Exception e) {
            log.error("最小化图标支持错误", e);
        }
    }
}
