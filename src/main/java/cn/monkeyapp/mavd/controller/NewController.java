package cn.monkeyapp.mavd.controller;

import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.entity.StatusEnum;
import cn.monkeyapp.mavd.entity.Task;
import cn.monkeyapp.mavd.service.SqliteService;
import cn.monkeyapp.mavd.service.impl.SqliteServiceImpl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * 新增任务Controller
 *
 * @author zhangcong
 */
public class NewController extends AbstractController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(NewController.class);
    private static final SqliteService sqliteService = new SqliteServiceImpl();

    @FXML
    private JFXTextField urlTextField;
    @FXML
    private JFXTextField titleTextField;
    @FXML
    private JFXTextField tagTextField;
    @FXML
    private JFXTextField typeTextField;
    @FXML
    private JFXTextField descriptionTextField;
    @FXML
    private JFXButton createButton;

    @Override
    public Stage loadStage(Stage primaryStage, String listFxmlUrl) {
        final Stage stage = loadingStage(primaryStage, listFxmlUrl, this);
        primaryStage.setOnCloseRequest(event -> {
            this.close(getClass().getName());
            this.removeStage(getClass().getName());
        });
        return stage;
    }

    @Override
    protected String stageTitle() {
        return "新建任务";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void createButtonOnAction(ActionEvent actionEvent) {
        Task task = new Task();
        task.setUrl(urlTextField.getText());
        task.setTitle(titleTextField.getText());
        task.setTag(tagTextField.getText());
        task.setDescription(descriptionTextField.getText());
        task.setType(typeTextField.getText());
        task.setStatus(StatusEnum.INITIAL_ENUM);
        if (sqliteService.save(task)) {
            urlTextField.setText("");
            titleTextField.setText("");
            tagTextField.setText("");
            descriptionTextField.setText("");
            typeTextField.setText("");
        }
    }
}
