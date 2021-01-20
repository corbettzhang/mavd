package cn.monkeyapp.mavd.entity;

/**
 * @author Corbett Zhang
 */
public enum ProxyTypeEnum {

    HTTP(0, "http"),
    SOCKET(1, "socket");

    public static final int HTTP_ENUM = 0;
    public static final int SOCKET_ENUM = 1;

    private Integer code;
    private String intro;

    ProxyTypeEnum(int code, String intro) {
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
        ProxyTypeEnum[] levelEnums = values();
        for (ProxyTypeEnum enums : levelEnums) {
            if (enums.getCode().equals(code)) {
                return enums.getIntro();
            }
        }
        return null;
    }

    public static Integer getCode(String intro) {
        ProxyTypeEnum[] levelEnums = values();
        for (ProxyTypeEnum enums : levelEnums) {
            if (enums.getIntro().equals(intro)) {
                return enums.getCode();
            }
        }
        return -1;
    }
}
