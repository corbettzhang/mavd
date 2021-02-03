package cn.monkeyapp.mavd.controller;

import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.MySystemTray;
import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.entity.Config;
import cn.monkeyapp.mavd.entity.Session;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 用户信息
 *
 * @author Corbett Zhang
 */
public class UserController extends AbstractController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);
    private ResourceBundle resourceBundle;

    @FXML
    private Pane root;
    @FXML
    private ImageView userImage;
    @FXML
    private Label nickNameLabel;
    @FXML
    private Label emailLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;

        Session session = (Session) LocalCache.getInstance().get(Properties.SESSION_KEY);
        Config config = (Config) LocalCache.getInstance().get(Properties.CONFIG_KEY);
        try {
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
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        nickNameLabel.setText(session.getUser().getNickname());
        emailLabel.setText(session.getUser().getEmail());
    }

    protected Object getValue() {
        return nickNameLabel.getText();
    }

    @Override
    public Stage loadStage(Stage primaryStage, String listFxmlUrl) {
        return loadingStage(primaryStage, listFxmlUrl, this);
    }

    @FXML
    private void logoutLabelMouseClicked(MouseEvent mouseEvent) {
        SwingUtilities.invokeLater(MySystemTray::destroyTray);
        showStage(LoginController.class.getName());
    }

    @Override
    protected String stageTitle() {
        return resourceBundle.getString("MyInformation");
    }
}
