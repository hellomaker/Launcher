package io.github.hellomaker.launcher;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import io.github.hellomaker.launcher.app.AppConst;
import io.github.hellomaker.launcher.app.ProcessUtil;
import io.github.hellomaker.launcher.app.StatusEnum;
import io.github.hellomaker.launcher.pool.MyThreadPool;
import io.github.hellomaker.launcher.verify.VerifyInfo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author hellomaker
 */
@Slf4j
public class LauncherApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        AnchorPane licensePane = new FXMLLoader(LauncherApplication.class.getResource("license.fxml")).load();
        AnchorPane activePane = new FXMLLoader(LauncherApplication.class.getResource("active.fxml")).load();
        Scene licenseScene = new Scene(licensePane, 520, 380);
        Scene activeScene = new Scene(activePane, 520, 380);

        licensePane.setStyle("-fx-background-color: #F7F8FA;");
        activePane.setStyle("-fx-background-color: #F7F8FA;");
//                new FXMLLoader(LauncherApplication.class.getResource("main-view.fxml"));

//        Label hello = (Label)load.lookup("#welcomeText");
//        hello.setText("look up!");
//        HBox container = new HBox();
//        container.setMinWidth(520);
//        container.set
//        container.getChildren().add(activePane);

        // 注册JVM关闭钩子
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
                VerifyInfo verifyInfo = Storage.getInstance().getVerifyInfo();
                if (verifyInfo != null) {
                    Text serialNumber = (Text) licensePane.lookup("#serialNumber");
                    serialNumber.setText(verifyInfo.getSerialNumber());
                    Text validDate = (Text) licensePane.lookup("#validDate");
                    validDate.setText(verifyInfo.getValidDate());

                    if (verifyInfo.getValidMenuNameList() != null) {
//                        Text validMenuNameList = (Text) licensePane.lookup("#validMenuNameList");
//                        // 将每个 Long 转换为 String
//                        validMenuNameList.setText(String.join(",", verifyInfo
//                                .getValidMenuNameList()));

                        Label validMenuNameList2 = (Label) licensePane.lookup("#validMenuNameList2");
                        validMenuNameList2.setText(String.join(",", verifyInfo
                                .getValidMenuNameList()));
                    }
                }
                Platform.runLater(() -> {
                    stage.setScene(licenseScene);
                });
            } else {
                TextArea verifyNumber = (TextArea) activePane.lookup("#verifyNumber");
                verifyNumber.setText("");
                Platform.runLater(() -> {
                    stage.setScene(activeScene);
                });
            }
        });

        Storage.getInstance().addStatusListener(statusEvent -> {
            Platform.runLater(() -> {
                Text statusText = (Text) licensePane.lookup("#status");
                if (statusEvent == StatusEnum.NOT_RUNNING) {
                    statusText.setText("未运行");
                } else if (statusEvent == StatusEnum.RUNNING) {
                    statusText.setText("启动中");
                } else if (statusEvent == StatusEnum.IN_RUNNING) {
                    statusText.setText("正在运行");
                } else {
                    statusText.setText("启动失败");
                }
            });
        });

        Storage.getInstance().whatStatus();
        Storage.getInstance().isActive();
        stage.setScene(activeScene);
        stage.setTitle("Launcher");
//        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

//        createTrayIcon(primaryStage);
//        primaryStage.getIcons().add(new Image("file:res/logo.png"));
//设置为false时点击关闭按钮程序不会退出
        Platform.setImplicitExit(false);
        showIcon(stage);
    }

    public static void main(String[] args) {
        launch();
    }

    private void showIcon(Stage stage) {
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

            // We can also nest menus, below is an Options menu with sub-items
//        Menu menuOptions = new Menu("Options");
//        MenuItem miOn = new MenuItem("On");
//        miOn.setOnAction(e -> System.out.println("Options -> On clicked"));
//        MenuItem miOff = new MenuItem("Off");
//        miOff.setOnAction(e -> System.out.println("Options -> Off clicked"));
//        menuOptions.getItems().addAll(miOn, miOff);
//        icon.addMenuItem(menuOptions);
            icon.setOnAction((e) -> {
                stage.show();
//                icon.hide();
            });
            icon.show();
        } else {
            stage.setOnCloseRequest(e -> {
                stage.hide();
                MyThreadPool.getInstance().close();
                System.exit(0);
            });
        }
    }
}
