package cn.monkeyapp.mavd.controller;

import cn.monkeyapp.mavd.common.manage.LogManager;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * 抽象的controller类，用于管理生成的所有stage，避免重复导致资源浪费。
 * 每个stage可以看做是一个窗口界面，这里统一管理便于界面的切换等操作
 *
 * @author Corbett Zhang
 */
public abstract class AbstractController {

    private static final Logger LOGGER = LogManager.getLogger(AbstractController.class);

    private static Map<String, Stage> stageMap = new ConcurrentHashMap<>();

    /**
     * 管理所有Stage
     *
     * @param stage stage
     */
    protected void addStage(String key, Stage stage) {
        stageMap.put(key, stage);
    }

    /**
     * 管理所有Stage
     *
     * @param key key
     */
    protected Stage getStage(String key) {
        return stageMap.get(key);
    }

    /**
     * 删除指定key的stage,模糊匹配key
     *
     * @param k key
     */
    protected void removeStage(String k) {
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
    protected boolean hasStage(String k) {
        return stageMap.containsKey(k);
    }

    /**
     * 关闭指定stage
     */
    protected void close(String key) {
        getStage(key).close();
    }

    /**
     * 关闭所有窗口
     */
    protected void closeAll() {
        stageMap.values().forEach(Stage::close);
    }

    /**
     * 打开指定窗口，其他窗口关闭
     */
    protected void showStage(String key) {
        stageMap.values().forEach(Stage::close);
        if (stageMap.containsKey(key)) {
            stageMap.get(key).show();
        }
    }

    /**
     * 关闭所有窗口，并退出程序
     */
    protected void exit() {
        stageMap.values().forEach(Stage::close);
        stageMap.clear();
        Platform.exit();
        System.exit(0);
    }

    /**
     * 抽象方法，用于子类对其他类提供创建Stage的功能
     *
     * @param primaryStage stage
     * @param listFxmlUrl  fxml地址
     * @return 实例化后的stage
     */
    protected abstract Stage loadStage(Stage primaryStage, String listFxmlUrl);

    /**
     * 显示在窗口上的名称，由子类实现
     *
     * @return title
     */
    protected abstract String stageTitle();

    /**
     * 通用加载stage方法
     *
     * @param stage      stage对象
     * @param url        fxml地址
     * @param controller 控制器
     * @return stage对象
     */
    protected Stage loadingStage(Stage stage, String url, AbstractController controller) {
        return loadingStage(stage, url, controller, controller.getClass().getName());
    }

    /**
     * 通用加载stage方法
     *
     * @param stage      stage对象
     * @param url        fxml地址
     * @param controller 控制器
     * @param key        map的key
     * @return stage对象
     */
    protected Stage loadingStage(Stage stage, String url, AbstractController controller, String key) {

        if (stageMap.containsKey(key)) {
            return stageMap.get(key);
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setController(controller);
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(url);
            Parent root = loader.load(Objects.requireNonNull(is, String.format("加载[%s]失败，请检查~ ", url)));
            LOGGER.log(Level.INFO, String.format("已加载[%s]", url));
            final Scene scene = new Scene(root);
            final URL cssUrl = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("css/stylesheet.css"), "加载stylesheet.css失败");
            scene.getStylesheets().add(cssUrl.toExternalForm());
            stage.setScene(scene);
            stage.getIcons().add(new Image("img/logo.png"));

//            MyMenu.showMenu(stage, controller);
            stage.setTitle(stageTitle());

            addStage(key, stage);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return stage;
    }


    //-------------------------窗口移动代码--------------------------
    private double x;
    private double y;

    protected void mouseDragged(MouseEvent mouseEvent) {
        Pane anchorPane = (Pane) mouseEvent.getSource();
        anchorPane.getScene().getWindow().setX(mouseEvent.getScreenX() - x);
        anchorPane.getScene().getWindow().setY(mouseEvent.getScreenY() - y);
    }

    protected void mousePressed(MouseEvent mouseEvent) {
        Pane anchorPane = (Pane) mouseEvent.getSource();
        x = mouseEvent.getScreenX() - anchorPane.getScene().getWindow().getX();
        y = mouseEvent.getScreenY() - anchorPane.getScene().getWindow().getY();
    }

}
