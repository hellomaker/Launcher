package io.github.hellomaker.launcher.controller;

import io.github.hellomaker.launcher.LauncherApplication;
import io.github.hellomaker.launcher.common.TimeUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    Logger log = LoggerFactory.getLogger(DashboardController.class);

    @FXML
    public ImageView homeIcon;

    @FXML
    public ImageView databaseIcon;

    @FXML
    public ImageView systemInfoIcon;

    @FXML
    public HBox indexItem;

    @FXML
    public HBox databaseItem;

    @FXML
    public HBox infoItem;

    @FXML
    public VBox bottomPane;

    @FXML
    public ImageView licenseInfoIcon;

    @FXML
    public HBox licenseItem;

    @FXML
    private VBox contentPane;


    private final List<HBox> itemList = new ArrayList<>();

    Node indexPane;

    Node databasePane;

    Node infoPane;

    Node licensePane;

    Node bottomPaneDetail;

    @FXML
    public void initialize() {
        try {
            TimeUtil.startPoint();
            indexPane = new FXMLLoader(LauncherApplication.class.getResource("dashboard/index.fxml")).load();
            TimeUtil.printMillsToLastPoint("初始化 index Pane ");
            databasePane = new FXMLLoader(LauncherApplication.class.getResource("dashboard/database.fxml")).load();
            TimeUtil.printMillsToLastPoint("初始化 database Pane  ");
            infoPane = new FXMLLoader(LauncherApplication.class.getResource("dashboard/info.fxml")).load();
            TimeUtil.printMillsToLastPoint("初始化 info Pane ");
            licensePane = new FXMLLoader(LauncherApplication.class.getResource("license.fxml")).load();
            TimeUtil.printMillsToLastPoint("初始化 license Pane  ");
            bottomPaneDetail = new FXMLLoader(LauncherApplication.class.getResource("dashboard/bottom.fxml")).load();
            TimeUtil.printMillsToLastPoint("初始化 bottom Pane ");
            TimeUtil.printMillsToStartPoint("解析多个fxml  ");
        } catch (Exception e) {
            log.error("dashborad初始化加载fxml错误", e);
        }

        itemList.add(indexItem);
        itemList.add(databaseItem);
        itemList.add(infoItem);
        itemList.add(licenseItem);
//        itemList.forEach((e) -> {
//            e.setOnMouseClicked((event) -> {
//                nowSelection(e);
//            });
//        });

        indexItem.setOnMouseClicked((e) -> {
            nowSelection(indexItem);
            switchToPage(indexPane);
        });
        databaseItem.setOnMouseClicked((e) -> {
            nowSelection(databaseItem);
            switchToPage(databasePane);
        });
        infoItem.setOnMouseClicked((e) -> {
            nowSelection(infoItem);
            switchToPage(infoPane);
        });
        licenseItem.setOnMouseClicked((e) -> {
            nowSelection(licenseItem);
            switchToPage(licensePane);
        });

        Platform.runLater(() -> {
            nowSelection(indexItem);
            switchToPage(indexPane);
            bottomPane.getChildren().add(bottomPaneDetail);
        });

    }

    private void nowSelection(HBox node) {
        itemList.forEach((n) -> {
            Label label = (Label) n.lookup("Label");
            if (n != node) {
//                n.setStyle("-fx-background-color: #2196F3; ");
//                label.setStyle("-fx-text-fill: white;");
                n.setBackground(new Background(new BackgroundFill(Paint.valueOf("#2196F3"), null, null)));
                label.setTextFill(Paint.valueOf("white"));
            } else {
//                n.setStyle("-fx-background-color: white; ");
//                label.setStyle("-fx-text-fill: #2196F3;");
                n.setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), null, null)));
                label.setTextFill(Paint.valueOf("#2196F3"));
            }
        });
    }

    private void switchToPage(Node page) {
        contentPane.getChildren().setAll(page);
    }
}
