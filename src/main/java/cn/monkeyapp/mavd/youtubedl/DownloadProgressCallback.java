package cn.monkeyapp.mavd.youtubedl;

/**
 * @author Corbett Zhang
 */
public interface DownloadProgressCallback {

    /**
     * 更新进度状态
     *
     * @param progress     进度百分百
     * @param etaInSeconds 预计剩余时间
     */
    void onProgressUpdate(float progress, long etaInSeconds);

//    /**
//     * 整行输出信息
//     * @param line
//     */
//    void onProgressUpdate(String line);


}
