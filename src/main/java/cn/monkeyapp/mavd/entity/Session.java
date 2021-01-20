package cn.monkeyapp.mavd.entity;

import java.util.Date;

/**
 * 用户会话信息
 *
 * @author Corbett Zhang
 */
public class Session {
    /**
     * Session有效时间
     */
    private String seconds;
    private Date loginTime;
    private String cookie;
    private String cookieAdmin;
    private String cookieName;
    private String rememberMe;
    private String image;
    private UserInfo user;

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCookieAdmin() {
        return cookieAdmin;
    }

    public void setCookieAdmin(String cookieAdmin) {
        this.cookieAdmin = cookieAdmin;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(String rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
