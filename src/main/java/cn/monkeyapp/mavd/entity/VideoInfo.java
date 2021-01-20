package cn.monkeyapp.mavd.entity;

import java.util.ArrayList;

/**
 * 视频信息
 * @author Corbett Zhang
 */
public class VideoInfo{

    private String id;
    private String fulltitle;
    private String title;
    private String uploadDate;
    private String displayId;
    private Double duration;
    private String description;
    private String thumbnail;
    private String license;

    private String uploaderId;
    private String uploader;

    private String playerUrl;
    private String webpageUrl;
    private String webpageUrlBasename;

    private String resolution;
    private Double width;
    private Double height;
    private String format;
    private String ext;

    private HttpHeader httpHeader;
    private ArrayList<String> categories;
    private ArrayList<String> tags;
    private ArrayList<VideoFormat> formats;
    private ArrayList<VideoThumbnail> thumbnails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFulltitle() {
        return fulltitle;
    }

    public void setFulltitle(String fulltitle) {
        this.fulltitle = fulltitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getPlayerUrl() {
        return playerUrl;
    }

    public void setPlayerUrl(String playerUrl) {
        this.playerUrl = playerUrl;
    }

    public String getWebpageUrl() {
        return webpageUrl;
    }

    public void setWebpageUrl(String webpageUrl) {
        this.webpageUrl = webpageUrl;
    }

    public String getWebpageUrlBasename() {
        return webpageUrlBasename;
    }

    public void setWebpageUrlBasename(String webpageUrlBasename) {
        this.webpageUrlBasename = webpageUrlBasename;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public HttpHeader getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(HttpHeader httpHeader) {
        this.httpHeader = httpHeader;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<VideoFormat> getFormats() {
        return formats;
    }

    public void setFormats(ArrayList<VideoFormat> formats) {
        this.formats = formats;
    }

    public ArrayList<VideoThumbnail> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(ArrayList<VideoThumbnail> thumbnails) {
        this.thumbnails = thumbnails;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "id='" + id + '\'' +
                ", fulltitle='" + fulltitle + '\'' +
                ", title='" + title + '\'' +
                ", uploadDate='" + uploadDate + '\'' +
                ", displayId='" + displayId + '\'' +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", license='" + license + '\'' +
                ", uploaderId='" + uploaderId + '\'' +
                ", uploader='" + uploader + '\'' +
                ", playerUrl='" + playerUrl + '\'' +
                ", webpageUrl='" + webpageUrl + '\'' +
                ", webpageUrlBasename='" + webpageUrlBasename + '\'' +
                ", resolution='" + resolution + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", format='" + format + '\'' +
                ", ext='" + ext + '\'' +
                ", httpHeader=" + httpHeader +
                ", categories=" + categories +
                ", tags=" + tags +
                ", formats=" + formats +
                ", thumbnails=" + thumbnails +
                '}';
    }
}
