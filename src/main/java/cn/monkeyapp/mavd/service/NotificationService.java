package cn.monkeyapp.mavd.service;

/**
 * 操作系统通知中心接口
 *
 * @author Corbett Zhang
 */
public interface NotificationService {

    /**
     * 通知
     *
     * @param title      标题
     * @param subtitle   子标题
     * @param text       文本
     * @param timeOffset 延迟显示时间偏移量，秒为单位
     * @return 发送是否成功
     */
    void sendNotification(String title, String subtitle, String text, int timeOffset);

}
