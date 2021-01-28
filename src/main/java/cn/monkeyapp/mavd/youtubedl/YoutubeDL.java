package cn.monkeyapp.mavd.youtubedl;

import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.ProgressCallback;
import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.entity.*;
import cn.monkeyapp.mavd.exception.MonkeyException;
import cn.monkeyapp.mavd.util.OsInfoUtils;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * <p>提供youtube-dl可执行文件的接口</p>
 *
 * <p>
 * 有关youtube-dl的更多信息，请参见
 * <a href="https://github.com/rg3/youtube-dl/blob/master/README.md">YouTube DL文档</a>
 * </p>
 *
 * @author Corbett Zhang
 */
public class YoutubeDL {

    /**
     * 将可执行文件名称附加到命令
     *
     * @param command 命令字符串
     * @return 命令字符串
     */
    protected static String buildCommand(String command) {
        String executablePath;
        if (OsInfoUtils.isWindows()) {
            executablePath = "youtube-dl.exe";
        }else{
            executablePath = "youtube-dl";
        }
        return String.format("%s %s", LocalCache.getInstance().get(Properties.LIB_KEY) + File.separator + executablePath, command);
    }

    /**
     * 执行youtube-dl请求
     *
     * @param request 请求对象
     * @return 响应对象
     * @throws MonkeyException
     */
    public static YoutubeDLResponse execute(YoutubeDLRequest request) throws MonkeyException {
        return execute(request, null);
    }

    /**
     * 执行youtube-dl请求
     *
     * @param request  请求对象
     * @param callback 回调
     * @return 响应对象
     * @throws MonkeyException
     */
    public static YoutubeDLResponse execute(YoutubeDLRequest request, ProgressCallback callback) throws MonkeyException {

        String command = buildCommand(request.buildOptions());
        String directory = request.getDirectory();
        Map<String, String> options = request.getOption();

        YoutubeDLResponse youtubeDLResponse;
        Process process;
        int exitCode;
        StringBuffer outBuffer = new StringBuffer(); //stdout
        StringBuffer errBuffer = new StringBuffer(); //stderr
        long startTime = System.nanoTime();

        String[] split = command.split(" ");

        ProcessBuilder processBuilder = new ProcessBuilder(split);

        // 定义目录（如果通过）
        if (directory != null) {
            processBuilder.directory(new File(directory));
        }

        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new MonkeyException(e);
        }

        InputStream outStream = process.getInputStream();
        InputStream errStream = process.getErrorStream();

        StreamProcessExtractor stdOutProcessor = new StreamProcessExtractor(outBuffer, outStream, callback);
        StreamGobbler stdErrProcessor = new StreamGobbler(errBuffer, errStream);

        try {
            stdOutProcessor.join();
            stdErrProcessor.join();
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            // 进程由于某种原因退出
            throw new MonkeyException(e);
        }

        String out = outBuffer.toString();
        String err = errBuffer.toString();

        if (exitCode > 0) {
            throw new MonkeyException(err);
        }

        int elapsedTime = (int) ((System.nanoTime() - startTime) / 1000000);

        youtubeDLResponse = new YoutubeDLResponse(command, options, directory, exitCode, elapsedTime, out, err);

        return youtubeDLResponse;
    }


    /**
     * 获取youtube-dl可执行版本
     *
     * @return 版本字串
     * @throws MonkeyException
     */
    public static String getVersion() throws MonkeyException {
        YoutubeDLRequest request = new YoutubeDLRequest();
        request.setOption("version");
        return YoutubeDL.execute(request).getOut();
    }

    /**
     * 字幕列表
     *
     * @param url Video url
     * @return list of tag
     * @throws MonkeyException
     */
    public static YoutubeDLResponse getVideoSubtitle(String url, String id) throws MonkeyException {

        final Preference preference = (Preference) LocalCache.getInstance().get(Properties.PREFERENCE_KEY);

        YoutubeDLRequest request = new YoutubeDLRequest(url);

        if (preference.getIsProxy() == 1) {
            request.setOption("proxy", preference.getProxy().getUrl());
        }

        // youtube专用参数"write-auto-sub"，其他的视频网站没有
        request.setOption("write-auto-sub");
        request.setOption("skip-download");
        request.setOption("sub-format", "vtt");
        request.setOption("sub-lang", "zh-Hans");
//        request.setOption("sub-lang", "zh-Hans,en");
        request.setOption("o", LocalCache.getInstance().get(Properties.DATA_KEY) + id + File.separator);
        return YoutubeDL.execute(request);
    }

    /**
     * 检索视频中的所有可用信息
     *
     * @param url 影片网址
     * @return 影片资讯
     * @throws MonkeyException
     */
    public static VideoInfo getVideoInfo(String url) throws MonkeyException {

        final Preference preference = (Preference) LocalCache.getInstance().get(Properties.PREFERENCE_KEY);

        // 建立请求
        YoutubeDLRequest request = new YoutubeDLRequest(url);
        request.setOption("dump-json");
        request.setOption("no-playlist");

        if (preference.getIsProxy() == 1) {
            request.setOption("proxy", preference.getProxy().getUrl());
        }

        YoutubeDLResponse response = YoutubeDL.execute(request);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();
        try {
            return gson.fromJson(response.getOut(), Content.class);
        } catch (JsonSyntaxException e) {
            throw new MonkeyException("Unable to parse video information: " + e.getMessage());
        }
    }

    /**
     * 清单格式
     *
     * @param url 影片网址
     * @return 格式清单
     * @throws MonkeyException
     */
    public static List<VideoFormat> getFormats(String url) throws MonkeyException {
        VideoInfo info = getVideoInfo(url);
        return info.getFormats();
    }

    /**
     * 列出缩略图
     *
     * @param url 影片网址
     * @return 缩略图列表
     * @throws MonkeyException
     */
    public static List<VideoThumbnail> getThumbnails(String url) throws MonkeyException {
        VideoInfo info = getVideoInfo(url);
        return info.getThumbnails();
    }

    /**
     * 类别列表
     *
     * @param url Video url
     * @return list of category
     * @throws MonkeyException
     */
    public static List<String> getCategories(String url) throws MonkeyException {
        VideoInfo info = getVideoInfo(url);
        return info.getCategories();
    }

    /**
     * 标签列表
     *
     * @param url Video url
     * @return list of tag
     * @throws MonkeyException
     */
    public static List<String> getTags(String url) throws MonkeyException {
        VideoInfo info = getVideoInfo(url);
        return info.getTags();
    }

}
