package cn.monkeyapp.mavd.util;

import cn.monkeyapp.mavd.common.manage.LogManager;
import org.sqlite.date.DateFormatUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Corbett Zhang
 */
public class DateUtils {
    private static final Logger LOGGER = LogManager.getLogger(DateUtils.class);

    /**
     * 仅显示年月日，例如 20150811.
     */
    public final static String yyyyMMdd = "yyyyMMdd";

    /**
     * 仅显示年月日，例如 2015-08-11.
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public final static String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取日期时间字符串，默认格式为（yyyy-MM-dd）.
     *
     * @param date    需要转化的日期时间
     * @param pattern 时间格式，例如"yyyy-MM-dd" "HH:mm:ss" "E"等
     * @return String 格式转换后的时间字符串
     * @since 1.0
     */
    public static String formatDate(Date date, String pattern) {
        String formatDate;
        if (pattern != null && !"".equals(pattern)) {
            formatDate = DateFormatUtils.format(date, pattern);
        } else {
            formatDate = DateFormatUtils.format(date, DateUtils.yyyyMMdd);
        }
        return formatDate;
    }

    /**
     * 日期比较，如果a早于b返回true，否则返回false
     *
     * @param time1 时间1
     * @param time2 时间2
     * @return 比较结果
     */
    public static boolean dateCompare(String time1, String time2, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            //将字符串形式的时间转化为Date类型的时间
            Date a = sdf.parse(time1);
            Date b = sdf.parse(time2);
            //Date类的一个方法，
            return a.before(b);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取调整日期
     *
     * @param originalDate 被调整的日期，null表示当前时间
     * @param type         {@link `}类定义的各种常量，年、月、周、日、时、分、秒、毫秒
     * @param value        调整值 正数表示日期之后，负数表示之前
     * @return
     */
    public static Date adjust(Date originalDate, int type, int value) {
        Calendar cal = Calendar.getInstance();

        if (originalDate != null) {
            cal.setTime(originalDate);
        }

        if (value != 0) {
            cal.add(type, value);
        }

        return cal.getTime();
    }
}
