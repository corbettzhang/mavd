package cn.monkeyapp.mavd.common.manage;

import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.Properties;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * webp图片转换
 *
 * @author Corbett Zhang
 * @since 2020-12-22
 */
public class ImageConverter {

    private static final Logger LOGGER = LogManager.getLogger(ImageConverter.class);

    private String commandDir;

    public ImageConverter() {
        this.commandDir = (String) LocalCache.getInstance().get(Properties.LIB_KEY);
    }

    public static ImageConverter create() {
        return new ImageConverter();
    }

    /**
     * Converter webp file to normal image
     *
     * @param src  webp file path
     * @param dest normal image path
     */
    public void toNormalImage(String src, String dest) {
        toNormalImage(new File(src), new File(dest));
    }

    /**
     * Converter webp file to normal image
     *
     * @param src  webp file path
     * @param dest normal image path
     */
    public void toNormalImage(File src, File dest) {
        String command = commandDir + (dest.getName().endsWith(".gif") ? File.separator + "gif2webp" : File.separator + "dwebp ") + src.getPath() + " -o " + dest.getPath();
        this.executeCommand(command);
    }

    /**
     * Convert normal image to webp file
     *
     * @param src  normal image path
     * @param dest webp file path
     */
    public void toWEBP(String src, String dest) {
        toWEBP(new File(src), new File(dest));
    }

    /**
     * Convert normal image to webp file
     *
     * @param src  normal image path
     * @param dest webp file path
     */
    public void toWEBP(File src, File dest) {
        try {
            String command = commandDir + (src.getName().endsWith(".gif") ? File.separator + "gif2webp " : File.separator + "cwebp ") + src.getPath() + " -o " + dest.getPath();
            this.executeCommand(command);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * execute command
     *
     * @param command command direct
     */
    private void executeCommand(String command) {
        LOGGER.log(Level.INFO, "Execute: " + command);

        StringBuilder output = new StringBuilder();
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        if (!"".equals(output.toString())) {
            LOGGER.log(Level.INFO, "Output: " + output);
        }
    }

}
