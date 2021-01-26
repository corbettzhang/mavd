package cn.monkeyapp.mavd.controller;

import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.common.manage.ThreadPoolManager;
import cn.monkeyapp.mavd.common.sqlite.SqliteHandler;
import cn.monkeyapp.mavd.entity.Content;
import cn.monkeyapp.mavd.entity.StatusEnum;
import cn.monkeyapp.mavd.entity.Task;
import cn.monkeyapp.mavd.exception.MonkeyException;
import cn.monkeyapp.mavd.service.SqliteService;
import cn.monkeyapp.mavd.service.impl.SqliteServiceImpl;
import cn.monkeyapp.mavd.youtubedl.YoutubeDL;
import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.TextFieldEditorBuilder;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Corbett Zhang
 */
public class ListController extends AbstractController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(ListController.class);
    private static final SqliteService sqliteService = new SqliteServiceImpl();

    @FXML
    private VBox root;

    //----------------------------增加、删除、搜索----------------------------
    @FXML
    private JFXButton treeTableViewAdd;
    @FXML
    private JFXButton treeTableViewRemove;
    @FXML
    private JFXTextField searchField;

    //----------------------------任务数量----------------------------
    @FXML
    private Label totalLabel;
    @FXML
    private Label activeLabel;
    @FXML
    private Label completedLabel;

    //----------------------------treeTableView列表----------------------------
    @FXML
    private JFXTreeTableView<TaskProperty> editableTreeTableView;
    @FXML
    private JFXTreeTableColumn<TaskProperty, Integer> idColumn;
    @FXML
    private JFXTreeTableColumn<TaskProperty, String> urlColumn;
    @FXML
    private JFXTreeTableColumn<TaskProperty, String> titleColumn;
    @FXML
    private JFXTreeTableColumn<TaskProperty, String> tagColumn;
    @FXML
    private JFXTreeTableColumn<TaskProperty, String> typeColumn;
    @FXML
    private JFXTreeTableColumn<TaskProperty, String> descriptionColumn;
    @FXML
    private JFXTreeTableColumn<TaskProperty, String> statusColumn;

    @FXML
    private void rootMouseDragged(javafx.scene.input.MouseEvent mouseEvent) {
        mouseDragged(mouseEvent);
    }

    @FXML
    private void rootMousePressed(javafx.scene.input.MouseEvent mouseEvent) {
        mousePressed(mouseEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupEditableTableView();
        flashTaskNum();
    }

    private void flashTaskNum() {
        final Map<String, Integer> map = sqliteService.queryTaskStatus();
        activeLabel.setText(String.valueOf(map.get("active")));
        completedLabel.setText(String.valueOf(map.get("completed")));
    }

    private <T> void setupCellValueFactory(JFXTreeTableColumn<TaskProperty, T> column, Function<TaskProperty, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<TaskProperty, T> param) -> {
            if (column.validateValue(param)) {
                return mapper.apply(param.getValue().getValue());
            } else {
                return column.getComputedValue(param);
            }
        });
    }

    private void setupEditableTableView() {
        ObservableList<TaskProperty> data = FXCollections.observableArrayList();
        final List<Task> list = sqliteService.getTaskList();
        list.forEach(task -> {
            data.add(new TaskProperty(task));
        });

        setupCellValueFactory(urlColumn, TaskProperty::getUrl);
        setupCellValueFactory(titleColumn, TaskProperty::getTitle);
        setupCellValueFactory(tagColumn, TaskProperty::getTag);
        setupCellValueFactory(descriptionColumn, TaskProperty::getDescription);
        setupCellValueFactory(typeColumn, TaskProperty::getType);
        setupCellValueFactory(idColumn, p -> p.id.asObject());
        setupCellValueFactory(statusColumn, TaskProperty::getStatus);

        // add editors
        urlColumn.setCellFactory((TreeTableColumn<TaskProperty, String> param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
        urlColumn.setOnEditCommit((TreeTableColumn.CellEditEvent<TaskProperty, String> t) -> {
            final Task task = new Task();
            task.setId(t.getRowValue().getValue().id.getValue());
            task.setUrl(t.getNewValue());
            if (updateTaskInfo(task, "url")) {
                t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().url.set(t.getNewValue());
            }
        });

        titleColumn.setCellFactory((TreeTableColumn<TaskProperty, String> param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
        titleColumn.setOnEditCommit((TreeTableColumn.CellEditEvent<TaskProperty, String> t) -> {
            final Task task = new Task();
            task.setId(t.getRowValue().getValue().id.getValue());
            task.setTitle(t.getNewValue());
            if (updateTaskInfo(task, "title")) {
                t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().title.set(t.getNewValue());
            }
        });

        tagColumn.setCellFactory((TreeTableColumn<TaskProperty, String> param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
        tagColumn.setOnEditCommit((TreeTableColumn.CellEditEvent<TaskProperty, String> t) -> {
            final Task task = new Task();
            task.setId(t.getRowValue().getValue().id.getValue());
            task.setTag(t.getNewValue());
            if (updateTaskInfo(task, "tag")) {
                t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().tag.set(t.getNewValue());
            }
        });

        descriptionColumn.setCellFactory((TreeTableColumn<TaskProperty, String> param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
        descriptionColumn.setOnEditCommit((TreeTableColumn.CellEditEvent<TaskProperty, String> t) -> {
            final Task task = new Task();
            task.setId(t.getRowValue().getValue().id.getValue());
            task.setDescription(t.getNewValue());
            if (updateTaskInfo(task, "description")) {
                t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().description.set(t.getNewValue());
            }
        });

        typeColumn.setCellFactory((TreeTableColumn<TaskProperty, String> param) -> new GenericEditableTreeTableCell<>(new TextFieldEditorBuilder()));
        typeColumn.setOnEditCommit((TreeTableColumn.CellEditEvent<TaskProperty, String> t) -> {
            final Task task = new Task();
            task.setId(t.getRowValue().getValue().id.getValue());
            task.setType(t.getNewValue());
            if (updateTaskInfo(task, "type")) {
                t.getTreeTableView().getTreeItem(t.getTreeTablePosition().getRow()).getValue().type.set(t.getNewValue());
            }
        });

        treeTableViewAdd.setOnMouseClicked((e) -> {
            final Task task = new Task("", "", "", "", "");
            if (sqliteService.save(task)) {
                final int id = sqliteService.queryLastId();
                task.setId(id);
                task.setStatus(0);
                data.add(new TaskProperty(task));
                final IntegerProperty currCountProp = editableTreeTableView.currentItemsCountProperty();
                currCountProp.set(currCountProp.get() + 1);
            }
        });

        treeTableViewRemove.setOnMouseClicked((e) -> {
            if (editableTreeTableView.getSelectionModel().getFocusedIndex() > -1) {
                if (sqliteService.deleteById(editableTreeTableView.getSelectionModel().selectedItemProperty().get().getValue().id.getValue())) {
                    data.remove(editableTreeTableView.getSelectionModel().selectedItemProperty().get().getValue());
                    final IntegerProperty currCountProp = editableTreeTableView.currentItemsCountProperty();
                    currCountProp.set(currCountProp.get() - 1);
                }
            }
        });

        editableTreeTableView.setRoot(new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren));
        editableTreeTableView.setShowRoot(false);
        editableTreeTableView.setEditable(true);

        editableTreeTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (editableTreeTableView.getSelectionModel().getSelectedItem() != null) {

                    Tooltip.uninstall(editableTreeTableView, editableTreeTableView.getTooltip());

                    final TaskProperty taskProperty = editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
                    if (tooltipMap.get(taskProperty.getUrl().getValue()) != null) {
                        final Tooltip tooltip = tooltipMap.get(taskProperty.getUrl().getValue());
                        Tooltip.install(editableTreeTableView, tooltip);
                    } else {
                        javafx.concurrent.Task<Content> progressTask = new javafx.concurrent.Task<Content>() {
                            @Override
                            protected Content call() throws MonkeyException {
                                return (Content) YoutubeDL.getVideoInfo(taskProperty.getUrl().getValue());
                            }
                        };
                        progressTask.setOnSucceeded(e -> {
                            VBox pane = new VBox();
                            final ImageView imageView = new ImageView();
                            try {
                                imageView.setImage(new Image(progressTask.get().getThumbnail()));
                                imageView.setFitWidth(130);
                                imageView.setFitHeight(100);
                                pane.getChildren().addAll(imageView, new Label(progressTask.get().getTitle()));
                                final Tooltip tooltip = new Tooltip();
                                tooltip.setGraphic(pane);
                                Tooltip.install(editableTreeTableView, tooltip);
                                tooltipMap.put(taskProperty.getUrl().getValue(), tooltip);
                            } catch (InterruptedException | ExecutionException ex) {
                                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                            }
                        });
                        progressTask.setOnFailed((e -> LOGGER.log(Level.SEVERE, e.getSource().getMessage(), e)));
                        ThreadPoolManager.getInstance().addThreadExecutor(progressTask);
                    }
                }
            }
        });

        totalLabel.textProperty()
                .bind(Bindings.createStringBinding(() -> String.valueOf(editableTreeTableView.getCurrentItemsCount()),
                        editableTreeTableView.currentItemsCountProperty()));
        searchField.textProperty()
                .addListener(setupSearchField(editableTreeTableView));
    }

    private static final Map<String, Tooltip> tooltipMap = new ConcurrentHashMap<>();

    private boolean updateTaskInfo(Task task, String field) {
        try {
            return sqliteService.update(SqliteHandler.appendSql(task, field));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return false;
    }

    private ChangeListener<String> setupSearchField(final JFXTreeTableView<TaskProperty> tableView) {
        return (o, oldVal, newVal) ->
                tableView.setPredicate(TaskProp -> {
                    final TaskProperty Task = TaskProp.getValue();
                    return Task.url.get().contains(newVal)
                            || Task.title.get().contains(newVal)
                            || Task.type.get().contains(newVal)
                            || Task.tag.get().contains(newVal)
                            || Task.description.get().contains(newVal)
                            || Task.status.get().contains(newVal)
                            || Integer.toString(Task.id.get()).contains(newVal);
                });
    }

    @FXML
    private void treeTableViewContextMenuRequested(ContextMenuEvent contextMenuEvent) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem enter = new MenuItem("执行");
        MenuItem detail = new MenuItem("详情");
        MenuItem refresh = new MenuItem("刷新");

        refresh.setOnAction(event -> {
            setupEditableTableView();
            flashTaskNum();
        });
        enter.setOnAction(event -> {
            if (editableTreeTableView.getSelectionModel().getSelectedItem() != null) {
                final int id = editableTreeTableView.getSelectionModel().getSelectedItem().getValue().id.get();
                final Task task = new Task();
                task.setId(id);
                task.setStatus(StatusEnum.WAITING_ENUM);
                try {
                    boolean bool = sqliteService.update(SqliteHandler.appendSql(task, "status"));
                    if (bool) {
                        setupEditableTableView();
                        flashTaskNum();
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        });
        detail.setOnAction(event -> {
            if (editableTreeTableView.getSelectionModel().getSelectedItem() != null) {
                final int id = editableTreeTableView.getSelectionModel().getSelectedItem().getValue().id.get();
                final Object loading = LocalCache.getInstance().get("loading", String.valueOf(id));
                try {
                    ((Stage) loading).show();
                } catch (NullPointerException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        });

        if (editableTreeTableView.getSelectionModel().getSelectedItem() != null) {
            final TaskProperty taskProperty = editableTreeTableView.getSelectionModel().getSelectedItem().getValue();
            if (StatusEnum.getCode(taskProperty.getStatus().getValue()) == StatusEnum.INITIAL_ENUM
                    || StatusEnum.getCode(taskProperty.getStatus().getValue()) == StatusEnum.FAILED_ENUM) {
                contextMenu.getItems().add(enter);
            } else if (StatusEnum.getCode(taskProperty.getStatus().getValue()) == StatusEnum.ACTIVE_ENUM) {
                contextMenu.getItems().add(detail);
            }
        }
        contextMenu.getItems().add(refresh);
        contextMenu.show(editableTreeTableView.getScene().getWindow(), contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
    }

    static class TaskProperty extends RecursiveTreeObject<TaskProperty> {

        SimpleIntegerProperty id;
        SimpleStringProperty url;
        SimpleStringProperty title;
        SimpleStringProperty tag;
        SimpleStringProperty type;
        SimpleStringProperty description;
        SimpleStringProperty status;

        public TaskProperty(Task task) {
            this.id = new SimpleIntegerProperty(task.getId());
            this.url = new SimpleStringProperty(task.getUrl());
            this.title = new SimpleStringProperty(task.getTitle());
            this.tag = new SimpleStringProperty(task.getTag());
            this.type = new SimpleStringProperty(task.getType());
            this.description = new SimpleStringProperty(task.getDescription());
            this.status = new SimpleStringProperty(StatusEnum.getImportance(task.getStatus()));
        }

        public SimpleIntegerProperty getId() {
            return id;
        }

        public SimpleStringProperty getUrl() {
            return url;
        }

        public SimpleStringProperty getTitle() {
            return title;
        }

        public SimpleStringProperty getTag() {
            return tag;
        }

        public SimpleStringProperty getType() {
            return type;
        }

        public SimpleStringProperty getDescription() {
            return description;
        }

        public SimpleStringProperty getStatus() {
            return status;
        }
    }

    @Override
    public Stage loadStage(Stage stage, String listFxmlUrl) {
        if (hasStage(getClass().getName())) {
            return getStage(getClass().getName());
        }
        return super.loadingStage(stage, listFxmlUrl, this);
    }

    @Override
    protected String stageTitle() {
        return null;
    }

}
