package cn.monkeyapp.mavd.common.manage;

import cn.monkeyapp.mavd.controller.AbstractController;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * 每个stage可以看做是一个窗口界面，这里统一管理便于界面的切换等操作
 *
 * @author zhangcong
 */
public class StageHelper {

    private static final Logger LOGGER = LogManager.getLogger(StageHelper.class);

    private static Map<String, Stage> stageMap = new ConcurrentHashMap<>();

    /**
     * 管理所有Stage
     *
     * @param stage stage
     */
    public static void addStage(String key, Stage stage) {
        stageMap.put(key, stage);
    }

    /**
     * 管理所有Stage
     *
     * @param key key
     */
    public static Stage getStage(String key) {
        return stageMap.get(key);
    }

    /**
     * 删除指定key的stage,模糊匹配key
     *
     * @param k key
     */
    public static void removeStage(String k) {
        final Map<String, Stage> stringStageMap = stageMap.entrySet().stream()
                .filter((e) -> e.getKey().contains(k))
                .collect(Collectors.toMap((e) -> (String) e.getKey(), Map.Entry::getValue));
        stringStageMap.keySet().forEach(e -> stageMap.remove(e));
    }

    /**
     * 匹配key
     *
     * @param k key
     */
    public static boolean hasStage(String k) {
        return stageMap.containsKey(k);
    }

    /**
     * 关闭指定stage
     */
    public static void close(String key) {
        getStage(key).close();
    }

    /**
     * 关闭所有窗口
     */
    public static void closeAll() {
        stageMap.values().forEach(Stage::close);
    }

    /**
     * 打开指定窗口，其他窗口关闭
     */
    public static void showStage(String key) {
        stageMap.values().forEach(Stage::close);
        if (stageMap.containsKey(key)) {
            stageMap.get(key).show();
        }
    }

    /**
     * 打开指定窗口，其他窗口关闭
     */
    public static void showStage(String key, boolean resizable, AbstractController controller, String listFxmlUrl) {
        Stage stage;
        try {
            if (hasStage(key)) {
                stage = getStage(key);
            } else {
                Method method = controller.getClass().getDeclaredMethod("loadStage");
                stage = (Stage) method.invoke(controller, new Stage(), listFxmlUrl);
            }
            stage.setResizable(resizable);
            stage.show();
            stage.toFront();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * 关闭所有窗口，并退出程序
     */
    public static void exit() {
        stageMap.values().forEach(Stage::close);
        stageMap.clear();
        Platform.exit();
        System.runFinalization();
        System.exit(0);
    }

}