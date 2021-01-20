package cn.monkeyapp.mavd.service.impl;

import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.entity.Config;
import cn.monkeyapp.mavd.entity.Session;
import cn.monkeyapp.mavd.service.LoginService;
import cn.monkeyapp.mavd.util.HttpUtils;
import cn.monkeyapp.mavd.util.StringUtils;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Corbett Zhang
 */
public class LoginServiceImpl implements LoginService {

    private static final Logger LOGGER = LogManager.getLogger(LoginServiceImpl.class);

    private static final String GENERATE_AUTH_COOKIE = "/api/user/generate_auth_cookie/";
    private static final String GET_USER_META = "/api/user/get_user_meta";
    private static final String AVATAR = "/wp-content/uploads";
    private static final String OK = "ok";
    private static final String ERROR = "error";
    private static final String STATUS = "status";

    @Override
    public Session login(String username, String password, String seconds) {

        final Config config = (Config) LocalCache.getInstance().get(Properties.CONFIG_KEY);

        if (config == null || StringUtils.isEmptyOrNull(config.getWebSite())) {
            return null;
        }

        Map<String, String> params = new HashMap<>(3);
        params.put("username", username);
        params.put("password", password);
        params.put("seconds", seconds);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();

        try {
            final String form = HttpUtils.getInstance().getForm(config.getWebSite() + GENERATE_AUTH_COOKIE, params);
            HashMap map = gson.fromJson(form, HashMap.class);
            if (((String) map.get(STATUS)).contains(OK)) {
                return gson.fromJson(form, Session.class);
            } else if (((String) map.get(STATUS)).contains(ERROR)) {
                LOGGER.log(Level.WARNING, (String) map.get(ERROR));
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String getAvatar(String cookie) {

        final Config config = (Config) LocalCache.getInstance().get(Properties.CONFIG_KEY);

        if (config == null || StringUtils.isEmptyOrNull(config.getWebSite())) {
            return null;
        }

        Map<String, String> params = new HashMap<>(3);
        params.put("cookie", cookie);
        params.put("meta_key", "wp__wpcom_metas");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();

        try {
            final String form = HttpUtils.getInstance().getForm(config.getWebSite() + GET_USER_META, params);
            HashMap map = gson.fromJson(form, HashMap.class);
            if (((String) map.get(STATUS)).contains(OK)) {
                final Object object = map.get("wp__wpcom_metas");
                if (object != null && ((ArrayList) object).size() > 0) {
                    return AVATAR + ((Map) ((ArrayList) map.get("wp__wpcom_metas")).get(0)).get("avatar");
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

}
