package cn.monkeyapp.mavd.entity;

/**
 * 数据处理对象
 *
 * @author Corbett Zhang
 */
public class Content extends VideoInfo {

    private String imagePath;
    private String imageName;
    /**
     * 转换为jpg格式后的地址和名称
     */
    private String imagePath1;
    private String imageName1;

    private String videoPath;
    private String videoName;
    /**
     * 合并视频和字幕后的地址
     */
    private String videoPath1;
    private String videoName1;

    /**
     * 默认以下载mp4格式视频为主，如果目标网站不支持mp4，则下载其他格式
     */
    private Boolean isMp4;
    private String defaultVideoFormat;

    /**
     * 视频信息
     */
    private String subtitlePath_CN;
    private String subtitlePath_EN;
    private String youtubeUrl;
    private String articleTitle;
    private String articleType;
    private String articleTag;
    private String articleDescription;
    private int hasSubtitle;

    /**
     * 任务ID，用于在完成任务后或者失败后改变任务状态
     */
    private Integer taskId;

    public int getHasSubtitle() {
        return hasSubtitle;
    }

    public void setHasSubtitle(int hasSubtitle) {
        this.hasSubtitle = hasSubtitle;
    }

    public String getVideoPath1() {
        return videoPath1;
    }

    public void setVideoPath1(String videoPath1) {
        this.videoPath1 = videoPath1;
    }

    public String getVideoName1() {
        return videoName1;
    }

    public void setVideoName1(String videoName1) {
        this.videoName1 = videoName1;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImagePath1() {
        return imagePath1;
    }

    public void setImagePath1(String imagePath1) {
        this.imagePath1 = imagePath1;
    }

    public String getImageName1() {
        return imageName1;
    }

    public void setImageName1(String imageName1) {
        this.imageName1 = imageName1;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getSubtitlePath_CN() {
        return subtitlePath_CN;
    }

    public void setSubtitlePath_CN(String subtitlePath_CN) {
        this.subtitlePath_CN = subtitlePath_CN;
    }

    public String getSubtitlePath_EN() {
        return subtitlePath_EN;
    }

    public void setSubtitlePath_EN(String subtitlePath_EN) {
        this.subtitlePath_EN = subtitlePath_EN;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public String getArticleTag() {
        return articleTag;
    }

    public void setArticleTag(String articleTag) {
        this.articleTag = articleTag;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Boolean getIsMp4() {
        return isMp4;
    }

    public void setIsMp4(Boolean isMp4) {
        this.isMp4 = isMp4;
    }

    public String getDefaultVideoFormat() {
        return defaultVideoFormat;
    }

    public void setDefaultVideoFormat(String defaultVideoFormat) {
        this.defaultVideoFormat = defaultVideoFormat;
    }
}
