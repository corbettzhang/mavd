package cn.monkeyapp.mavd.controller;

import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.common.manage.ThreadPoolManager;
import cn.monkeyapp.mavd.common.MySystemTray;
import cn.monkeyapp.mavd.common.MyTimerTask;
import cn.monkeyapp.mavd.entity.Config;
import cn.monkeyapp.mavd.entity.Session;
import cn.monkeyapp.mavd.service.LoginService;
import cn.monkeyapp.mavd.service.XmlService;
import cn.monkeyapp.mavd.service.impl.LoginServiceImpl;
import cn.monkeyapp.mavd.service.impl.XmlServiceImpl;
import cn.monkeyapp.mavd.util.*;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 登录控制器
 *
 * @author Corbett Zhang
 */
public class LoginController extends AbstractController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(LoginController.class);
    private static final LoginService loginService = new LoginServiceImpl();
    private static final XmlService xmlService = new XmlServiceImpl();
    private Session session;

    @FXML
    private Button loginButton;
    @FXML
    private JFXSpinner loginSpinner;
    @FXML
    private JFXTextField usernameTextField;
    @FXML
    private JFXPasswordField passwordTextField;
    @FXML
    private Label registerLabel;
    @FXML
    private JFXCheckBox rememberCheckBox;
    @FXML
    private Button closeButton;
    @FXML
    private ImageView userImageView;
    @FXML
    private Pane mainPane;

    //-------------------------窗口移动代码--------------------------
    @FXML
    private void mainPaneMouseDragged(javafx.scene.input.MouseEvent mouseEvent) {
        mouseDragged(mouseEvent);
    }

    @FXML
    private void mainPaneMousePressed(javafx.scene.input.MouseEvent mouseEvent) {
        mousePressed(mouseEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = xmlService.getSession();

        InputStream stream;
        try {
            stream = new FileInputStream(new File(session.getImage()));
        } catch (Exception e) {
            try {
                stream = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream(session.getImage()));
            } catch (Exception e1) {
                stream = ClassLoader.getSystemClassLoader().getResourceAsStream("img/setting-account.png");
            }
        }
        userImageView.setImage(new Image(Objects.requireNonNull(stream, "加载文件出错" + session.getImage())));

        // 是否存在remember me，如果存在说明上次登录时正常
        if (StringUtils.isNotEmptyOrNull(session.getRememberMe())) {
            final String[] decode = AesUtil.decode(session.getRememberMe());
            // remember me时间未过期
            if (decode != null && decode.length == 3 && DateUtils.dateCompare(DateUtils.formatDate(new Date(), DateUtils.yyyyMMdd), decode[2], DateUtils.yyyyMMdd)) {
                rememberCheckBox.setSelected(true);
                usernameTextField.setText(decode[0]);
                passwordTextField.setText(decode[1]);
            } else {
                session.setRememberMe(null);
                xmlService.saveSession(session);
            }
        }

        LOGGER.log(Level.INFO, "Initialization login.fxml... ");
    }

    @FXML
    private void loginButtonClicked(ActionEvent actionEvent) {

        Service<Boolean> myThread = new Service<Boolean>() {
            @Override
            protected Task<Boolean> createTask() {
                return new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        if (StringUtils.isNotEmptyOrNull(usernameTextField.getText())
                                && StringUtils.isNotEmptyOrNull(passwordTextField.getText())) {

                            LOGGER.log(Level.INFO, String.format("处理用户登录，Username [%s] ，Password [%s]", usernameTextField.getText(), passwordTextField.getText()));

                            loginSpinner.setVisible(true);
                            loginButton.setDisable(true);

                            if (rememberCheckBox.isSelected() && StringUtils.isNotEmptyOrNull(session.getRememberMe())) {
                                final String[] decode = AesUtil.decode(session.getRememberMe());
                                // remember me时间未过期
                                if (decode != null && decode.length == 3 && DateUtils.dateCompare(DateUtils.formatDate(new Date(), DateUtils.yyyyMMdd), decode[2], DateUtils.yyyyMMdd)) {
                                    LocalCache.getInstance().add(session, Properties.SESSION_KEY);
                                    // 无需调用远程登录接口
                                    return usernameTextField.getText().equals(decode[0]) && passwordTextField.getText().equals(decode[1]);
                                }
                            }
                            Session sess = loginService.login(usernameTextField.getText(), passwordTextField.getText(), 60 * 60 * 24 * 21 + "");
                            if (!ObjectUtils.isEmpty(sess)) {
                                String avatar = loginService.getAvatar(sess.getCookie());
                                if (avatar != null) {
                                    final String[] strings = avatar.split("/");
                                    File file2 = new File(LocalCache.getInstance().get(Properties.PHOTO_PATH_KEY) + File.separator + strings[strings.length - 1]);
                                    if (!file2.exists()) {
                                        final boolean newFile = file2.createNewFile();
                                        LOGGER.log(Level.INFO, String.format("创建登录用户头像[%s]，%s", file2, newFile));
                                        final String urlStr = ((Config) LocalCache.getInstance().get(Properties.CONFIG_KEY)).getWebSite() + avatar;
                                        FileUtils.download(urlStr, file2);
                                    }
                                    sess.getUser().setAvatar(avatar);
                                    sess.setImage(file2.getPath());
                                } else {
                                    sess.setImage("img/setting-account.png");
                                }
                                sess.getUser().setIdentify(AesUtil.encode(usernameTextField.getText(), passwordTextField.getText()));

                                // 登录成功后，如果用户选择了remember me，则将用户信息加密后写入到文件中，加密方式采用 username$password$date
                                if (rememberCheckBox.isSelected()) {
                                    sess.setRememberMe(AesUtil.encode(usernameTextField.getText(), passwordTextField.getText(), 21));
                                } else {
                                    sess.setRememberMe(null);
                                }
                                xmlService.saveSession(sess);

                                LocalCache.getInstance().add(sess, Properties.SESSION_KEY);

                                return true;
                            }
                        }
                        return false;
                    }
                };
            }
        };
        myThread.exceptionProperty().addListener((observable, oldValue, newValue) -> {
            loginSpinner.setVisible(false);
            loginButton.setDisable(false);
            LOGGER.log(Level.SEVERE, newValue.getMessage(), newValue);
        });
        myThread.setOnSucceeded(event -> {
            if (myThread.getValue()) {
                showMainStage();
            } else {
                loadingFailedAnimation();
                LOGGER.log(Level.INFO, String.format("用户登录验证失败，Username [%s] Password [%s] ", usernameTextField.getText(), passwordTextField.getText()));
            }
            loginSpinner.setVisible(false);
            loginButton.setDisable(false);
        });

        myThread.start();

    }

    // 当退出登录时，stage被保存在map容器中，这里直接返回容器内的stage
    private void showMainStage() {
        if (hasStage(MainController.class.getName())) {
            showStage(MainController.class.getName());
        } else {
            // 启动定时任务，30秒后开始，每隔30秒执行一次
            ThreadPoolManager.getInstance().addScheduledExecutor(new MyTimerTask(), 30, 30, TimeUnit.SECONDS);

            Platform.runLater(() -> {
                final MainController mainController = new MainController();
                Stage stage = mainController.loadStage(new Stage(), Properties.MAIN_FXML_URL);
                MySystemTray.getInstance().enableTray(stage, mainController);
                stage.show();
                stage.toFront();
            });
            ((Stage) mainPane.getScene().getWindow()).close();
        }
    }

    /**
     * 登录失败时，加载动画，采用时间线的方式让button左右移动两次
     */
    private void loadingFailedAnimation() {
        KeyValue kv1 = new KeyValue(loginButton.translateXProperty(), 0);
        KeyFrame kf1 = new KeyFrame(Duration.ZERO, kv1);
        KeyValue kv2 = new KeyValue(loginButton.translateXProperty(), -5);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(0.05), kv2);
        KeyValue kv3 = new KeyValue(loginButton.translateXProperty(), 5);
        KeyFrame kf3 = new KeyFrame(Duration.seconds(0.15), kv3);
        KeyValue kv4 = new KeyValue(loginButton.translateXProperty(), 0);
        KeyFrame kf4 = new KeyFrame(Duration.seconds(0.20), kv4);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(kf1, kf2, kf3, kf4);
        timeline.setCycleCount(2);
        timeline.setAutoReverse(true);
        timeline.play();
    }

    @FXML
    private void paneKeyPressed(KeyEvent keyEvent) {
        // 当按下键盘 enter键时触发点击登录
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            try {
                loginButton.fire();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    @Override
    public Stage loadStage(Stage primaryStage, String loginFxmlUrl) {
        final Stage stage = super.loadingStage(primaryStage, loginFxmlUrl, this);
        stage.setOnCloseRequest(event -> exit());
        stage.setResizable(false);
        return stage;
    }

    @FXML
    private void skipLabelMouseClicked(MouseEvent mouseEvent) {
        // 无需登录，直接跳过。跳过之前，首先去把只下载到本地赋值上
        final Config config = xmlService.getConfig();
        config.setOnlyDownload(true);
        xmlService.saveConfig(config);
        LocalCache.getInstance().add(config, Properties.CONFIG_KEY);
        showMainStage();
    }

    @Override
    protected String stageTitle() {
        return null;
    }
}
