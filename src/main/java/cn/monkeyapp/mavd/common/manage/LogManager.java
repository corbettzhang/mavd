package cn.monkeyapp.mavd.common.manage;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * JDK自带日志简单封装
 *
 * @author Corbett Zhang
 */
public class LogManager {

    // 初始化LogManager
    static {
        // 读取配置文件
        ClassLoader cl = LogManager.class.getClassLoader();
        InputStream inputStream;
        if (cl != null) {
            inputStream = cl.getResourceAsStream("log.properties");
        } else {
            inputStream = ClassLoader.getSystemResourceAsStream("log.properties");
        }
        java.util.logging.LogManager logManager = java.util.logging.LogManager.getLogManager();
        try {
            // 重新初始化日志属性并重新读取日志配置。
            logManager.readConfiguration(inputStream);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取日志对象
     *
     * @param clazz
     * @return
     */
    public static Logger getLogger(Class clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        return logger;
    }

}
