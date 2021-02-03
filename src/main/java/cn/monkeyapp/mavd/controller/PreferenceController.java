package cn.monkeyapp.mavd.controller;

import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.GlobalKeyListener;
import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.common.manage.ThreadPoolManager;
import cn.monkeyapp.mavd.entity.Preference;
import cn.monkeyapp.mavd.service.XmlService;
import cn.monkeyapp.mavd.service.impl.XmlServiceImpl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.net.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Corbett Zhang
 */
public class PreferenceController extends AbstractController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(PreferenceController.class);
    private static final XmlService xmlService = new XmlServiceImpl();
    private Preference preference;
    private ResourceBundle resourceBundle;

    /**
     * 对勾图标key
     */
    private final String CHECK_CIRCLE = "CHECK_CIRCLE";
    /**
     * 关闭图标key
     */
    private final String CLOSE = "CLOSE";

    //-----------------------头部的常规和网络-------------------------
    @FXML
    private HBox conventionHBox;
    @FXML
    private HBox networkHBox;
    @FXML
    private HBox shortcutsHBox;

    //-----------------------常规和网络主体pane-------------------------
    @FXML
    private ScrollPane conventionPane;
    @FXML
    private Pane networkPane;
    @FXML
    private ScrollPane shortcutsPane;

    //-----------------------常规部分-------------------------
    @FXML
    private ComboBox update;
    @FXML
    private JFXRadioButton lightness;
    @FXML
    private JFXRadioButton darkness;
    @FXML
    private JFXRadioButton auto;
    @FXML
    private JFXCheckBox enableNotification;

    //-----------------------网络部分-------------------------
    @FXML
    private Pane proxySetting;
    @FXML
    private ComboBox proxyType;
    @FXML
    private JFXTextField proxyAddress;
    @FXML
    private JFXTextField proxyPort;
    // 测试连接
    @FXML
    private JFXButton testConnect;
    // 保存代理设置
    @FXML
    private JFXButton saveProxy;
    // 使用代理
    @FXML
    private JFXRadioButton useProxyRadioButton;
    // 不使用代理
    @FXML
    private JFXRadioButton unusedProxyRadioButton;

    //-----------------------快捷键部分-------------------------
    @FXML
    private JFXCheckBox enableShortcuts;

    //-----------------------消息部分-------------------------
    @FXML
    private HBox message;
    @FXML
    private FontAwesomeIconView fontAwesomeIcon;
    @FXML
    private Label messageLabel;

    public PreferenceController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.resourceBundle = resources;

        this.preference = xmlService.getPreference();

        final ToggleGroup proxy = new ToggleGroup();
        useProxyRadioButton.setToggleGroup(proxy);
        unusedProxyRadioButton.setToggleGroup(proxy);

        final ToggleGroup style = new ToggleGroup();
        darkness.setToggleGroup(style);
        lightness.setToggleGroup(style);
        auto.setToggleGroup(style);
        darkness.setUserData(1);
        lightness.setUserData(2);
        auto.setUserData(3);

        // 样式，1：暗黑模式 2：明亮模式 3：自动
        if (preference.getStyle() == 1) {
            darkness.setSelected(true);
        } else if (preference.getStyle() == 2) {
            lightness.setSelected(true);
        } else if (preference.getStyle() == 3) {
            auto.setSelected(true);
        }
        style.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            preference.setStyle((Integer) newValue.getUserData());
            savePreference();
        });

        enableNotification.setSelected(preference.isEnableNotification());

        final ObservableList<String> updateList = FXCollections.observableArrayList("自动更新", "通知更新");
        update.setItems(updateList);
        update.getSelectionModel().select((int) preference.getAcceptUpdate());

        final ObservableList<String> proxyTypeList = FXCollections.observableArrayList("HTTP", "SOCKET");
        proxyType.setItems(proxyTypeList);

        if (preference.getIsProxy() == 1) {
            useProxyRadioButton.setSelected(true);
            proxySetting.setVisible(true);
            if (preference.getProxy() != null) {
                proxyType.getSelectionModel().select((int) preference.getProxy().getType());
                proxyAddress.setText(preference.getProxy().getHostname());
                proxyPort.setText(String.valueOf(preference.getProxy().getPort()));
            }
        } else {
            unusedProxyRadioButton.setSelected(true);
            proxySetting.setVisible(false);
        }

        conventionHBox.setStyle("-fx-background-color: rgb(244,244,244)");
        networkHBox.setStyle("-fx-background-color: rgb(255,255,255)");
        conventionPane.setVisible(true);
        networkPane.setVisible(false);
        shortcutsPane.setVisible(false);
        message.setVisible(false);
    }

    @FXML
    private void conventionHBoxMouseClicked(MouseEvent mouseEvent) {
        conventionHBox.setStyle("-fx-background-color: rgb(244,244,244)");
        networkHBox.setStyle("-fx-background-color: rgb(255,255,255)");
        shortcutsHBox.setStyle("-fx-background-color: rgb(255,255,255)");
        conventionPane.setVisible(true);
        networkPane.setVisible(false);
        shortcutsPane.setVisible(false);
    }

    @FXML
    private void networkHBoxMouseClicked(MouseEvent mouseEvent) {
        networkHBox.setStyle("-fx-background-color: rgb(244,244,244)");
        conventionHBox.setStyle("-fx-background-color: rgb(255,255,255)");
        shortcutsHBox.setStyle("-fx-background-color: rgb(255,255,255)");
        conventionPane.setVisible(false);
        networkPane.setVisible(true);
        shortcutsPane.setVisible(false);
    }

    @FXML
    private void shortcutsHBoxMouseClicked(MouseEvent mouseEvent) {
        shortcutsHBox.setStyle("-fx-background-color: rgb(244,244,244)");
        conventionHBox.setStyle("-fx-background-color: rgb(255,255,255)");
        networkHBox.setStyle("-fx-background-color: rgb(255,255,255)");
        conventionPane.setVisible(false);
        networkPane.setVisible(false);
        shortcutsPane.setVisible(true);
    }

    @FXML
    private void useProxyRadioButtonAction(ActionEvent actionEvent) {
        proxySetting.setVisible(true);
        preference.setIsProxy(1);
        savePreference();
    }

    @FXML
    private void unusedProxyRadioButtonAction(ActionEvent actionEvent) {
        proxySetting.setVisible(false);
        preference.setIsProxy(0);
        savePreference();
    }

    private void showMessage(String glyphName, String msg) {
        fontAwesomeIcon.setGlyphName(glyphName);
        fontAwesomeIcon.setGlyphStyle("-fx-fill:#FFF");
        fontAwesomeIcon.setGlyphSize(18);
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-color-label-visible: #FFF; -fx-text-fill: #E6A23C;");

        message.setOpacity(0);
        message.setVisible(true);
        KeyValue kv1 = new KeyValue(message.opacityProperty(), 0);
        KeyFrame kf1 = new KeyFrame(Duration.ZERO, kv1);
        KeyValue kv2 = new KeyValue(message.opacityProperty(), 1);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(0.2), kv2);
        KeyValue kv3 = new KeyValue(message.opacityProperty(), 1);
        KeyFrame kf3 = new KeyFrame(Duration.seconds(1.2), kv3);
        KeyValue kv4 = new KeyValue(message.opacityProperty(), 0);
        KeyFrame kf4 = new KeyFrame(Duration.seconds(1.4), kv4);
        KeyValue kv5 = new KeyValue(message.visibleProperty(), false);
        KeyFrame kf5 = new KeyFrame(Duration.seconds(1.5), kv5);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(kf1, kf2, kf3, kf4, kf5);
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.play();
    }

    @Override
    public Stage loadStage(Stage primaryStage, String url) {
        super.loadingStage(primaryStage, url, this);
        primaryStage.setOnCloseRequest(event -> {
            close(getClass().getName());
            removeStage(getClass().getName());
        });
        return primaryStage;
    }

    @FXML
    private void testConnectOnAction(ActionEvent actionEvent) {
        Task<Boolean> progressTask = new Task<Boolean>() {
            @Override
            protected Boolean call() {
                try {
                    final InetSocketAddress address = new InetSocketAddress(proxyAddress.getText(), Integer.parseInt(proxyPort.getText()));
                    // Authenticator.setDefault(new MyAuthenticator("username", "password"));// 设置代理的用户和密码
                    Proxy proxy;
                    if (proxyType.getSelectionModel().getSelectedIndex() == 0) {
                        URL realUrl = new URL("https://www.google.com");
                        proxy = new Proxy(Proxy.Type.HTTP, address);
                        final URLConnection conn = realUrl.openConnection(proxy);
                        conn.setConnectTimeout(2500);
                        conn.setReadTimeout(2500);
                        // 打开和URL之间的连接
                        final Object content = conn.getContent();
                        if (content != null) {
                            return true;
                        }
                    } else if (proxyType.getSelectionModel().getSelectedIndex() == 1) {
                        proxy = new Proxy(Proxy.Type.SOCKS, address);
                        Socket socket = new Socket(proxy);
                        //服务器的ip及地址
                        socket.connect(new InetSocketAddress("46.182.174.69", 80), 2500);
                        socket.close();
                        return true;
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
                return false;
            }
        };
        progressTask.setOnSucceeded(event -> {
            if (progressTask.getValue()) {
                showMessage(CHECK_CIRCLE, "连接成功");
            } else {
                showMessage(CLOSE, "连接失败");
            }
        });
        ThreadPoolManager.getInstance().addThreadExecutor(progressTask);
    }

    @FXML
    private void saveProxyOnAction(ActionEvent actionEvent) {
        try {
            if (useProxyRadioButton.isSelected()) {
                preference.setIsProxy(1);
                preference.setProxy(new Preference.Proxy(proxyType.getSelectionModel().getSelectedIndex(), proxyAddress.getText(), Integer.parseInt(proxyPort.getText())));
            } else if (unusedProxyRadioButton.isSelected()) {
                preference.setIsProxy(0);
            }
            savePreference();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            showMessage(CLOSE, "更新失败");
        }
    }

    private void savePreference() {
        xmlService.savePreference(preference);
        LocalCache.getInstance().add(preference, Properties.PREFERENCE_KEY);
        showMessage(CHECK_CIRCLE, "更新成功");
    }

    @FXML
    private void enableNotificationAction(ActionEvent actionEvent) {
        if (enableNotification.isSelected()) {
            preference.setEnableNotification(true);
        } else {
            preference.setEnableNotification(false);
        }
        savePreference();
    }

    @FXML
    private void updateAction(ActionEvent actionEvent) {
        preference.setAcceptUpdate(update.getSelectionModel().getSelectedIndex());
        savePreference();
    }

    @FXML
    private void enableShortcutsAction(ActionEvent actionEvent) {
        if (enableShortcuts.isSelected()) {
            registerNativeHook();
            preference.setEnableShortcut(true);
        } else {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e) {
                e.printStackTrace();
            }
            preference.setEnableShortcut(false);
        }
        savePreference();
    }

    private void registerNativeHook() {
        try {
            // 注册系统热键侦听
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new GlobalKeyListener(getStage(MainController.class.getName())));
        } catch (NativeHookException ex) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e) {
                LOGGER.log(Level.SEVERE, "There was a problem registering the native hook." + e.getMessage(), e);
            }
            LOGGER.log(Level.SEVERE, "There was a problem registering the native hook." + ex.getMessage(), ex);
        }
    }

    @Override
    protected String stageTitle() {
        return resourceBundle.getString("Preference");
    }

}
