package io.github.hellomaker.launcher;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class SceneSwitcher {

    private static final Logger log = LoggerFactory.getLogger(SceneSwitcher.class);
    static Map<String, Scene> scenes = new HashMap<>();
    static Map<Scene, Stage> stages = new HashMap<>();
    public static void registerScene(String sceneName, Scene scene, Stage stage) {
        if (scenes.containsKey(sceneName)) {
            log.error("已经包含scene{}", sceneName);
            return;
        } else if (stages.containsKey(scene)) {
            log.error("已经包含stage{}", sceneName);
            return;
        }
        scenes.put(sceneName, scene);
        stages.put(scene, stage);
    }

    public static void switchScene(String sceneName) {
        Scene scene = scenes.get(sceneName);
        if (scene == null) {
            log.error("没有该scene{}", sceneName);
            return;
        }
        Stage stage = stages.get(scene);
        if (stage == null) {
            log.error("没有该stage{}", sceneName);
            return;
        }
        stages.values().forEach(
            (e) -> {
                if (e != stage) {
                    e.close();
                } else {
                    stage.setScene(scene);
                    stage.show();
                }
            }
        );
    }


}
