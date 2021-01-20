package cn.monkeyapp.mavd.common.init;

import cn.monkeyapp.mavd.Main;
import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.common.manage.LogManager;
import cn.monkeyapp.mavd.util.IOUtils;
import cn.monkeyapp.mavd.util.OsInfoUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.attribute.PosixFilePermission.*;

/**
 * 下载帮助类
 *
 * @author Corbett Zhang
 */
public class DownloadHelper {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    private static final String URL = "https://img.goodlymoon.com/${OS}/%s.zip";
    private static File LIB_DIR;
    private static String[] strings;

    static {
        File f = new File(Properties.USER_LIB).getParentFile();
        LIB_DIR = new File(f.getAbsolutePath() + File.separator + "lib");
        if (!LIB_DIR.exists()) {
            if (!LIB_DIR.mkdirs()) {
                LOGGER.log(Level.SEVERE, "Can not find directory to save monkeyapp lib. " +
                        "please try to set user home by -Duser.home=");
            }
        }
        strings = new String[]{"NsUserNotificationsBridge.dylib"};
    }

    private static void downloadLib(String s) {
        File unzipDir = new File(LIB_DIR.getAbsolutePath());
        try {
            File tempFile = File.createTempFile("mavd", "mavd");
            saveUrl(tempFile.getAbsolutePath(), s, true);
            IOUtils.unzip(tempFile.getAbsolutePath(), unzipDir.getAbsolutePath());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void saveUrl(final String filename, final String urlString, boolean printProgress)
            throws IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            URLConnection connection = openUrlConnection(urlString);
            in = new BufferedInputStream(connection.getInputStream());
            List<String> values = connection.getHeaderFields().get("Content-Length");
            int fileSize = 0;
            if (values != null && !values.isEmpty()) {
                String contentLength = (String) values.get(0);
                if (contentLength != null) {
                    // parse the length into an integer...
                    fileSize = Integer.parseInt(contentLength);
                }
            }

            fout = new FileOutputStream(filename);

            final byte[] data = new byte[1024 * 1024];
            int totalCount = 0;
            int count;
            long lastPrintTime = System.currentTimeMillis();
            while ((count = in.read(data, 0, 1024 * 1024)) != -1) {
                totalCount += count;
                if (printProgress) {
                    long now = System.currentTimeMillis();
                    if (now - lastPrintTime > 1000) {
                        LOGGER.log(Level.INFO, String.format("File size: %s, downloaded size: %s, downloading ..."
                                , formatFileSize(fileSize), formatFileSize(totalCount)));
                        lastPrintTime = now;
                    }
                }
                fout.write(data, 0, count);
            }
        } catch (javax.net.ssl.SSLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            IOUtils.close(in);
            IOUtils.close(fout);
        }
    }


    /**
     * support redirect
     *
     * @param url
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    private static URLConnection openUrlConnection(String url) throws MalformedURLException, IOException {
        URLConnection connection = new URL(url).openConnection();
        if (connection instanceof HttpURLConnection) {
            connection.setConnectTimeout(3000);
            // normally, 3xx is redirect
            int status = ((HttpURLConnection) connection).getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER) {
                    String newUrl = connection.getHeaderField("Location");
                    LOGGER.log(Level.SEVERE, String.format("Try to open url: %s, redirect to: %s", url, newUrl));
                    return openUrlConnection(newUrl);
                }
            }
        }
        return connection;
    }


    private static String formatFileSize(long size) {
        String hrSize;

        double b = size;
        double k = size / 1024.0;
        double m = ((size / 1024.0) / 1024.0);
        double g = (((size / 1024.0) / 1024.0) / 1024.0);
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else if (k > 1) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }

        return hrSize;
    }

    public static Boolean doDownload() {
        if (OsInfoUtils.isMacOS0()) {
            Arrays.stream(strings).forEach(e -> {
                if (!verify(e, LIB_DIR.getAbsolutePath())) {
                    downloadLib(String.format(URL.replace("${OS}", "macos"), e));
                    recursiveAuthorization(LIB_DIR.getAbsolutePath());
                }
                LocalCache.getInstance().add(LIB_DIR.getAbsolutePath() + File.separator + e, Properties.NS_USER_NOTIFICATIONS_BRIDGE_KEY);
            });
        }
        return true;
    }

    public static void updatePermission(String s) {
        Path path = Paths.get(s);
        PosixFileAttributeView posixView = Files.getFileAttributeView(path, PosixFileAttributeView.class);
        if (posixView == null) {
            return;
        }
        readPermissions(posixView);
        updatePermissions(posixView);
        readPermissions(posixView);
    }

    /**
     * 递归读取文件夹下的文件并授予权限
     *
     * @param path
     */
    public static void recursiveAuthorization(String path) {
        File file = new File(path);
        // 如果是文件夹，则需要递归处理
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File currentFile : files) {
                if (currentFile.isDirectory()) {
                    recursiveAuthorization(currentFile.getPath());
                } else {
                    updatePermission(currentFile.getAbsolutePath());
                }
            }
        } else {
            updatePermission(file.getAbsolutePath());
        }
    }


    public static void readPermissions(PosixFileAttributeView posixView) {
        try {
            PosixFileAttributes attribs;
            attribs = posixView.readAttributes();
            Set<PosixFilePermission> permissions = attribs.permissions();
            // 将posix文件权限集转换为rwxrwxrwx形式
            String rwxFormPermissions = PosixFilePermissions.toString(permissions);
            LOGGER.log(Level.INFO, rwxFormPermissions);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void updatePermissions(PosixFileAttributeView posixView) {
        try {
            Set<PosixFilePermission> permissions = EnumSet.of(OWNER_READ, OWNER_WRITE, OWNER_EXECUTE, GROUP_READ, GROUP_WRITE);
            posixView.setPermissions(permissions);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private static boolean verify(String e, String path) {
        File home = new File(path);
        if (home.isDirectory()) {
            return new File(home, e).exists();
        }
        return false;
    }
}
