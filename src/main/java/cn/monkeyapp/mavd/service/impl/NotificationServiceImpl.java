package cn.monkeyapp.mavd.service.impl;

import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.common.MySystemTray;
import cn.monkeyapp.mavd.entity.Preference;
import cn.monkeyapp.mavd.service.NotificationService;
import cn.monkeyapp.mavd.util.OsInfoUtils;
import com.sun.jna.Library;
import com.sun.jna.Native;

import java.awt.TrayIcon.MessageType;

/**
 * 发送通知栏消息
 * <a href="https://github.com/petesh/OSxNotificationCenter">Mac OS</a>
 *
 * @author Corbett Zhang
 */
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendNotification(String title, String subtitle, String text, int timeoffset) {
        final Preference preference = (Preference) LocalCache.getInstance().get(Properties.PREFERENCE_KEY);
        if (preference.isEnableNotification()) {
            if (OsInfoUtils.isWindows()) {
                MySystemTray.sendNotification(title, text, MessageType.INFO);
            }
            if (OsInfoUtils.isMacOS0()) {
                NsUserNotificationsBridge.INSTANCE.sendNotification(title, subtitle, text, timeoffset);
            }
        }
    }

    interface NsUserNotificationsBridge extends Library {
        NsUserNotificationsBridge INSTANCE = Native.loadLibrary((String) LocalCache.getInstance().get(Properties.NS_USER_NOTIFICATIONS_BRIDGE_KEY), NsUserNotificationsBridge.class);

        /**
         * MacOS发送消息通知
         *
         * @param title
         * @param subtitle
         * @param text
         * @param timeoffset
         * @return
         */
        int sendNotification(String title, String subtitle, String text, int timeoffset);
    }
}


