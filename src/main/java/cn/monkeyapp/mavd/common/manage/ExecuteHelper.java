package cn.monkeyapp.mavd.common.manage;

import cn.monkeyapp.mavd.youtubedl.ProgressCallback;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author zhangcong
 */
public class ExecuteHelper {

    private static final Logger LOGGER = LogManager.getLogger(ImageConverter.class);

    /**
     * execute command
     *
     * @param command command direct
     */
    public static void executeCommand(String command, ProgressCallback callback) {
        LOGGER.log(Level.INFO, "Execute: " + command);
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                callback.onProgressUpdate(line);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

}
