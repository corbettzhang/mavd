package cn.monkeyapp.mavd.common;

import cn.monkeyapp.mavd.common.manage.ImageConverter;
import cn.monkeyapp.mavd.common.manage.LogManager;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 回调函数的默认实现
 *
 * @author zhangcong
 */
public class DefaultProgressCallback implements ProgressCallback {

    private static final Logger LOGGER = LogManager.getLogger(DefaultProgressCallback.class);

    @Override
    public void onProgressUpdate(float progress, long etaInSeconds) {
        LOGGER.log(Level.INFO, String.format("剩余时间：%s s 已下载 %s", etaInSeconds, progress + " %"));
    }

    @Override
    public void onProgressUpdate(String line) {
        LOGGER.log(Level.INFO, line);
    }
}
