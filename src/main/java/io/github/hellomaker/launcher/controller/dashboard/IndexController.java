package io.github.hellomaker.launcher.controller.dashboard;

import io.github.hellomaker.launcher.app.ProcessUtil;
import io.github.hellomaker.launcher.app.StatusEnum;
import io.github.hellomaker.launcher.app.service.ServiceItem;
import io.github.hellomaker.launcher.app.service.ServiceItemRegister;
import io.github.hellomaker.launcher.app.service.ServiceItemRegisterFactory;
import io.github.hellomaker.launcher.app.service.impl.AutoListenStrategy;
import io.github.hellomaker.launcher.app.service.impl.ServiceItemRegisterImpl;
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
import java.util.function.Consumer;

public class IndexController {

    private static final Map<String, String> serviceMap = new HashMap<>() {
        {
            put("redis", "redis");
//            put("mysql", "MYSQL(iXLab)");
            put("mysql", "mysql");
        }
    };

    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @FXML
    public Rectangle mysqlStatus;

    @FXML
    public Rectangle redisStatus;

    @FXML
    public TextArea logArea;

    ServiceItemRegister serviceItemRegister;

    @FXML
    void startItem(ActionEvent event) {
        Button source = (Button) event.getSource();
        String id = source.getParent().getId();
        System.out.println(id);
        if (serviceMap.containsKey(id)) {
            String serviceName = serviceMap.get(id);
            try {
                ProcessUtil.startService(serviceName);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }

    @FXML
    void startInstallAll(ActionEvent event) {

    }

    @FXML
    void restartItem(ActionEvent event) {
        Button source = (Button) event.getSource();
        String id = source.getParent().getId();
        System.out.println(id);
        if (serviceMap.containsKey(id)) {
            String serviceName = serviceMap.get(id);
            try {
                ProcessUtil.restartService(serviceName);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }

    Paint red = Paint.valueOf("#FF0000");
    Paint blue = Paint.valueOf("#87CEEB");
    Paint grey = Paint.valueOf("#A9A9A9");
    Paint green = Paint.valueOf("#228B22");

    @FXML
    public void initialize() {
        serviceItemRegister = ServiceItemRegisterFactory.getRegister("auto");
        if (serviceItemRegister == null) {
            serviceItemRegister = new ServiceItemRegisterImpl(new AutoListenStrategy());
        }

        serviceItemRegister.register(new ServiceItem("MYSQL(iXLab)", "index"), statusEnum -> {
            Platform.runLater(() -> {
                switch (statusEnum) {
                    case RUNNING:
                        mysqlStatus.setFill(blue);
                        break;
                    case NOT_RUNNING:
                        mysqlStatus.setFill(grey);
                        break;
                    case RUNNING_FAILED:
                        mysqlStatus.setFill(red);
                        break;
                    case IN_RUNNING:
                        mysqlStatus.setFill(green);
                        break;
                    default:
                        mysqlStatus.setFill(grey);
                        break;
                }
            });
        });

        serviceItemRegister.register(new ServiceItem("redis", "index"), statusEnum -> {
            Platform.runLater(() -> {
                switch (statusEnum) {
                    case RUNNING:
                        redisStatus.setFill(blue);
                        break;
                    case NOT_RUNNING:
                        redisStatus.setFill(grey);
                        break;
                    case RUNNING_FAILED:
                        redisStatus.setFill(red);
                        break;
                    case IN_RUNNING:
                        redisStatus.setFill(green);
                        break;
                    default:
                        redisStatus.setFill(grey);
                        break;
                }
            });
        });

//        serviceItemRegister.register();

    }

}
