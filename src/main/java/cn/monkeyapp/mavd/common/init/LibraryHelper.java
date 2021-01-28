package cn.monkeyapp.mavd.common.init;

import cn.monkeyapp.mavd.Main;
import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.service.SqliteService;
import cn.monkeyapp.mavd.service.XmlService;
import cn.monkeyapp.mavd.service.impl.SqliteServiceImpl;
import cn.monkeyapp.mavd.service.impl.XmlServiceImpl;
import cn.monkeyapp.mavd.util.FileUtils;
import cn.monkeyapp.mavd.util.IOUtils;
import cn.monkeyapp.mavd.util.OsInfoUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.attribute.PosixFilePermission.*;

/**
 * 初始化帮助类
 *
 * @author Corbett Zhang
 */
public class LibraryHelper {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    private static String[] windowsLibs = new String[]{"cwebp.exe", "dwebp.exe", "ffmpeg.exe", "gif2webp.exe", "youtube-dl.exe"};
    private static String[] otherLibs = new String[]{"cwebp", "dwebp", "ffmpeg", "gif2webp", "youtube-dl"};

    /**
     * 初始化基础数据
     */
    public static void initialize() {
        // jar文件的绝对路径
        File file = new File(Properties.MAVD_LIB);
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
        // 将数据库进行中的任务改为失败
        updateTaskStatus();

    }

    private static void updateTaskStatus() {
        final SqliteService sqliteService = new SqliteServiceImpl();
        sqliteService.updateExecute();
    }

    private static void createLibFile(File file) {
        File configFile = new File(file.getParentFile(), "lib");
        if (!configFile.exists()) {
            final boolean mkdirs = configFile.mkdirs();
            LOGGER.log(Level.INFO, String.format("创建目录[lib]，%s", mkdirs));
        }
        if (!verify(configFile.getAbsolutePath(), OsInfoUtils.isWindows() ? windowsLibs : otherLibs)) {
            final File lib = new File(file.getParent(), "lib.zip");
            try {
                if (!lib.exists()) {
                    lib.createNewFile();
                }
                InputStream libZip = ClassLoader.getSystemClassLoader().getResourceAsStream("lib.zip");
                assert libZip != null;
                FileUtils.copy(libZip, lib);
                IOUtils.unzip(lib.getAbsolutePath(), configFile.getAbsolutePath());
                lib.delete();
                if (!OsInfoUtils.isWindows()) {
                    recursiveAuthorization(configFile.getAbsolutePath());
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        LocalCache.getInstance().add(configFile.getAbsolutePath(), Properties.LIB_KEY);
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

    /**
     * 递归读取文件夹下的文件并授予权限
     *
     * @param path
     */
    public static void recursiveAuthorization(String path) {
        File file = new File(path);
        // 如果是文件夹，则需要递归处理
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File currentFile : files) {
                if (currentFile.isDirectory()) {
                    recursiveAuthorization(currentFile.getPath());
                } else {
                    updatePermission(currentFile.getAbsolutePath());
                }
            }
        } else {
            updatePermission(file.getAbsolutePath());
        }
    }

    public static void readPermissions(PosixFileAttributeView posixView) {
        try {
            PosixFileAttributes attribs;
            attribs = posixView.readAttributes();
            Set<PosixFilePermission> permissions = attribs.permissions();
            // 将posix文件权限集转换为rwxrwxrwx形式
            String rwxFormPermissions = PosixFilePermissions.toString(permissions);
            LOGGER.log(Level.INFO, rwxFormPermissions);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void updatePermissions(PosixFileAttributeView posixView) {
        try {
            Set<PosixFilePermission> permissions = EnumSet.of(OWNER_READ, OWNER_WRITE, OWNER_EXECUTE, GROUP_READ, GROUP_WRITE);
            posixView.setPermissions(permissions);
            System.out.println("Permissions set successfully.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void updatePermission(String s) {
        Path path = Paths.get(s);
        PosixFileAttributeView posixView = Files.getFileAttributeView(path, PosixFileAttributeView.class);
        if (posixView == null) {
            System.out.format("POSIX attribute view  is not  supported%n.");
            return;
        }
        readPermissions(posixView);
        updatePermissions(posixView);
        readPermissions(posixView);
    }

    /**
     * 判断文件夹下是否有指定文件
     *
     * @param path  要判断的目录
     * @param files 是否存在的文件列表
     * @return 是否全部存在
     */
    private static boolean verify(String path, String... files) {
        File home = new File(path);
        if (!home.isDirectory()) {
            return false;
        }
        for (String s : files) {
            if (!new File(home, s).exists()) {
                return false;
            }
        }
        return true;
    }

}
