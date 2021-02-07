package cn.monkeyapp.mavd.common;

import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.common.manage.StageHelper;
import cn.monkeyapp.mavd.entity.Preference;
import com.steadystate.css.dom.CSSRuleListImpl;
import com.steadystate.css.parser.CSSOMParser;
import javafx.scene.Scene;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 系统样式修改
 *
 * @author zhangcong
 */
public class StyleManager {
    private static final Logger LOGGER = LogManager.getLogger(StyleManager.class);

    /**
     * stylesheet_dark.css路径
     */
    public static final URL DARK_CSS_URL = ClassLoader.getSystemClassLoader().getResource("css/stylesheet_dark.css");
    /**
     * stylesheet_light.css路径
     */
    public static final URL LIGHT_CSS_URL = ClassLoader.getSystemClassLoader().getResource("css/stylesheet_light.css");
    /**
     * stylesheet.css路径
     */
    public static final URL BASE_CSS_URL = ClassLoader.getSystemClassLoader().getResource("css/stylesheet.css");
    /**
     * 当前使用的样式
     */
    public static URL CURRENT_CSS_URL;

    /**
     * 样式文件具体CSS规则
     */
    private static CSSRuleListImpl CURRENT_CSS_RULE_LIST;

    static {
        try {
            updateCurrentStyle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析css文件
     */
    private static void build() {
        InputStream stream = null;
        try {
            stream = new FileInputStream(CURRENT_CSS_URL.getFile());
            InputSource source = new InputSource(new InputStreamReader(stream));
            CSSOMParser parser = new CSSOMParser();
            CSSStyleSheet stylesheet = parser.parseStyleSheet(source, null, null);
            CURRENT_CSS_RULE_LIST = (CSSRuleListImpl) stylesheet.getCssRules();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    /**
     * 获取样式class具体的值
     *
     * @param styleClass 样式class
     * @return 具体的样式值
     */
    private static String get(String styleClass) {
        final Optional<CSSRule> rule = CURRENT_CSS_RULE_LIST.getRules().stream()
                .filter(cssRule -> ((CSSStyleRule) cssRule).getSelectorText().contains(styleClass)).findFirst();
        return rule.map(cssRule -> ((CSSStyleRule) cssRule).getStyle().getCssText()).orElse(null);
    }

    /**
     * 更新当前样式
     */
    public static void updateCurrentStyle() {
        final Preference preference = (Preference) LocalCache.getInstance().get(Properties.PREFERENCE_KEY);
        final Integer style = preference.getStyle();
        CURRENT_CSS_URL = style == 1 ? DARK_CSS_URL : LIGHT_CSS_URL;
        build();
    }

    /**
     * 更新所有stage样式
     *
     * @param url 样式的URL
     */
    public static void updateStyle(URL url) {
        StageHelper.getAllStage().forEach(stage -> {
            stage.getScene().getStylesheets().remove(url == DARK_CSS_URL
                    ? LIGHT_CSS_URL.toExternalForm() : DARK_CSS_URL.toExternalForm());
            stage.getScene().getStylesheets().add(url.toExternalForm());
        });
        CURRENT_CSS_URL = url;
    }

    /**
     * 为scene加载样式
     *
     * @param scene scene
     */
    public static void loadStageStyle(Scene scene) {
        // Load Common Styles
        scene.getStylesheets().add(BASE_CSS_URL.toExternalForm());
        // Load the bright style
        scene.getStylesheets().add(CURRENT_CSS_URL.toExternalForm());
    }

    /**
     * 获取样式文本
     */
    public static String getStageStyle(Scene scene, String styleClass) {
        return get(scene.getStylesheets().get(1));
    }

}
