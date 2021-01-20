package cn.monkeyapp.mavd.service;

import cn.monkeyapp.mavd.entity.Session;

/**
 * 用户登录接口
 *
 * @author Corbett Zhang
 */
public interface LoginService {

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @param seconds  session失效时间
     * @return
     */
    Session login(String username, String password, String seconds);

    /**
     * 获取用户头像
     *
     * @param cookie cookie
     * @return 用户头像URL
     */
    String getAvatar(String cookie);
}
