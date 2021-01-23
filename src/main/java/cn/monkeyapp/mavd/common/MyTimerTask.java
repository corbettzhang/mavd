package cn.monkeyapp.mavd.common;

import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.common.sqlite.SqliteHandler;
import cn.monkeyapp.mavd.controller.LoadingController;
import cn.monkeyapp.mavd.entity.Content;
import cn.monkeyapp.mavd.entity.StatusEnum;
import cn.monkeyapp.mavd.entity.Task;
import cn.monkeyapp.mavd.service.SqliteService;
import cn.monkeyapp.mavd.service.impl.SqliteServiceImpl;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 功能：每隔一段时间去数据库查看是否有处于<p>等待中</p>{@link StatusEnum#WAITING}的任务；
 * 查询状态为<p>活跃中</p>{@link StatusEnum#ACTIVE}任务的数量，如果数量超过设定的阈值{@link #threshold}，则不处理
 * 反之将状态为<p>等待中</p>的任务设置为<p>活跃中</p>，并开始执行任务逻辑
 *
 * @author Corbett Zhang
 */
public class MyTimerTask extends TimerTask {
    private static final Logger LOGGER = LogManager.getLogger(MyTimerTask.class);

    /**
     * 阈值，如果进行中任务数量超过设定的阈值，则不处理
     */
    public static volatile Integer threshold = 5;

    private static final SqliteService sqliteService = new SqliteServiceImpl();

    @Override
    public void run() {
        LOGGER.log(Level.INFO, String.format("线程[%s]开始执行任务，当前时间[%s]", Thread.currentThread().getName(), new Date()));
        // 1、查询正在执行的任务
        final int active = sqliteService.queryTaskThreshold(StatusEnum.ACTIVE_ENUM);
        // 2、比较阈值
        if (active > -1 && active < threshold) {
            // 3、未超过则加入到处理任务中
            startTask(sqliteService.queryTaskPage(threshold - active));
        }
    }

    private void startTask(List<Task> tasks) {
        tasks.forEach(task -> {
            LOGGER.log(Level.INFO, String.format("线程[%s]开始处理任务，任务------>> %s", Thread.currentThread().getName(), task.toString()));
            Content content = getContent(task);
            Platform.runLater(() -> {
                final LoadingController loadingController = new LoadingController(content);
                final Stage stage = loadingController.loadStage(new Stage(), Properties.LOADING_FXML_URL);
                stage.setResizable(false);
                stage.hide();
                // 获取fxml中id为startButton的控件
                Button button = (Button) stage.getScene().lookup("#startButton");
                button.fire();
                /*
                 * 把stage加到缓存中，方便{@link cn.monkeyapp.mavd.controller.ListController}中使用，查看下载详情信息
                 */
                LocalCache.getInstance().add(stage, "loading", String.valueOf(task.getId()));
                updateStatus(task.getId());
            });
        });
    }

    private Content getContent(Task task) {
        Content content = new Content();
        content.setTaskId(task.getId());
        content.setArticleTitle(task.getTitle());
        content.setArticleTag(task.getTag());
        content.setArticleType(task.getType());
        content.setArticleDescription(task.getDescription());
        content.setYoutubeUrl(task.getUrl());
        content.setHasSubtitle(task.getHasSubTitle());
        return content;
    }

    /**
     * 修改任务状态为进行中
     *
     * @param id task任务id
     */
    private void updateStatus(Integer id) {
        Task task = new Task();
        task.setId(id);
        task.setStatus(StatusEnum.ACTIVE_ENUM);
        try {
            sqliteService.update(SqliteHandler.appendSql(task, "status"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
