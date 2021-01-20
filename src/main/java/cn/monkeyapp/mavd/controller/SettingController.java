package cn.monkeyapp.mavd.controller;

import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.common.stage.MonkeyAppAlert;
import cn.monkeyapp.mavd.entity.Config;
import cn.monkeyapp.mavd.service.XmlService;
import cn.monkeyapp.mavd.service.impl.XmlServiceImpl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

    private JFXTextField objectHost;
    private JFXTextField accessKey;
    private JFXTextField secretKey;
    private JFXTextField bucket;
    private JFXTextField webSite;
    private JFXTextField url;
    private JFXCheckBox onlyDownload;
    private JFXTextField downloadPath;
    private JFXButton chooseButton;


    public SettingController() {
    }

    @Override
    protected Stage loadStage(Stage primaryStage, String listFxmlUrl) {
        return null;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        config = (Config) LocalCache.getInstance().get(Properties.CONFIG_KEY);

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

        setting1HBox.setStyle("-fx-background-color: bisque");
        setting2HBox.setStyle("-fx-background-color: #FFFFFF");
        setting3HBox.setStyle("-fx-background-color: #FFFFFF");
        if (root.getChildren().size() == 3) {
            root.getChildren().set(1, settingBody1HBox);
        } else {
            root.getChildren().add(1, settingBody1HBox);
        }
    }

    // 保存设置信息
    @FXML
    private void saveButtonAction(ActionEvent actionEvent) {
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
        MonkeyAppAlert.showAlert("Successful. ", ((JFXButton) actionEvent.getSource()).getScene().getWindow());
    }

    public void setting3HBoxMouseClicked(MouseEvent mouseEvent) {
        setting3HBox.setStyle("-fx-background-color: bisque");
        setting2HBox.setStyle("-fx-background-color: #FFFFFF");
        setting1HBox.setStyle("-fx-background-color: #FFFFFF");
        if (root.getChildren().size() == 3) {
            root.getChildren().set(1, settingBody3HBox);
        } else {
            root.getChildren().add(1, settingBody3HBox);
        }
    }

    public void setting2HBoxMouseClicked(MouseEvent mouseEvent) {
        setting2HBox.setStyle("-fx-background-color: bisque");
        setting3HBox.setStyle("-fx-background-color: #FFFFFF");
        setting1HBox.setStyle("-fx-background-color: #FFFFFF");
        if (root.getChildren().size() == 3) {
            root.getChildren().set(1, settingBody2HBox);
        } else {
            root.getChildren().add(1, settingBody2HBox);
        }
    }

    public void setting1HBoxMouseClicked(MouseEvent mouseEvent) {
        setting1HBox.setStyle("-fx-background-color: bisque");
        setting2HBox.setStyle("-fx-background-color: #FFFFFF");
        setting3HBox.setStyle("-fx-background-color: #FFFFFF");
        if (root.getChildren().size() == 3) {
            root.getChildren().set(1, settingBody1HBox);
        } else {
            root.getChildren().add(1, settingBody1HBox);
        }
    }

    private HBox loadSetting(String checkSettingFxmlUrl) {
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(checkSettingFxmlUrl);
        try {
            return new FXMLLoader().load(Objects.requireNonNull(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected String stageTitle() {
        return "list";
    }

}
