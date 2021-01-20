package cn.monkeyapp.mavd.entity;

/**
 * @author Corbett Zhang
 */
public class VideoThumbnail {
    private String url;
    private String id;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "VideoThumbnail{" +
                "url='" + url + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
