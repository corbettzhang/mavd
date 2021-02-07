package cn.monkeyapp.mavd.common.manage;

import cn.monkeyapp.mavd.controller.AbstractController;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.lang.reflect.Method;
import java.util.Collection;
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

    private static final Map<String, Stage> STAGE_MAP = new ConcurrentHashMap<>();

    /**
     * 添加stage到map中
     *
     * @param stage stage
     */
    public static void addStage(String key, Stage stage) {
        STAGE_MAP.put(key, stage);
    }

    /**
     * 返回所有stage
     *
     * @return stage集合
     */
    public static Collection<Stage> getAllStage() {
        return STAGE_MAP.values();
    }

    /**
     * 返回一个指定的stage
     *
     * @param key key
     */
    public static Stage getStage(String key) {
        return STAGE_MAP.get(key);
    }

    /**
     * 删除指定key的stage,模糊匹配key
     *
     * @param k key
     */
    public static void removeStage(String k) {
        final Map<String, Stage> stringStageMap = STAGE_MAP.entrySet().stream()
                .filter((e) -> e.getKey().contains(k))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        stringStageMap.keySet().forEach(STAGE_MAP::remove);
    }

    /**
     * 匹配key
     *
     * @param k key
     */
    public static boolean hasStage(String k) {
        return STAGE_MAP.containsKey(k);
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
        STAGE_MAP.values().forEach(Stage::close);
    }

    /**
     * 打开指定窗口，其他窗口关闭
     */
    public static void showStage(String key) {
        STAGE_MAP.values().forEach(Stage::close);
        if (STAGE_MAP.containsKey(key)) {
            STAGE_MAP.get(key).show();
        }
    }

    /**
     * 通过反射方式获取stage
     *
     * @param resizable   是否最大化
     * @param controller  具体的controller
     * @param listFxmlUrl fxml路径
     */
    public static void showStage(boolean resizable, AbstractController controller, String listFxmlUrl) {
        try {
            Method method = AbstractController.class.getDeclaredMethod("loadStage", Stage.class, String.class);
            method.setAccessible(true);
            Stage stage = (Stage) method.invoke(controller, new Stage(), listFxmlUrl);
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
        STAGE_MAP.values().forEach(Stage::close);
        STAGE_MAP.clear();
        Platform.exit();
        System.runFinalization();
        System.exit(0);
    }


    public static void removeOtherStage(String key) {
        showStage(key);
        final Map<String, Stage> stringStageMap = STAGE_MAP.entrySet().stream()
                .filter((e) -> !e.getKey().contains(key))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        stringStageMap.keySet().forEach(STAGE_MAP::remove);
    }
}