package cn.monkeyapp.mavd.controller;

import cn.monkeyapp.mavd.common.StyleManager;
import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.common.manage.StageHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 抽象的controller类，用于管理生成的所有stage，避免重复导致资源浪费。
 *
 * @author Corbett Zhang
 */
public abstract class AbstractController {

    private static final Logger LOGGER = LogManager.getLogger(AbstractController.class);

    /**
     * 管理所有Stage
     *
     * @param stage stage
     */
    protected void addStage(String key, Stage stage) {
        StageHelper.addStage(key, stage);
    }

    /**
     * 管理所有Stage
     *
     * @param key key
     */
    protected Stage getStage(String key) {
        return StageHelper.getStage(key);
    }

    /**
     * 删除指定key的stage,模糊匹配key
     *
     * @param k key
     */
    protected void removeStage(String k) {
        StageHelper.removeStage(k);
    }

    /**
     * 匹配key
     *
     * @param k key
     */
    protected boolean hasStage(String k) {
        return StageHelper.hasStage(k);
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
        StageHelper.closeAll();
    }

    /**
     * 打开指定窗口，其他窗口关闭
     */
    protected void showStage(String key) {
        StageHelper.showStage(key);
    }

    /**
     * 打开指定窗口，其他窗口关闭
     */
    protected void removeOtherStage(String key) {
        StageHelper.removeOtherStage(key);
    }

    /**
     * 退出程序
     */
    protected void exit() {
        StageHelper.exit();
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

        if (StageHelper.hasStage(key)) {
            return StageHelper.getStage(key);
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setController(controller);
//        Locale.setDefault(new Locale("en", "EN"));
        loader.setResources(ResourceBundle.getBundle("mavd", Locale.getDefault()));
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(url);
            Parent root = loader.load(Objects.requireNonNull(is, String.format("加载[%s]失败，请检查~ ", url)));
            LOGGER.log(Level.INFO, String.format("已加载[%s]", url));
            final Scene scene = new Scene(root);
            StyleManager.loadStageStyle(scene);
            stage.setScene(scene);
            stage.getIcons().add(new Image("img/logo.png"));
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
