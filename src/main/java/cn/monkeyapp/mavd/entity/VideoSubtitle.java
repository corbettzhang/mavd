package cn.monkeyapp.mavd.entity;

import java.util.List;

/**
 * 影片字幕
 *
 * @author Corbett Zhang
 */
public class VideoSubtitle {
//    Language formats
//    gu       vtt, ttml, srv3, srv2, srv1
//    zh-Hans  vtt, ttml, srv3, srv2, srv1
//    zh-Hant  vtt, ttml, srv3, srv2, srv1

    private String Language;
    private List<String> formats;

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String Language) {
        this.Language = Language;
    }

    public List<String> getFormats() {
        return formats;
    }

    public void setFormats(List<String> formats) {
        this.formats = formats;
    }
}
