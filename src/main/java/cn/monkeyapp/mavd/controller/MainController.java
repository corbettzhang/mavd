package cn.monkeyapp.mavd.controller;

import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.entity.Session;
import cn.monkeyapp.mavd.service.SqliteService;
import cn.monkeyapp.mavd.service.XmlService;
import cn.monkeyapp.mavd.service.impl.SqliteServiceImpl;
import cn.monkeyapp.mavd.service.impl.XmlServiceImpl;
import cn.monkeyapp.mavd.util.ObjectUtils;
import com.jfoenix.controls.JFXHamburger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Corbett Zhang
 */
public class MainController extends AbstractController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(MainController.class);
    private static final XmlService xmlService = new XmlServiceImpl();

    //-------------------------------主窗体-------------------------------
    @FXML
    private HBox root;
    @FXML
    private VBox mainLeftVBox;
    @FXML
    private VBox mainRightVBox;

    //-------------------------------首页左侧-------------------------------
    @FXML
    private JFXHamburger burger;
    @FXML
    private Label mavdLabel;
    @FXML
    private Label listLabel;
    @FXML
    private Label settingLabel;


    @FXML
    private HBox mainHBox;

    @FXML
    private HBox listHBox;
    @FXML
    private HBox settingHBox;
    @FXML
    private HBox toolsHBox;
    @FXML
    private HBox chartHBox;

    //-------------------------------右上角用户信息-------------------------------
    @FXML
    private HBox userHBox;
    @FXML
    private ImageView userImage;
    @FXML
    private Label nickName;
    @FXML
    private ImageView userInfoImage;

    @FXML
    private void rootMouseDragged(javafx.scene.input.MouseEvent mouseEvent) {
        mouseDragged(mouseEvent);
    }

    @FXML
    private void rootMousePressed(javafx.scene.input.MouseEvent mouseEvent) {
        mousePressed(mouseEvent);
    }

    //-------------------------------右上角用户信息-------------------------------
    @FXML
    private void userInfoImageMouseClicked(MouseEvent mouseEvent) {
        Platform.runLater(() -> {

            final UserController userController = new UserController();
            final Stage stage = userController.loadStage(new Stage(), Properties.USER_FXML_URL);
            stage.setResizable(false);
            stage.setOnCloseRequest(event -> userController.close(userController.getClass().getName()));
            stage.show();

        });
    }

    @FXML
    private void userImageMouseClicked(MouseEvent mouseEvent) throws IOException {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("IMAGE", "*.jpg", "*.png", "*.gif", "*.jpeg"),
                new FileChooser.ExtensionFilter("All FILE", "*.*")
        );
        File file = fileChooser.showOpenDialog(((ImageView) mouseEvent.getSource()).getScene().getWindow());
        if (file != null) {
            userImage.setImage(new Image(new FileInputStream(file)));
            FileChannel inputChannel = null;
            FileChannel outputChannel = null;
            try {
                final String substring = file.getName().substring(file.getName().indexOf('.'));
                final String path = LocalCache.getInstance().get(Properties.PHOTO_PATH_KEY) + File.separator + UUID.randomUUID() + substring;
                final File file1 = new File(path);
                if (!file1.exists()) {
                    file1.createNewFile();
                }
                inputChannel = new FileInputStream(file).getChannel();
                outputChannel = new FileOutputStream(file1).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

                final Session session = ((Session) LocalCache.getInstance().get(Properties.SESSION_KEY));
                session.setImage(file1.getAbsolutePath());
                xmlService.saveSession(session);

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            } finally {
                inputChannel.close();
                outputChannel.close();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Session session = ((Session) LocalCache.getInstance().get(Properties.SESSION_KEY));
        // 跳过了登录
        if (session != null) {
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
            userImage.setImage(new Image(Objects.requireNonNull(stream, "加载文件出错")));
            nickName.setText(session.getUser().getNickname());
        } else {
            // 隐藏右上角用户信息
            userHBox.setVisible(false);
        }

        // 预加载list UI
        Platform.runLater(() -> {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(Properties.MAIN_LIST_FXML_URL);
            FXMLLoader loader = new FXMLLoader();
            try {
                listVBox = loader.load(Objects.requireNonNull(is));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // 预加载setting UI
        Platform.runLater(() -> {
            final SettingController settingController = new SettingController();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(Properties.MAIN_SETTING_FXML_URL);
            FXMLLoader loader = new FXMLLoader();
            loader.setController(settingController);
            try {
                settingVBox = loader.load(Objects.requireNonNull(is));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        bindAction(burger);

        mainLeftVBox.widthProperty().addListener((observable, oldValue, newValue) -> {
            listHBox.setPrefWidth(newValue.doubleValue() - 8);
            settingHBox.setPrefWidth(newValue.doubleValue() - 8);
            if (newValue.doubleValue() < 113) {
                mainHBox.getChildren().remove(mavdLabel);
                listHBox.getChildren().remove(listLabel);
                settingHBox.getChildren().remove(settingLabel);
            } else if (newValue.doubleValue() >= 105) {
                if (listHBox.getChildren().size() == 1) {
                    listHBox.getChildren().add(listLabel);
                }
                if (settingHBox.getChildren().size() == 1) {
                    settingHBox.getChildren().add(settingLabel);
                }
            }
        });
    }

    private VBox listVBox;
    private VBox settingVBox;

    @FXML
    private void listHBoxMouseClicked(MouseEvent mouseEvent) {
        if (ObjectUtils.isEmpty(listVBox)) {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(Properties.MAIN_LIST_FXML_URL);
            FXMLLoader loader = new FXMLLoader();
            try {
                listVBox = loader.load(Objects.requireNonNull(is));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mainRightVBox.getChildren().set(1, listVBox);
        VBox.setVgrow(listVBox, Priority.ALWAYS);
    }

    @FXML
    private void settingHBoxMouseClicked(MouseEvent mouseEvent) {
        if (ObjectUtils.isEmpty(settingVBox)) {
            SettingController settingController = new SettingController();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(Properties.MAIN_SETTING_FXML_URL);
            FXMLLoader loader = new FXMLLoader();
            loader.setController(settingController);
            try {
                settingVBox = loader.load(Objects.requireNonNull(is));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mainRightVBox.getChildren().set(1, settingVBox);
        VBox.setVgrow(settingVBox, Priority.ALWAYS);

    }

    @FXML
    private void rootKeyPressed(KeyEvent keyEvent) {
        // 当按下键盘组合键时触发
        KeyCombination ctrlB = KeyCodeCombination.keyCombination("Alt+B");
        if (ctrlB.match(keyEvent)) {
            final Stage stage = new PreferenceController().loadStage(new Stage(), Properties.PREFERENCE_FXML_URL);
            stage.show();
            stage.toFront();
        }
    }

    @Override
    public Stage loadStage(Stage primaryStage, String listFxmlUrl) {
        super.loadingStage(primaryStage, listFxmlUrl, this);
        primaryStage.setOnCloseRequest(event -> this.close(getClass().getName()));
        return primaryStage;
    }

    @Override
    public void exit() {
        super.exit();
    }

    private void bindAction(JFXHamburger burger) {
        burger.setOnMouseClicked((e) -> {
            final Transition burgerAnimation = burger.getAnimation();
            burgerAnimation.setRate(burgerAnimation.getRate() * -1);
            burgerAnimation.play();

            Timeline timeline = new Timeline();
            timeline.setCycleCount(1);
            timeline.setAutoReverse(false);
            KeyValue kv1 = new KeyValue(mainLeftVBox.maxWidthProperty(), mainLeftVBox.getMaxWidth());
            KeyFrame kf1 = new KeyFrame(Duration.ZERO, kv1);
            if (mainLeftVBox.getMaxWidth() == 50) {
                KeyValue kv2 = new KeyValue(mainLeftVBox.maxWidthProperty(), 150);
                KeyFrame kf2 = new KeyFrame(Duration.seconds(0.2), kv2);
                timeline.getKeyFrames().addAll(kf1, kf2);
                timeline.play();
            } else if (mainLeftVBox.getMaxWidth() == 150) {
                KeyValue kv2 = new KeyValue(mainLeftVBox.maxWidthProperty(), 50);
                KeyFrame kf2 = new KeyFrame(Duration.seconds(0.2), kv2);
                timeline.getKeyFrames().addAll(kf1, kf2);
                timeline.play();
            }

        });
    }

    @Override
    protected String stageTitle() {
        return "mavd";
    }

}
