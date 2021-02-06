package cn.monkeyapp.mavd.controller;

import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.entity.Config;
import cn.monkeyapp.mavd.service.XmlService;
import cn.monkeyapp.mavd.service.impl.XmlServiceImpl;
import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Corbett Zhang
 */
public class SettingController extends AbstractController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(SettingController.class);
    private static final XmlService xmlService = new XmlServiceImpl();
    private Config config;
    private ResourceBundle resourceBundle;

    //-------------------------------设置页面-------------------------------
    @FXML
    private HBox setting1HBox;
    @FXML
    private HBox setting2HBox;
    @FXML
    private HBox setting3HBox;
    @FXML
    private HBox settingBody1HBox;
    @FXML
    private HBox settingBody2HBox;
    @FXML
    private HBox settingBody3HBox;
    @FXML
    private VBox root;

    @FXML
    private ImageView wordpressSettingImage;
    @FXML
    private ImageView cloudSettingImage;
    @FXML
    private ImageView otherSettingImage;

    private JFXTextField objectHost;
    private JFXTextField accessKey;
    private JFXTextField secretKey;
    private JFXTextField bucket;
    private JFXTextField webSite;
    private JFXTextField url;
    private JFXCheckBox onlyDownload;
    private JFXTextField downloadPath;
    private JFXButton chooseButton;


    @Override
    protected Stage loadStage(Stage primaryStage, String listFxmlUrl) {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.resourceBundle = resources;

        config = (Config) LocalCache.getInstance().get(Properties.CONFIG_KEY);

        loadSetting();

        updateSettingHBox(setting1HBox, setting2HBox, setting3HBox);
        if (root.getChildren().size() == 3) {
            root.getChildren().set(1, settingBody1HBox);
        } else {
            root.getChildren().add(1, settingBody1HBox);
        }
    }

    @FXML
    private void saveButtonAction(ActionEvent actionEvent) {
        // 保存设置信息
        config.setObjectHost(objectHost.getText());
        config.setAccessKey(accessKey.getText());
        config.setSecretKey(secretKey.getText());
        config.setBucket(bucket.getText());
        config.setWebSite(webSite.getText());
        config.setUrl(url.getText());
        config.setDownloadPath(downloadPath.getText());
        config.setOnlyDownload(onlyDownload.isSelected());
        xmlService.saveConfig(config);
        LocalCache.getInstance().add(config, Properties.CONFIG_KEY);
        LOGGER.log(Level.INFO, "更新配置信息成功，" + config.toString());
        showAlert(resourceBundle.getString("SettingAlertInfo"), ((JFXButton) actionEvent.getSource()).getScene().getWindow());
    }

    public static void showAlert(String content, Window window) {
        Platform.runLater(() -> {
            JFXDialogLayout layout = new JFXDialogLayout();
            layout.setBody(new Label(content));
            JFXAlert<Void> alert = new JFXAlert<>(window);
            alert.setOverlayClose(true);
            alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
            alert.setContent(layout);
            alert.initModality(Modality.NONE);
            alert.show();
        });
    }

    public void setting3HBoxMouseClicked(MouseEvent mouseEvent) {
        updateSettingHBox(setting3HBox, setting2HBox, setting1HBox);
        if (root.getChildren().size() == 3) {
            root.getChildren().set(1, settingBody3HBox);
        } else {
            root.getChildren().add(1, settingBody3HBox);
        }
    }

    public void setting2HBoxMouseClicked(MouseEvent mouseEvent) {
        updateSettingHBox(setting2HBox, setting1HBox, setting3HBox);
        if (root.getChildren().size() == 3) {
            root.getChildren().set(1, settingBody2HBox);
        } else {
            root.getChildren().add(1, settingBody2HBox);
        }
    }

    public void setting1HBoxMouseClicked(MouseEvent mouseEvent) {
        updateSettingHBox(setting1HBox, setting2HBox, setting3HBox);
        if (root.getChildren().size() == 3) {
            root.getChildren().set(1, settingBody1HBox);
        } else {
            root.getChildren().add(1, settingBody1HBox);
        }
    }

    private void updateSettingHBox(HBox h1, HBox h2, HBox h3) {
        h1.getStyleClass().remove("setting-background-hover");
        h1.getStyleClass().add("setting-background-hover");
        h2.getStyleClass().remove("setting-background-hover");
        h3.getStyleClass().remove("setting-background-hover");
    }

    private HBox loadSetting(String checkSettingFxmlUrl) {
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(checkSettingFxmlUrl);
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setResources(ResourceBundle.getBundle("mavd", Locale.getDefault()));
            return loader.load(Objects.requireNonNull(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected String stageTitle() {
        return null;
    }

    private void loadSetting() {
        wordpressSettingImage.setImage(new Image("img/wordpress-simple.png"));
        cloudSettingImage.setImage(new Image("img/cloud.png"));
        otherSettingImage.setImage(new Image("img/project-hover-menu.png"));

        settingBody3HBox = loadSetting(Properties.MAIN_SETTING3_FXML_URL);
        settingBody2HBox = loadSetting(Properties.MAIN_SETTING2_FXML_URL);
        settingBody1HBox = loadSetting(Properties.MAIN_SETTING1_FXML_URL);

        webSite = (JFXTextField) settingBody1HBox.lookup("#webSite");
        url = (JFXTextField) settingBody1HBox.lookup("#url");

        objectHost = (JFXTextField) settingBody2HBox.lookup("#objectHost");
        accessKey = (JFXTextField) settingBody2HBox.lookup("#accessKey");
        secretKey = (JFXTextField) settingBody2HBox.lookup("#secretKey");
        bucket = (JFXTextField) settingBody2HBox.lookup("#bucket");

        onlyDownload = (JFXCheckBox) settingBody3HBox.lookup("#onlyDownload");
        downloadPath = (JFXTextField) settingBody3HBox.lookup("#downloadPath");
        chooseButton = (JFXButton) settingBody3HBox.lookup("#chooseButton");

        objectHost.setText(config.getObjectHost());
        accessKey.setText(config.getAccessKey());
        secretKey.setText(config.getSecretKey());
        bucket.setText(config.getBucket());
        webSite.setText(config.getWebSite());
        url.setText(config.getUrl());
        onlyDownload.setSelected(config.getOnlyDownload());
        downloadPath.setText(config.getDownloadPath());
        chooseButton.setOnAction(event -> {
            final DirectoryChooser file = new DirectoryChooser();
            file.setInitialDirectory(new File(System.getProperty("user.home")));
            File file1 = file.showDialog(((JFXButton) event.getSource()).getScene().getWindow());
            if (file1 != null) {
                downloadPath.setText(file1.getAbsolutePath());
            }
        });
    }

}
