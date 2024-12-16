package io.github.hellomaker.launcher.controller.dashboard;

import io.github.hellomaker.launcher.app.Storage;
import io.github.hellomaker.launcher.app.service.ServiceItem;
import io.github.hellomaker.launcher.app.service.ServiceItemRegister;
import io.github.hellomaker.launcher.app.service.ServiceItemRegisterFactory;
import io.github.hellomaker.launcher.app.service.impl.AutoListenStrategy;
import io.github.hellomaker.launcher.app.service.impl.ServiceItemRegisterImpl;
import io.github.hellomaker.launcher.common.TimeUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * @author hellomaker
 */
public class BottomController {

    @FXML
    public Rectangle redisStatus;

    @FXML
    public Rectangle mysqlStatus;
    @FXML
    public Rectangle ixlabStatus;

    Paint red = Paint.valueOf("#FF0000");
    Paint blue = Paint.valueOf("#87CEEB");
    Paint grey = Paint.valueOf("#A9A9A9");
    Paint green = Paint.valueOf("#228B22");

    ServiceItemRegister serviceItemRegister;

    @FXML
    public void initialize() {

        TimeUtil.startPoint();

        serviceItemRegister = ServiceItemRegisterFactory.getRegister("auto");
        if (serviceItemRegister == null) {
            serviceItemRegister = new ServiceItemRegisterImpl(new AutoListenStrategy());
        }

        //MYSQL8 MYSQL(iXLab)
        serviceItemRegister.register(new ServiceItem("MYSQL8", "bottom"), statusEnum -> {
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

        serviceItemRegister.register(new ServiceItem("redis", "bottom"), statusEnum -> {
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

        serviceItemRegister.start();

        Storage.getInstance().addStatusListener(statusEnum -> {
            Platform.runLater(() -> {
//                Text statusText = (Text) licensePane.lookup("#status");
                switch (statusEnum) {
                    case RUNNING:
                        ixlabStatus.setFill(blue);
                        break;
                    case NOT_RUNNING:
                        ixlabStatus.setFill(grey);
                        break;
                    case RUNNING_FAILED:
                        ixlabStatus.setFill(red);
                        break;
                    case IN_RUNNING:
                        ixlabStatus.setFill(green);
                        break;
                    default:
                        ixlabStatus.setFill(grey);
                        break;
                }
            });
        });

        TimeUtil.printMillsToStartPoint("bottom init");
    }

}
