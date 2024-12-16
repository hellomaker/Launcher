package io.github.hellomaker.launcher.controller.dashboard;

import io.github.hellomaker.launcher.app.AppConst;
import io.github.hellomaker.launcher.app.ProcessUtil;
import io.github.hellomaker.launcher.app.Storage;
import io.github.hellomaker.launcher.app.service.ServiceItem;
import io.github.hellomaker.launcher.app.service.ServiceItemRegister;
import io.github.hellomaker.launcher.app.service.ServiceItemRegisterFactory;
import io.github.hellomaker.launcher.app.service.impl.AutoListenStrategy;
import io.github.hellomaker.launcher.app.service.impl.ServiceItemRegisterImpl;
import io.github.hellomaker.launcher.pool.MyThreadPool;
import io.github.hellomaker.launcher.verify.VerifyInfo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class IndexController {

    private static final Map<String, String> serviceMap = new HashMap<>() {
        {
            put("redis", "redis");
//            put("mysql", "MYSQL(iXLab)");
            put("mysql", "MYSQL8");

            put("DsDocServiceSvc", "DsDocServiceSvc");
            put("DsProxySvc", "DsProxySvc");
            put("DsConverterSvc", "DsConverterSvc");
        }
    };

    private static final Map<String, String> confPathMap = new HashMap<>() {
        {
            put("redis", "D:\\iXLab\\condition\\Redis-x64-5.0.10/redis-windows.conf");
//            put("mysql", "MYSQL(iXLab)");
            put("mysql", "D:\\iXLab\\condition\\mysql8.0.39\\my.ini");
//            put("redis-log", "D:\\iXLab\\condition\\Redis-6.2/redis.log");
//            put("mysql-log", "D:\\iXLab\\condition\\MySql\\MySQL\\Data/ADMIN-PC.err");
            put("redis-log", "D:\\iXLab\\condition\\Redis-x64-5.0.10");
            put("mysql-log", "D:\\iXLab\\condition\\mysql8.0.39\\Data");
            put("ixlab-log", "D:\\iXLab");
        }
    };

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @FXML
    public Button mysqlStartButton;
    @FXML
    public Button redisStartButton;
    @FXML
    public Button ixlabStartButton;
    @FXML
    public Rectangle dsConverterSvcStatus;
    @FXML
    public Rectangle dsDocServiceSvcStatus;
    @FXML
    public Rectangle dsProxySvcStatus;
    @FXML
    public Button dsConverterSvcStartButton;
    @FXML
    public Button dsDocServiceSvcStartButton;
    @FXML
    public Button dsProxySvcStartButton;

    @FXML
    Rectangle mysqlStatus;

    @FXML
    Rectangle redisStatus;

    @FXML
    TextArea logArea;

    @FXML
    Rectangle ixlabStatus;

    ServiceItemRegister serviceItemRegister;

    @FXML
    void startItem(ActionEvent event) {
        Button source = (Button) event.getSource();
        String id = source.getParent().getId();
        String text = source.getText();
//        System.out.println(id);
        if ("启动".equals(text)) {
            logArea.appendText("启动" + id + "服务...\n");
        } else {
            logArea.appendText("关闭" + id + "服务...\n");
        }
        MyThreadPool.getInstance().submit(() -> {
            if (serviceMap.containsKey(id)) {
                String serviceName = serviceMap.get(id);
                try {
                    if ("启动".equals(text)) {
                        ProcessUtil.startService(serviceName);
                    } else {
                        ProcessUtil.stopService(serviceName);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }
            }
        });
    }

    @FXML
    void startInstallAll(ActionEvent event) {

    }

    @FXML
    void restartItem(ActionEvent event) {
        Button source = (Button) event.getSource();
        String id = source.getParent().getId();
        logArea.appendText("重启" + id + "服务...\n");
//        System.out.println(id);
        MyThreadPool.getInstance().submit(() -> {
            if (serviceMap.containsKey(id)) {
                String serviceName = serviceMap.get(id);
                try {
                    ProcessUtil.restartService(serviceName);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }
            }
        });
    }

    Paint red = Paint.valueOf("#FF0000");
    Paint blue = Paint.valueOf("#87CEEB");
    Paint grey = Paint.valueOf("#A9A9A9");
    Paint green = Paint.valueOf("#228B22");

    @FXML
    public void initialize() {
        MyThreadPool.getInstance().submit(this::init);
    }

    private void init() {
        serviceItemRegister = ServiceItemRegisterFactory.getRegister("auto");
        if (serviceItemRegister == null) {
            serviceItemRegister = new ServiceItemRegisterImpl(new AutoListenStrategy());
        }

        serviceItemRegister.register(new ServiceItem("MYSQL8", "index"), statusEnum -> {
            Platform.runLater(() -> {
                switch (statusEnum) {
                    case RUNNING:
                        mysqlStatus.setFill(blue);
                        mysqlStartButton.setText("停止");
                        mysqlStartButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
                        logArea.appendText("mysql服务启动中。\n");
                        break;
                    case NOT_RUNNING:
                        mysqlStatus.setFill(grey);
                        mysqlStartButton.setText("启动");
                        mysqlStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("mysql服务已停止。\n");
                        break;
                    case RUNNING_FAILED:
                        mysqlStatus.setFill(red);
                        mysqlStartButton.setText("启动");
                        mysqlStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("mysql服务启动失败。\n");
                        break;
                    case IN_RUNNING:
                        mysqlStatus.setFill(green);
                        mysqlStartButton.setText("停止");
                        mysqlStartButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
                        logArea.appendText("mysql服务运行中。\n");
                        break;
                    default:
                        mysqlStatus.setFill(grey);
                        mysqlStartButton.setText("启动");
                        mysqlStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("mysql服务已停止。\n");
                        break;
                }
            });
        });

        serviceItemRegister.register(new ServiceItem("redis", "index"), statusEnum -> {
            Platform.runLater(() -> {
                switch (statusEnum) {
                    case RUNNING:
                        redisStatus.setFill(blue);
                        redisStartButton.setText("停止");
                        redisStartButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
                        logArea.appendText("redis服务启动中。\n");
                        break;
                    case NOT_RUNNING:
                        redisStatus.setFill(grey);
                        redisStartButton.setText("启动");
                        redisStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("redis服务已停止。\n");
                        break;
                    case RUNNING_FAILED:
                        redisStatus.setFill(red);
                        redisStartButton.setText("启动");
                        redisStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("redis服务启动失败。\n");
                        break;
                    case IN_RUNNING:
                        redisStatus.setFill(green);
                        redisStartButton.setText("停止");
                        redisStartButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
                        logArea.appendText("redis服务运行中。\n");
                        break;
                    default:
                        redisStatus.setFill(grey);
                        redisStartButton.setText("启动");
                        redisStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("redis服务已停止。\n");
                        break;
                }
            });
        });

        serviceItemRegister.register(new ServiceItem("DsDocServiceSvc", "index"), statusEnum -> {
            Platform.runLater(() -> {
                switch (statusEnum) {
                    case RUNNING:
                        dsDocServiceSvcStatus.setFill(blue);
                        dsDocServiceSvcStartButton.setText("停止");
                        dsDocServiceSvcStartButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
                        logArea.appendText("DsDocServiceSvc服务启动中。\n");
                        break;
                    case NOT_RUNNING:
                        dsDocServiceSvcStatus.setFill(grey);
                        dsDocServiceSvcStartButton.setText("启动");
                        dsDocServiceSvcStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("DsDocServiceSvc服务已停止。\n");
                        break;
                    case RUNNING_FAILED:
                        dsDocServiceSvcStatus.setFill(red);
                        dsDocServiceSvcStartButton.setText("启动");
                        dsDocServiceSvcStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("DsDocServiceSvc服务启动失败。\n");
                        break;
                    case IN_RUNNING:
                        dsDocServiceSvcStatus.setFill(green);
                        dsDocServiceSvcStartButton.setText("停止");
                        dsDocServiceSvcStartButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
                        logArea.appendText("DsDocServiceSvc服务运行中。\n");
                        break;
                    default:
                        dsDocServiceSvcStatus.setFill(grey);
                        dsDocServiceSvcStartButton.setText("启动");
                        dsDocServiceSvcStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("DsDocServiceSvc服务已停止。\n");
                        break;
                }
            });
        });

        serviceItemRegister.register(new ServiceItem("DsProxySvc", "index"), statusEnum -> {
            Platform.runLater(() -> {
                switch (statusEnum) {
                    case RUNNING:
                        dsProxySvcStatus.setFill(blue);
                        dsProxySvcStartButton.setText("停止");
                        dsProxySvcStartButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
                        logArea.appendText("DsProxySvc服务启动中。\n");
                        break;
                    case NOT_RUNNING:
                        dsProxySvcStatus.setFill(grey);
                        dsProxySvcStartButton.setText("启动");
                        dsProxySvcStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("DsProxySvc服务已停止。\n");
                        break;
                    case RUNNING_FAILED:
                        dsProxySvcStatus.setFill(red);
                        dsProxySvcStartButton.setText("启动");
                        dsProxySvcStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("DsProxySvc服务启动失败。\n");
                        break;
                    case IN_RUNNING:
                        dsProxySvcStatus.setFill(green);
                        dsProxySvcStartButton.setText("停止");
                        dsProxySvcStartButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
                        logArea.appendText("DsProxySvc服务运行中。\n");
                        break;
                    default:
                        dsProxySvcStatus.setFill(grey);
                        dsProxySvcStartButton.setText("启动");
                        dsProxySvcStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("DsProxySvc服务已停止。\n");
                        break;
                }
            });
        });

        serviceItemRegister.register(new ServiceItem("DsConverterSvc", "index"), statusEnum -> {
            Platform.runLater(() -> {
                switch (statusEnum) {
                    case RUNNING:
                        dsConverterSvcStatus.setFill(blue);
                        dsConverterSvcStartButton.setText("停止");
                        dsConverterSvcStartButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
                        logArea.appendText("DsConverterSvc服务启动中。\n");
                        break;
                    case NOT_RUNNING:
                        dsConverterSvcStatus.setFill(grey);
                        dsConverterSvcStartButton.setText("启动");
                        dsConverterSvcStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("DsConverterSvc服务已停止。\n");
                        break;
                    case RUNNING_FAILED:
                        dsConverterSvcStatus.setFill(red);
                        dsConverterSvcStartButton.setText("启动");
                        dsConverterSvcStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("DsConverterSvc服务启动失败。\n");
                        break;
                    case IN_RUNNING:
                        dsConverterSvcStatus.setFill(green);
                        mysqlStartButton.setText("停止");
                        mysqlStartButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
                        logArea.appendText("DsConverterSvc服务运行中。\n");
                        break;
                    default:
                        dsConverterSvcStatus.setFill(grey);
                        dsConverterSvcStartButton.setText("启动");
                        dsConverterSvcStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("DsConverterSvc服务已停止。\n");
                        break;
                }
            });
        });

//        serviceItemRegister.register();

        Storage.getInstance().addStatusListener(statusEnum -> {
            Platform.runLater(() -> {
//                Text statusText = (Text) licensePane.lookup("#status");
                switch (statusEnum) {
                    case RUNNING:
                        ixlabStatus.setFill(blue);
                        ixlabStartButton.setText("停止");
                        ixlabStartButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
                        logArea.appendText("ixlab服务启动中。\n");
                        break;
                    case NOT_RUNNING:
                        ixlabStatus.setFill(grey);
                        ixlabStartButton.setText("启动");
                        ixlabStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("ixlab服务已停止。\n");
                        break;
                    case RUNNING_FAILED:
                        ixlabStatus.setFill(red);
                        ixlabStartButton.setText("启动");
                        ixlabStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("ixlab服务启动失败。\n");
                        break;
                    case IN_RUNNING:
                        ixlabStatus.setFill(green);
                        ixlabStartButton.setText("停止");
                        ixlabStartButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
                        logArea.appendText("ixlab服务运行中。\n");
                        break;
                    default:
                        ixlabStatus.setFill(grey);
                        ixlabStartButton.setText("启动");
                        ixlabStartButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                        logArea.appendText("ixlab服务已停止。\n");
                        break;
                }
            });
        });
    }

    @FXML
    public void config(ActionEvent event) {
        Button source = (Button) event.getSource();
        String id = source.getParent().getId();
//        System.out.println(id);
        logArea.appendText("打开" + id + "配置文件...\n");
        if (confPathMap.containsKey(id)) {
            String configFilePath = confPathMap.get(id);
            MyThreadPool.getInstance().submit(() -> {
                try {
                    ProcessUtil.execCommand("notepad " + configFilePath);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }
            });
        }
    }

    @FXML
    public void logs(ActionEvent event) {
        Button source = (Button) event.getSource();
        String id = source.getParent().getId();
//        System.out.println(id);
        logArea.appendText("打开" + id + "日志文件夹...\n");
        if (confPathMap.containsKey(id + "-log")) {
            String configFilePath = confPathMap.get(id + "-log");
            MyThreadPool.getInstance().submit(() -> {
                try {
                    ProcessUtil.execCommand("explorer " + configFilePath);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }
            });
        }
    }

    @FXML
    public void startIXLABItem(ActionEvent actionEvent) {
        Button source = (Button) actionEvent.getSource();
        if ("启动".equals(source.getText())) {
            logArea.appendText("启动ixlab服务...\n");
        } else {
            logArea.appendText("关闭ixlab服务...\n");
        }
        MyThreadPool.getInstance().submit(() -> {
            if ("启动".equals(source.getText())) {
                try {
                    if (Storage.getInstance().isActive()) {
                        VerifyInfo verifyInfo = Storage.getInstance().getVerifyInfo();
//                        String pidByPort = ProcessUtil.findPidByPort(AppConst.APP_PORT);
//                        ProcessUtil.taskKill(pidByPort);
//                        log.info("close " + AppConst.APP_PORT + " : pid : " + pidByPort + " success.");
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
            } else {
                Storage.getInstance().stopApp();
            }
        });
    }

    @FXML
    public void restartIXLABItem(ActionEvent actionEvent) {
        Button source = (Button) actionEvent.getSource();
        logArea.appendText("重启ixlab服务...\n");
        MyThreadPool.getInstance().submit(() -> {
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
        });
    }

    @FXML
    public void clearLogArea(ActionEvent actionEvent) {
        logArea.setText("");
    }
}
