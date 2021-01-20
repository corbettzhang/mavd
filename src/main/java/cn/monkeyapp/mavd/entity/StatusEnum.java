package cn.monkeyapp.mavd.entity;

/**
 * @author Corbett Zhang
 */
public enum StatusEnum {

    INITIAL(0, "未开始"),

    WAITING(1, "等待中"),

    ACTIVE(2, "进行中"),

    COMPLETED(3, "已完成"),

    FAILED(4, "失败");

    public static final int INITIAL_ENUM = 0;
    public static final int WAITING_ENUM = 1;
    public static final int ACTIVE_ENUM = 2;
    public static final int COMPLETED_ENUM = 3;
    public static final int FAILED_ENUM = 4;

    private Integer code;
    private String intro;

    StatusEnum(int code, String intro) {
        this.code = code;
        this.intro = intro;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public static String getImportance(Integer code) {
        StatusEnum[] levelEnums = values();
        for (StatusEnum enums : levelEnums) {
            if (enums.getCode().equals(code)) {
                return enums.getIntro();
            }
        }
        return null;
    }

    public static Integer getCode(String intro) {
        StatusEnum[] levelEnums = values();
        for (StatusEnum enums : levelEnums) {
            if (enums.getIntro().equals(intro)) {
                return enums.getCode();
            }
        }
        return -1;
    }
}
