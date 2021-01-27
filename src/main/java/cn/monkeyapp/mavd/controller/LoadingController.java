package cn.monkeyapp.mavd.controller;

import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.ProgressCallback;
import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.common.manage.ExecuteHelper;
import cn.monkeyapp.mavd.common.manage.ImageConverter;
import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.common.manage.ThreadPoolManager;
import cn.monkeyapp.mavd.common.sqlite.SqliteHandler;
import cn.monkeyapp.mavd.entity.*;
import cn.monkeyapp.mavd.exception.MonkeyException;
import cn.monkeyapp.mavd.service.NotificationService;
import cn.monkeyapp.mavd.service.SqliteService;
import cn.monkeyapp.mavd.service.impl.NotificationServiceImpl;
import cn.monkeyapp.mavd.service.impl.SqliteServiceImpl;
import cn.monkeyapp.mavd.util.*;
import cn.monkeyapp.mavd.youtubedl.YoutubeDL;
import cn.monkeyapp.mavd.youtubedl.YoutubeDLRequest;
import cn.monkeyapp.mavd.youtubedl.YoutubeDLResponse;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXTextArea;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * 下载，上传实现逻辑
 *
 * @author Corbett Zhang
 */
public class LoadingController extends AbstractController implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(LoadingController.class);
    private static final NotificationService notificationService = new NotificationServiceImpl();
    private static final SqliteService sqliteService = new SqliteServiceImpl();

    @FXML
    private ImageView img1;
    @FXML
    private ImageView img2;
    @FXML
    private ImageView img3;
    @FXML
    private ImageView img4;
    @FXML
    private ImageView img5;
    @FXML
    private Text text1;
    @FXML
    private Text text2;
    @FXML
    private Text text3;
    @FXML
    private Text text4;
    @FXML
    private Text text5;
    @FXML
    private Label lab1;
    @FXML
    private Label lab2;
    @FXML
    private Label lab3;
    @FXML
    private Label lab4;

    private RotateTransition rotateTransition1, rotateTransition2, rotateTransition3, rotateTransition4, rotateTransition5;

    @FXML
    private Label labSuccess;
    @FXML
    private Button startButton;
    @FXML
    private Button closeButton;

    //--------------------------显示明细部分--------------------------
    @FXML
    private Button detailButton;
    @FXML
    private Pane detailPane;
    @FXML
    private JFXTextArea detailTextArea;

    //--------------------------显示明细部分--------------------------
    @FXML
    private Pane rootPane;
    @FXML
    private Pane mainPane;

    private final Content content;
    private final Config settings;
    private final Session session;
    private String openBrowserUrl;

    @FXML
    private void anchorPaneMouseDragged(MouseEvent mouseEvent) {
        mouseDragged(mouseEvent);
    }

    @FXML
    private void anchorPaneMousePressed(MouseEvent mouseEvent) {
        mousePressed(mouseEvent);
    }

    public LoadingController(Content content) {
        this.content = content;
        this.settings = (Config) LocalCache.getInstance().get(Properties.CONFIG_KEY);
        this.session = (Session) LocalCache.getInstance().get(Properties.SESSION_KEY);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void closeButtonClicked(ActionEvent actionEvent) {
        ((Stage) closeButton.getScene().getRoot().getScene().getWindow()).close();
    }

    @FXML
    private void detailButtonClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        if (detailPane.isVisible()) {
            detailPane.setVisible(false);
            stage.setWidth(mainPane.getPrefWidth());
            stage.setHeight(mainPane.getPrefHeight());
        } else {
            detailPane.setVisible(true);
            stage.setWidth(mainPane.getPrefWidth() + detailPane.getPrefWidth());
            stage.setHeight(mainPane.getPrefHeight());
        }
    }

    @FXML
    private void startButtonClicked(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        if ("OPEN".equals(button.getText())) {
            OpenBrowserUtils.openUrl(openBrowserUrl);
        } else {

            rotateTransition1 = new RotateTransition(Duration.seconds(2), img1);
            rotateTransition2 = new RotateTransition(Duration.seconds(2), img2);
            rotateTransition3 = new RotateTransition(Duration.seconds(2), img3);
            rotateTransition4 = new RotateTransition(Duration.seconds(2), img4);
            rotateTransition5 = new RotateTransition(Duration.seconds(2), img5);
            RotateTransition[] transition = {rotateTransition1, rotateTransition2, rotateTransition3, rotateTransition4, rotateTransition5};
            for (RotateTransition rotateTransition : transition) {
                rotateTransition.setCycleCount(Timeline.INDEFINITE);
                // true表示正转一次反转一次，false表示循环
                rotateTransition.setAutoReverse(false);
                rotateTransition.setFromAngle(360);
                rotateTransition.setToAngle(0);
            }

            Platform.runLater(() -> {
                // 设置按钮不可用
                startButton.setDisable(true);
                try {
                    doDownload();
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                } finally {
                    startButton.setDisable(true);
                }
            });
        }
    }

    public void flow(RotateTransition v1, ImageView v2, Label v3, ImageView v5, Text v6, RotateTransition v7, String text) {
        v1.stop();
        v2.setImage(new Image("img/loading-check-all.png"));
        v3.setStyle("-fx-background-color: #45A55563");
        v5.setImage(new Image("img/loading-sign.png"));
        v6.setText(text);
        v6.setVisible(true);
        v7.play();
    }

    public void error(RotateTransition v1, ImageView v2, Text v3, Throwable exception) {
        v1.stop();
        v2.setImage(new Image("img/loading-error.png"));
        v3.setText(exception.getMessage());
    }

    private void doDownload() {
        img1.setImage(new Image("img/loading-sign.png"));
        rotateTransition1.play();
        text1.setText("正在获取视频信息");
        setMessage("正在获取视频信息");
        text1.setVisible(true);

        Task<Boolean> getVideoInfoTask = getVideoInfoTask();
        Task<Boolean> downloadVideoTask = downloadVideoTask();
        Task<Boolean> mergeVideoTask = mergeVideoTask();
        Task<Boolean> uploadToQiNiuCloudTask = uploadToQiNiuCloudTask();
        Task<String> uploadToWordPressTask = uploadToWordPressTask();

        getVideoInfoTask.setOnSucceeded(e -> {
            rotateTransition1.jumpTo(Duration.ZERO);
            flow(rotateTransition1, img1, lab1, img2, text2, rotateTransition2, "正在下载视频文件");
            ThreadPoolManager.getInstance().addThreadExecutor(downloadVideoTask);
        });
        getVideoInfoTask.messageProperty().addListener((observable, oldValue, newValue) -> setMessage(newValue));
        getVideoInfoTask.setOnFailed(e -> {
            exceptionHandler(e.getSource().getException());
            rotateTransition1.jumpTo(Duration.ZERO);
            error(rotateTransition1, img1, text1, e.getSource().getException());
        });

        downloadVideoTask.setOnSucceeded(e -> {
            // 转换视频格式
            if (!content.getIsMp4()) {
                convertToMp4(content);
            }
            rotateTransition2.jumpTo(Duration.ZERO);
            if (settings.getOnlyDownload()) {
                success(rotateTransition2, img2);
            } else {
                flow(rotateTransition2, img2, lab2, img3, text3, rotateTransition3, "正在合并视频和字幕");
                ThreadPoolManager.getInstance().addThreadExecutor(mergeVideoTask);
            }
        });
        downloadVideoTask.messageProperty().addListener((observable, oldValue, newValue) -> setMessage(newValue));
        downloadVideoTask.setOnFailed(e -> {
            exceptionHandler(e.getSource().getException());
            rotateTransition2.jumpTo(Duration.ZERO);
            error(rotateTransition2, img2, text2, e.getSource().getException());
        });

        mergeVideoTask.setOnSucceeded(e -> {
            rotateTransition3.jumpTo(Duration.ZERO);
            flow(rotateTransition3, img3, lab3, img4, text4, rotateTransition4, "正在上传视频文件到七牛云");
            ThreadPoolManager.getInstance().addThreadExecutor(uploadToQiNiuCloudTask);
        });
        mergeVideoTask.messageProperty().addListener((observable, oldValue, newValue) -> setMessage(newValue));
        mergeVideoTask.setOnFailed(e -> {
            exceptionHandler(e.getSource().getException());
            rotateTransition3.jumpTo(Duration.ZERO);
            error(rotateTransition3, img3, text3, e.getSource().getException());
        });

        uploadToQiNiuCloudTask.setOnSucceeded(event -> {
            rotateTransition4.jumpTo(Duration.ZERO);
            flow(rotateTransition4, img4, lab4, img5, text5, rotateTransition5, "正在发布文章中");
            ThreadPoolManager.getInstance().addThreadExecutor(uploadToWordPressTask);
        });
        uploadToQiNiuCloudTask.messageProperty().addListener((observable, oldValue, newValue) -> setMessage(newValue));
        uploadToQiNiuCloudTask.setOnFailed(e -> {
            exceptionHandler(e.getSource().getException());
            rotateTransition4.jumpTo(Duration.ZERO);
            error(rotateTransition4, img4, text4, e.getSource().getException());
        });

        uploadToWordPressTask.setOnSucceeded(e -> {
            openBrowserUrl = String.format(settings.getUrl(), uploadToWordPressTask.getValue());
            setMessage(openBrowserUrl);
            success(rotateTransition5, img5);
            labSuccess.setText("下载完成. ");
            labSuccess.setVisible(true);
            startButton.setText("OPEN");
            startButton.setDisable(false);
            notificationService.sendNotification("已处理完成", "",
                    String.format("已完成任务【%s】，访问地址为：%s", content.getTaskId(), openBrowserUrl), 0);
        });
        uploadToWordPressTask.messageProperty().addListener((observable, oldValue, newValue) -> setMessage(newValue));
        uploadToWordPressTask.setOnFailed(e -> {
            exceptionHandler(e.getSource().getException());
            rotateTransition5.jumpTo(Duration.ZERO);
            error(rotateTransition5, img5, text5, e.getSource().getException());
        });

        ThreadPoolManager.getInstance().addThreadExecutor(getVideoInfoTask);

    }

    private void success(RotateTransition var1, ImageView var2) {
        var1.jumpTo(Duration.ZERO);
        var1.stop();
        var2.setImage(new Image("img/loading-check-all.png"));
        updateStatus(StatusEnum.COMPLETED_ENUM);
    }

    private void exceptionHandler(Throwable e) {
        LOGGER.log(Level.INFO, "LoadingController.exceptionHandler------------------------》" + content.toString());
        LOGGER.log(Level.INFO, "修改状态为失败，task：" + content.getTaskId());
        LOGGER.log(Level.SEVERE, e.getMessage(), e);
        removeStage(getClass().getName());
        updateStatus(StatusEnum.FAILED_ENUM);
    }

    private void updateStatus(int status) {
        cn.monkeyapp.mavd.entity.Task task = new cn.monkeyapp.mavd.entity.Task();
        task.setId(content.getTaskId());
        task.setStatus(status);
        try {
            sqliteService.update(SqliteHandler.appendSql(task, "status"));
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private Task<String> uploadToWordPressTask() {
        return new Task<String>() {
            @Override
            public String call() throws Exception {
                String port = uploadToWordPress(content);
                updateMessage("已上传至Wordpress，请点击OPEN打开查看！");
                return port;
            }
        };
    }

    private Task<Boolean> getVideoInfoTask() {
        return new Task<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final Content videoInfo = (Content) YoutubeDL.getVideoInfo(content.getYoutubeUrl());
                updateMessage(videoInfo.getVideoName());
                ObjectUtils.mergeObject(videoInfo, content);
                // 下载视频格式优先级
                videoFormatPriority(content);
                return true;
            }
        };
    }

    /**
     * 默认下载mp4格式，如果网站不支持mp4格式则下载其他格式
     *
     * @param content 数据处理上下文
     */
    private void videoFormatPriority(Content content) {

        if ("mp4".equals(content.getExt())) {
            content.setIsMp4(true);
            content.setDefaultVideoFormat(content.getExt());
            return;
        }

        Map<String, Integer> map = new ConcurrentHashMap<>();

        content.getFormats().forEach(format -> {
            if (map.containsKey(format.getExt())) {
                map.put(format.getExt(), map.get(format.getExt()) + 1);
            } else {
                map.put(format.getExt(), 1);
            }
        });

        if (map.containsKey("mp4")) {
            content.setIsMp4(true);
            content.setDefaultVideoFormat("mp4");
            return;
        }

        // 按照value值，从大到小排序
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((o1, o2) -> o2.getValue() - o1.getValue());

        content.setIsMp4(false);
        content.setDefaultVideoFormat(list.get(0).getKey());
    }

    private Task<Boolean> downloadVideoTask() {
        return new Task<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                // 没有开启添加字幕则无需下载字幕
                if (content.getHasSubtitle() > 0) {
                    YoutubeDLResponse videoSubtitle = null;
                    for (int i = 0; i < 5; i++) {
                        videoSubtitle = YoutubeDL.getVideoSubtitle(content.getYoutubeUrl(), content.getId());
                        if (StringUtils.isEmptyOrNull(videoSubtitle.getErr())) {
                            break;
                        }
                    }

                    if (StringUtils.isNotEmptyOrNull(videoSubtitle.getOut())) {
                        final String[] strings = videoSubtitle.getOut().split("\n");
                        final String[] strings1 = strings[strings.length - 1].split(" ");
                        final boolean contains = strings1[strings1.length - 1].contains(LocalCache.getInstance().get(Properties.DATA_KEY) + content.getId() + File.separator);
                        content.setSubtitlePath_CN(contains ? strings1[strings1.length - 1] : null);
                    }
                    if (StringUtils.isNotEmptyOrNull(videoSubtitle.getErr())) {
                        updateMessage(videoSubtitle.getErr());
                    }
                }

                downloadVideo(new ProgressCallback() {
                    @Override
                    public void onProgressUpdate(float progress, long etaInSeconds) {
                        updateMessage(String.format("剩余时间：%s s 已下载 %s", etaInSeconds, progress + " %"));
                    }

                    @Override
                    public void onProgressUpdate(String line) {
                    }
                });
                return true;
            }
        };
    }

    private Task<Boolean> mergeVideoTask() {
        return new Task<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                // 如果说没有找到字幕就不需要合并
                if (content.getHasSubtitle() == 2) {  //合并双字幕
                    // TODO 合并双字幕
                } else if (content.getHasSubtitle() == 1) {  // 合并中文字幕
                    if (StringUtils.isNotEmptyOrNull(content.getSubtitlePath_CN())) {
                        execute(new ProgressCallback() {
                            @Override
                            public void onProgressUpdate(float progress, long etaInSeconds) {
                                updateMessage(String.format("剩余时间：%s s 已下载 %s", etaInSeconds, progress + " %"));
                            }

                            @Override
                            public void onProgressUpdate(String line) {
                            }
                        }, content.getSubtitlePath_CN());
                    }
                }
                return true;
            }
        };
    }

    private Task<Boolean> uploadToQiNiuCloudTask() {
        return new Task<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                // 构造一个带指定 Region 对象的配置类
                Configuration cfg = new Configuration(Region.autoRegion());
                Auth auth = Auth.create(settings.getAccessKey(), settings.getSecretKey());
                String upToken = auth.uploadToken(settings.getBucket());

                String localTempDir = Paths.get(System.getProperty("java.io.tmpdir"), settings.getBucket()).toString();

                updateMessage("正在上传缩略图 . . .");
                doUploadQiNiuCloud(cfg, upToken, localTempDir, content.getImagePath1(), content.getImageName1());

                updateMessage("正在上传视频 . . .");
                if (content.getHasSubtitle() > 0 && StringUtils.isNotEmptyOrNull(content.getSubtitlePath_CN())) {
                    doUploadQiNiuCloud(cfg, upToken, localTempDir, content.getVideoPath1(), content.getVideoName1());
                } else {
                    doUploadQiNiuCloud(cfg, upToken, localTempDir, content.getVideoPath(), content.getVideoName());
                }

                return true;
            }
        };
    }

    private void downloadVideo(ProgressCallback callback) throws Exception {

        final Preference preference = (Preference) LocalCache.getInstance().get(Properties.PREFERENCE_KEY);

        String path = LocalCache.getInstance().get(Properties.DATA_KEY) + File.separator + content.getId() + File.separator;
        //构造请求体
        YoutubeDLRequest request = new YoutubeDLRequest(content.getYoutubeUrl());
        // 遇到下载错误时，跳过
        request.setOption("ignore-errors");
        // 输出名称为文件ID+后缀
        request.setOption("output", path + "%(id)s.%(ext)s");

        request.setOption("format", content.getDefaultVideoFormat());
        // 重试
        request.setOption("retries", 10);

        if (preference.getIsProxy() == 1) {
            // 设置代理
            request.setOption("proxy", preference.getProxy().getUrl());
        }

        //把缩略图写入硬盘
        request.setOption("write-thumbnail");

        YoutubeDLResponse response = YoutubeDL.execute(request, callback);

        Set<String> resource = new HashSet<>();
        final String[] out = response.getOut().split("\n");
        for (String str : out) {
            if (str.contains(path)) {
                final String[] temp = str.split(" ");
                resource.add(Arrays.stream(temp).filter(e -> e.contains(path)).collect(Collectors.joining()));
            }
        }

        resource.forEach(s -> {
            if (s.contains(content.getDefaultVideoFormat())) {
                content.setVideoPath(s);
                content.setVideoName(s.substring(s.lastIndexOf(File.separator) + 1));
            } else {
                content.setImagePath(s);
                content.setImageName(s.substring(s.lastIndexOf(File.separator) + 1));
                // 设置远程发布方法的参数
                ImageConverter.create().toNormalImage(new File(s), new File(path + content.getId() + ".jpg"));
                content.setImagePath1(path + content.getId() + ".jpg");
                content.setImageName1(content.getId() + ".jpg");
            }
        });
    }

    /**
     * ffmpeg -i "Spider-Man.webm" -c copy "Spider-Man.mp4"
     * 转换格式为MP4
     *
     * @param content 下载上下文
     */
    private void convertToMp4(Content content) {
        content.setVideoName(content.getVideoName().substring(0, content.getVideoName().indexOf('.') + 1) + "mp4"); // 合并后的视频文件名称
        final String s = LocalCache.getInstance().get(Properties.DATA_KEY) + StringUtils.stringToPath(content.getId()) + content.getVideoName();
        // content.getVideoPath()  输入文件地址
        // -c copy 指示ffmpeg在转换过程中不要调整视频质量
        String command = LocalCache.getInstance().get(Properties.LIB_KEY) + File.separator + "ffmpeg " + "-i " + content.getVideoPath() + "  -c copy " + s;
        content.setVideoPath(s);
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * 执行ffmpeg请求，合并字幕
     *
     * @param callback 回调
     * @throws IOException IO异常
     */
    public void execute(ProgressCallback callback, String subtitle) throws IOException {
        content.setVideoName1(content.getVideoName() + "sub"); // 合并后的视频文件名称
        content.setVideoPath1(LocalCache.getInstance().get(Properties.DATA_KEY) + StringUtils.stringToPath(content.getId()) + content.getVideoName1());
        ExecuteHelper executeHelper = new ExecuteHelper();
        String command =
                LocalCache.getInstance().get(Properties.LIB_KEY) + File.separator +
                        "ffmpeg " +
                        "-i " +    //输入文件地址
                        content.getVideoPath() +
                        " -vf" +    //设置音频解码器
                        " subtitles=" + subtitle +
                        " -y " +   //覆盖输出文件无需提问
                        content.getVideoPath1();

        executeHelper.exec(command, callback);
    }


    private void setMessage(String message) {
        detailTextArea.insertText(0, message + "\n");
        LOGGER.log(Level.INFO, message);
    }

    /**
     * 发布到wordpress
     *
     * @param resource 资源
     * @throws Exception
     */
    private String uploadToWordPress(Content resource) throws Exception {
        // 设置到服务器的XML-RPC连接
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(settings.getWebSite() + "/xmlrpc.php"));
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);
        // 上传文件，获取postId
        int postId = doUploadMediaObject(client, resource);
        return doUploadNewPost(client, resource, postId);
    }

    /**
     * https://codex.wordpress.org/XML-RPC_MetaWeblog_API xml-RPC-metaWeblog介绍
     * <p>
     * 传媒体类型文件到服务器上
     *
     * @param client  客户端
     * @param content 文件名称
     * @return 文件ID
     * @throws Exception
     */
    private int doUploadMediaObject(XmlRpcClient client, Content content) throws Exception {
        if (session == null) {
            throw new MonkeyException("用户未登录");
        }
        Map<String, Object> post = new HashMap<>(3);
        post.put("name", content.getImageName1());
        post.put("type", "image/jpg");
        post.put("bits", FileUtils.fileConvertToByteArray(new File(content.getImagePath1())));
        final String[] decode = AesUtil.decode(session.getUser().getIdentify());
        Object[] params = new Object[]{0, decode[0], decode[1], post};

        //远程方法调用
        Map result = (Map) client.execute("metaWeblog.newMediaObject", params);
        if (result.containsKey("id")) {
            return Integer.parseInt((String) result.get("id"));
        }
        return 0;
    }

    /**
     * 上传文章
     *
     * @param client   客户端
     * @param resource 视频信息
     * @return
     * @throws XmlRpcException
     */
    private String doUploadNewPost(XmlRpcClient client, Content resource, int postId) throws XmlRpcException {
        Map<String, Object> post = new HashMap<>(4);
        //标题
        post.put("title", content.getArticleTitle());
        //标签
        post.put("mt_keywords", content.getArticleTag());
        // 分类
        post.put("categories", content.getArticleType().split(","));
        // 缩略图id
        post.put("wp_post_thumbnail", postId);
        //内容
        post.put("description", "<blockquote class=\"wp-block-quote\"><p>" + resource.getArticleDescription() + "</p></blockquote>" +
                "<figure class=\"wp-block-video\"><video controls poster=\"" + settings.getObjectHost() + "/" + resource.getImageName1() + "\" src=\"" + settings.getObjectHost() + "/" + resource.getVideoName() + "\"></video></figure>");
        LOGGER.log(Level.INFO, post.toString());
        final String[] decode = AesUtil.decode(session.getUser().getIdentify());
        Object[] params = new Object[]{1, decode[0], decode[1], post, Boolean.TRUE};

        return (String) client.execute("metaWeblog.newPost", params);
    }

    /**
     * 上传文件到七牛云
     *
     * @param cfg          构造一个带指定 Region 对象的配置类
     * @param key          默认不指定key的情况下，以文件内容的hash值作为文件名
     * @param upToken      token
     * @param localTempDir 本地缓存路径
     * @param filePath     文件路径
     * @throws IOException IOException
     */
    private String doUploadQiNiuCloud(Configuration cfg, String upToken, String localTempDir, String filePath, String key) throws Exception {
        //设置断点续传文件进度保存目录
        FileRecorder fileRecorder = new FileRecorder(localTempDir);
        UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
        try {
            Response response = uploadManager.put(filePath, key, upToken);
            DefaultPutRet defaultPutRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return defaultPutRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            try {
                LOGGER.log(Level.SEVERE, r.bodyString(), ex);
            } catch (QiniuException ex2) {
                throw new MonkeyException(ex2.getMessage());
            }
        }
        return null;
    }

    @Override
    public Stage loadStage(Stage stage, String listFxmlUrl) {
        return super.loadingStage(stage, listFxmlUrl, this, this.getClass().getName() + content.getTaskId());
    }


    @Override
    protected String stageTitle() {
        return null;
    }
}