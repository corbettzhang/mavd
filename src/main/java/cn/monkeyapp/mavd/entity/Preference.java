package cn.monkeyapp.mavd.entity;

/**
 * 偏好设置实体类
 *
 * @author Corbett Zhang
 */
public class Preference {

    /**
     * 样式，1：暗黑模式 2：明亮模式 3：自动
     */
    private Integer style = 1;

    /**
     * 是否开启代理 0：不开启  1：开启
     */
    private Integer isProxy = 0;

    /**
     * 代理地址
     */
    private Proxy proxy;

    /**
     * 自动更新
     */
    private Integer acceptUpdate;

    /**
     * 启用系统通知栏
     */
    private Boolean enableNotification = true;

    /**
     * 启用系统快捷键
     */
    private Boolean enableShortcut = true;


    public Integer getStyle() {
        return style;
    }

    public void setStyle(Integer style) {
        this.style = style;
    }

    public Integer getIsProxy() {
        return isProxy;
    }

    public void setIsProxy(Integer isProxy) {
        this.isProxy = isProxy;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public Integer getAcceptUpdate() {
        return acceptUpdate;
    }

    public void setAcceptUpdate(Integer acceptUpdate) {
        this.acceptUpdate = acceptUpdate;
    }

    public boolean isEnableNotification() {
        return enableNotification;
    }

    public void setEnableNotification(boolean enableNotification) {
        this.enableNotification = enableNotification;
    }

    public boolean isEnableShortcut() {
        return enableShortcut;
    }

    public void setEnableShortcut(boolean enableShortcut) {
        this.enableShortcut = enableShortcut;
    }

    public static class Proxy {
        /**
         * 代理类型，http,socket
         */
        private Integer type;
        private String hostname;
        private int port;

        public Proxy() {
        }

        public Proxy(Integer type, String hostname, int port) {
            this.type = type;
            this.hostname = hostname;
            this.port = port;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getHostname() {
            return hostname;
        }

        public void setHostname(String hostname) {
            this.hostname = hostname;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getUrl() {
            return ProxyTypeEnum.getImportance(type) + "://" + hostname + ":" + port;
        }
    }
}
