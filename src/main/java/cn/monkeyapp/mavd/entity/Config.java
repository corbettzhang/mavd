package cn.monkeyapp.mavd.entity;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * 项目配置文件
 *
 * @author Corbett Zhang
 */
@XmlRootElement
public class Config {

    /**
     * 七牛云相关设置
     */
    private String objectHost;
    private String accessKey;
    private String secretKey;
    private String bucket;

    /**
     * wordpress配置
     * 站点的文章路径
     * 站点网址
     */
    private String webSite;
    private String url;

    /**
     * 其他配置
     * 只下载
     * 下载路径
     */
    private boolean onlyDownload;
    private String downloadPath;


    public Config() {
    }

    public Config(String objectHost, String accessKey, String secretKey, String bucket, String webSite, String url, Boolean onlyDownload, String downloadPath) {
        this.objectHost = objectHost;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucket = bucket;
        this.webSite = webSite;
        this.url = url;
        this.onlyDownload = onlyDownload;
        this.downloadPath = downloadPath;
    }

    public String getObjectHost() {
        return objectHost;
    }

    public void setObjectHost(String objectHost) {
        this.objectHost = objectHost;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getOnlyDownload() {
        return onlyDownload;
    }

    public void setOnlyDownload(boolean onlyDownload) {
        this.onlyDownload = onlyDownload;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    @Override
    public String toString() {
        return "Config{" +
                "objectHost='" + objectHost + '\'' +
                ", accessKey='" + accessKey + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", bucket='" + bucket + '\'' +
                ", webSite='" + webSite + '\'' +
                ", url='" + url + '\'' +
                ", onlyDownload=" + onlyDownload +
                ", downloadPath='" + downloadPath + '\'' +
                '}';
    }
}