package cn.monkeyapp.mavd.service.impl;

import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.entity.Config;
import cn.monkeyapp.mavd.entity.Preference;
import cn.monkeyapp.mavd.entity.Session;
import cn.monkeyapp.mavd.service.XmlService;
import jakarta.xml.bind.JAXB;

import java.io.File;

/**
 * 读写XMl文件，用于存储项目信息
 *
 * @author Corbett Zhang
 */
public class XmlServiceImpl implements XmlService {

    private static File configFile = new File((String) LocalCache.getInstance().get(Properties.CONFIG_PATH_KEY));
    private static File preferenceFile = new File((String) LocalCache.getInstance().get(Properties.PREFERENCE_PATH_KEY));
    private static File sessionFile = new File((String) LocalCache.getInstance().get(Properties.SESSION_PATH_KEY));

    @Override
    public void saveConfig(Config config) {
        JAXB.marshal(config, configFile);
    }

    @Override
    public Config getConfig() {
        return JAXB.unmarshal(configFile, Config.class);
    }

    @Override
    public void savePreference(Preference preference) {
        JAXB.marshal(preference, preferenceFile);
    }

    @Override
    public Preference getPreference() {
        return JAXB.unmarshal(preferenceFile, Preference.class);
    }

    @Override
    public void saveSession(Session session) {
        JAXB.marshal(session, sessionFile);
    }

    @Override
    public Session getSession() {
        return JAXB.unmarshal(sessionFile, Session.class);
    }
}
