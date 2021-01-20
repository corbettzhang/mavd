package cn.monkeyapp.mavd.common.init;

import cn.monkeyapp.mavd.Main;
import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.common.manage.ThreadPoolManager;
import cn.monkeyapp.mavd.service.XmlService;
import cn.monkeyapp.mavd.service.impl.XmlServiceImpl;
import cn.monkeyapp.mavd.util.FileUtils;
import cn.monkeyapp.mavd.util.IOUtils;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 初始化以及依赖库下载
 *
 * @author Corbett Zhang
 */
public class LibraryHelper {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    /**
     * 下载依赖完成后该值为true，在执行下载任务时，如果该值为false则下载任务无法进行
     *
     * @see DownloadHelper#doDownload
     */
    public static volatile boolean isDownloaded = false;

    /**
     * 初始化基础数据
     */
    public static void initialize() {
        // jar文件的绝对路径
        File file = new File(Properties.USER_LIB);
        // Copy Jar内的Config.xml文件到Jar外，便于存取配置信息
        createXmlFile(file);
        // 创建lib文件
        createLibFile(file);
        // 创建photo目录到jar外，用于存储用户图片
        createPhotoFile(file);
        // 创建Sqlite数据库文件
        createDbFile();
        // 创建视频文件存储目录
        createVideoInfo();
        // 如果不存在插件则去下载插件
//        downloadLibrary();

    }

    private static void createLibFile(File file) {
        File configFile = new File(file.getParentFile(), "lib");
        if (!configFile.exists()) {
            final boolean mkdirs = configFile.mkdirs();
            LOGGER.log(Level.INFO, String.format("创建目录[lib]，%s", mkdirs));
        }
        LocalCache.getInstance().add(configFile.getAbsolutePath(), Properties.LIB_KEY);

        final File lib = new File(file.getParent(), "lib.zip");
        if (!lib.exists()) {
            try {
                lib.createNewFile();
                InputStream libZip = ClassLoader.getSystemClassLoader().getResourceAsStream("lib.zip");
                assert libZip != null;
                FileUtils.copy(libZip, lib);
                IOUtils.unzip(lib.getAbsolutePath(), configFile.getAbsolutePath());
                lib.delete();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    private static void createVideoInfo() {
        File videoInfo = new File(Properties.USER_HOME, "video_info");
        if (!videoInfo.exists()) {
            videoInfo.mkdir();
        }
        LocalCache.getInstance().add(videoInfo.getAbsolutePath(), Properties.DATA_KEY);
    }

    private static void createXmlFile(File file) {
        File configFile = new File(file.getParentFile(), "config");
        if (!configFile.exists()) {
            final boolean mkdirs = configFile.mkdirs();
            LOGGER.log(Level.INFO, String.format("创建目录[config]，%s", mkdirs));
        }
        File config = new File(configFile.getAbsoluteFile(), "Config.xml");
        if (!config.exists()) {
            try {
                boolean newFile = config.createNewFile();
                LOGGER.log(Level.INFO, String.format("创建文件[Config.xml]，%s", newFile));
                final InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("data/Config.xml");
                FileUtils.copy(Objects.requireNonNull(stream, "加载jar内[data/Config.xml]文件出错！"), config);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        LocalCache.getInstance().add(config.getAbsolutePath(), Properties.CONFIG_PATH_KEY);
        LOGGER.log(Level.INFO, String.format("完成创建[Config.xml]文件，%s", config.getAbsolutePath()));

        File preference = new File(configFile.getAbsoluteFile(), "Preference.xml");
        if (!preference.exists()) {
            try {
                boolean newFile = preference.createNewFile();
                LOGGER.log(Level.INFO, String.format("创建文件[Preference.xml]，%s", newFile));
                final InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("data/Preference.xml");
                FileUtils.copy(Objects.requireNonNull(stream, "加载jar内[data/Preference.xml]文件出错！"), preference);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        LocalCache.getInstance().add(preference.getAbsolutePath(), Properties.PREFERENCE_PATH_KEY);
        LOGGER.log(Level.INFO, String.format("完成创建[Preference.xml]文件，%s", preference.getAbsolutePath()));

        File session = new File(configFile.getAbsoluteFile(), "Session.xml");
        if (!session.exists()) {
            try {
                boolean newFile = session.createNewFile();
                LOGGER.log(Level.INFO, String.format("创建文件[Session.xml]，%s", newFile));
                final InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("data/Session.xml");
                FileUtils.copy(Objects.requireNonNull(stream, "加载jar内[data/Session.xml]文件出错！"), session);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        LocalCache.getInstance().add(session.getAbsolutePath(), Properties.SESSION_PATH_KEY);
        LOGGER.log(Level.INFO, String.format("完成创建[Session.xml]文件，%s", session.getAbsolutePath()));

        XmlService xmlService = new XmlServiceImpl();
        LocalCache.getInstance().add(xmlService.getConfig(), Properties.CONFIG_KEY);
        LocalCache.getInstance().add(xmlService.getPreference(), Properties.PREFERENCE_KEY);
        LOGGER.log(Level.INFO, "完成缓存Xml配置信息");
    }

    private static void createPhotoFile(File file) {
        File photoPath = new File(file.getParentFile(), "photo");
        if (!photoPath.exists()) {
            final boolean mkdirs = photoPath.mkdirs();
            LOGGER.log(Level.INFO, String.format("创建目录[photo]，%s", mkdirs));
        }
        LocalCache.getInstance().add(photoPath.getAbsolutePath(), Properties.PHOTO_PATH_KEY);
        LOGGER.log(Level.INFO, String.format("完成创建[photo]目录，%s", photoPath.getAbsolutePath()));
    }

    private static void createDbFile() {
        File database = new File(Properties.USER_DIR + File.separator + ".database");
        if (!database.exists()) {
            final boolean mkdirs = database.mkdirs();
            LOGGER.log(Level.INFO, "生成目录[database]" + mkdirs);
        }
        File dbFile = new File(database.getAbsolutePath(), "monkey-tools.db");
        if (!dbFile.exists()) {
            final boolean success;
            try {
                success = dbFile.createNewFile();
                LOGGER.log(Level.INFO, "生成文件[monkey-tools.db]" + success);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LocalCache.getInstance().add(dbFile.getAbsolutePath(), Properties.MONKEY_TOOLS_DB_KEY);
        LOGGER.log(Level.INFO, String.format("完成创建[monkey-tools.db]数据库文件，%s", dbFile.getAbsolutePath()));
    }

    private static void downloadLibrary() {
        Task<Boolean> progressTask = new Task<Boolean>() {
            @Override
            protected Boolean call() {
                return DownloadHelper.doDownload();
            }
        };
        progressTask.setOnSucceeded(event -> {
            try {
                isDownloaded = progressTask.get();
            } catch (InterruptedException | ExecutionException e) {
                isDownloaded = false;
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        });
        progressTask.setOnFailed((e -> LOGGER.log(Level.SEVERE, e.getSource().getMessage(), e)));
        ThreadPoolManager.getInstance().addThreadExecutor(progressTask);
    }
}
