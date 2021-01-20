package cn.monkeyapp.mavd.entity;

/**
 * 任务实体
 *
 * @author Corbett Zhang
 */
public class Task {
    private int id;
    private String url;
    private String title;
    private String tag;
    private String type;
    private String description;
    private Integer status;
    private Integer hasSubTitle;

    public Task() {
    }

    public Task(String url, String title, String tag, String type, String description) {
        this.url = url;
        this.title = title;
        this.tag = tag;
        this.type = type;
        this.description = description;
    }

    public Task(String url, String title, String tag, String type, String description, Integer status, Integer hasSubTitle) {
        this.url = url;
        this.title = title;
        this.tag = tag;
        this.type = type;
        this.description = description;
        this.status = status;
        this.hasSubTitle = hasSubTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getHasSubTitle() {
        return hasSubTitle;
    }

    public void setHasSubTitle(Integer hasSubTitle) {
        this.hasSubTitle = hasSubTitle;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", tag='" + tag + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", hasSubTitle=" + hasSubTitle +
                '}';
    }
}
