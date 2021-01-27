package cn.monkeyapp.mavd.common;

import cn.monkeyapp.mavd.Main;

/**
 * 通用配置信息类
 *
 * @author Corbett Zhang
 */
public class Properties {

    public static final String USER_HOME = System.getProperty("user.home");
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String USER_DIR = System.getProperty("user.dir");

    /**
     * jar的绝对路径
     * <b>
     * 用于生成临时目录，日志文件等
     * </b>
     */
    public static final String MAVD_LIB = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();

    /**
     * 依赖库路径
     */
    public static final String LIB_KEY = "lib_key";

    /**
     * 下载的视屏、图片、字幕存放路径
     */
    public static final String DATA_KEY = "data_key";
    /**
     * 用户登录信息
     */
    public static final String SESSION_KEY = "session";
    /**
     * 程序配置信息
     */
    public static final String CONFIG_KEY = "settings";
    /**
     * 程序配置信息
     */
    public static final String PREFERENCE_KEY = "preference";

    /**
     * XML文件路径
     */
    public static final String CONFIG_PATH_KEY = "config_path_key";
    public static final String PREFERENCE_PATH_KEY = "preference_path_key";
    public static final String SESSION_PATH_KEY = "session_path_key";
    /**
     * 相片存储目录
     */
    public static final String PHOTO_PATH_KEY = "photo_path_key";
    /**
     * 数据库文件路径
     */
    public static final String MONKEY_TOOLS_DB_KEY = "monkey_tools_db_key";

    /**
     * 系统通知桥接器
     */
    public static final String NS_USER_NOTIFICATIONS_BRIDGE_KEY = "NsUserNotificationsBridge.dylib";

    /**
     * fxml资源路径
     */
    public static final String LOGIN_FXML_URL = "fxml/login.fxml";
    public static final String MAIN_FXML_URL = "fxml/main.fxml";
    public static final String MAIN_LIST_FXML_URL = "fxml/main-list.fxml";
    public static final String MAIN_SETTING_FXML_URL = "fxml/main-setting.fxml";
    public static final String MAIN_SETTING1_FXML_URL = "fxml/main-setting-1.fxml";
    public static final String MAIN_SETTING2_FXML_URL = "fxml/main-setting-2.fxml";
    public static final String MAIN_SETTING3_FXML_URL = "fxml/main-setting-3.fxml";
    public static final String LOADING_FXML_URL = "fxml/loading.fxml";
    public static final String USER_FXML_URL = "fxml/user.fxml";
    public static final String PREFERENCE_FXML_URL = "fxml/preference.fxml";
    public static final String ABOUT_FXML_URL = "fxml/about.fxml";
    public static final String NEW_FXML_URL = "fxml/new.fxml";

}
