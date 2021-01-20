package cn.monkeyapp.mavd.util;

/////////////////////////////////////////////////////////
// Bare Bones Browser Launch                           //
// Version 1.5 (December 10, 2005)                     //
// By Dem Pilafian                                     //
// 支持: Mac OS X, GNU/Linux, Unix, Windows XP         //
// 可免费使用                                           //
/////////////////////////////////////////////////////////

import java.lang.reflect.Method;

/**
 * @author Dem Pilafian
 * @author John Kristian
 */
public class OpenBrowserUtils {

    public static void openUrl(String url) {
        try {
            browse(url);
        } catch (Exception e) {
            // ignore
        }
    }

    private static void browse(String url) throws Exception {
        //苹果的打开方式
        if (OsInfoUtils.isMacOS() || OsInfoUtils.isMacOSX()) {
            Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openUrl = fileMgr.getDeclaredMethod("openURL", String.class);
            openUrl.invoke(null, url);
        } else if (OsInfoUtils.isWindows()) {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        } else {
            // Unix or Linux的打开方式
            String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; count++) {
                //执行代码，在brower有值后跳出，
                //这里是如果进程创建成功了，==0是表示正常结束。
                if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
                    browser = browsers[count];
                }
            }
            if (browser == null) {
                throw new Exception("Could not find web browser");
            } else {
                //这个值在上面已经成功的得到了一个进程。
                Runtime.getRuntime().exec(new String[]{browser, url});
            }
        }
    }
}