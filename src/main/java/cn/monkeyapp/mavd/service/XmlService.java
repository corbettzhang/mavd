package cn.monkeyapp.mavd.service;

import cn.monkeyapp.mavd.entity.Config;
import cn.monkeyapp.mavd.entity.Preference;
import cn.monkeyapp.mavd.entity.Session;

/**
 * @author Corbett Zhang
 */
public interface XmlService {

    void saveConfig(Config config);

    Config getConfig();

    void savePreference(Preference preference);

    Preference getPreference();

    void saveSession(Session session);

    Session getSession();
}
