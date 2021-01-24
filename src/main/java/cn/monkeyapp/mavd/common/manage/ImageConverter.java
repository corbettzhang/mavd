package cn.monkeyapp.mavd.common.manage;

import cn.monkeyapp.mavd.cache.LocalCache;
import cn.monkeyapp.mavd.common.Properties;
import cn.monkeyapp.mavd.youtubedl.ProgressCallback;

import java.io.File;
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
        ExecuteHelper.executeCommand(command, new ProgressCallback() {
            @Override
            public void onProgressUpdate(float progress, long etaInSeconds) {

            }

            @Override
            public void onProgressUpdate(String line) {
                LOGGER.log(Level.INFO, line);
            }
        });
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
            ExecuteHelper.executeCommand(command, new ProgressCallback() {
                @Override
                public void onProgressUpdate(float progress, long etaInSeconds) {

                }

                @Override
                public void onProgressUpdate(String line) {
                    LOGGER.log(Level.INFO, line);
                }
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }


}
