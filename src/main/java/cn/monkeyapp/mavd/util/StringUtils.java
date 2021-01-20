package cn.monkeyapp.mavd.util;

import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

/**
 * 字符串工具类
 *
 * @author Corbett Zhang
 */
public class StringUtils {

    public static String EMPTY = "";

    /**
     * 判断字符串是否是 空字符串 或者 是null
     *
     * @param str
     * @return null、""、"null","[]" 返回true，否则false
     */
    public static boolean isEmptyOrNull(String str) {
        if (str == null || str.length() == 0 || "null".equalsIgnoreCase(str) || "[]".equals(str)) {
            return true;
        }

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 为字符串添加下划线，营造一种超链接的假象
     *
     * @param str
     * @return
     */
    public static String genUnderline(String str, String fontColor) {
        if (StringUtils.isEmptyOrNull(str)) {
            return "";
        }

        if (StringUtils.isEmptyOrNull(fontColor)) {
            return StringUtils.format("<html><u>{0}</u></html>", str);
        }

        return StringUtils.format("<html><font color=\"{1}\"><u>{0}</u></font></html>", str, fontColor);
    }

    public static boolean isEmptyOrNull(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmptyOrNull(value);
            }
        }
        return result;
    }

    /**
     * {@link StringUtils#isEmptyOrNull(String)}取反
     *
     * @param str
     * @return
     */
    public static boolean isNotEmptyOrNull(String str) {
        return !isEmptyOrNull(str);
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String toFirstCharUpper(String str) {
        if (StringUtils.isEmptyOrNull(str)) {
            return StringUtils.EMPTY;
        }
        char c = str.charAt(0);
        if (Character.isUpperCase(c)) {
            return str;
        }
        StringBuffer sb = new StringBuffer(str);
        sb.setCharAt(0, Character.toUpperCase(c));
        return sb.toString();
    }

    /**
     * 格式化字符串
     *
     * @param value 字符串中可以存在变量占位符{0}...{n}
     * @param paras 对应占位符的变量值
     * @return
     */
    public static String format(String value, Object... paras) {
        return MessageFormat.format(value, paras);
    }

    public static String replace(String value) {
        String src = new String(value);
        src = replace(src, "*", "//*");
        src = replace(src, "?", "//?");
        src = replace(src, ")", "//)");
        src = replace(src, "(", "//(");
        src = replace(src, "{", "//{");
        src = replace(src, "}", "//}");
        src = replace(src, "|", "//|");
        src = replace(src, "$", "//$");
        src = replace(src, "+", "//+");
        src = replace(src, ".", "//.");
        return src;
    }

    public static String replace(String value, String old, String newChar) {
        return value.replace(old, newChar);
    }

    public static String getCode(Object... args) {
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            sb.append(arg);
        }

        int length = sb.length();
        int start = sb.indexOf("[");
        int end = sb.indexOf("]");
        if (length > 0 && start < end && end <= length) {
            return sb.substring(start + 1, end);
        }

        return sb.toString();
    }

    public static String getKey(Object... args) {
        return getKeyBySplit("_", args);
    }

    public static String getKeyBySplit(String split, Object... args) {
        StringBuilder sb = new StringBuilder();

        for (Object arg : args) {
            sb.append(arg);
            sb.append(split);
        }

        int length = sb.length();
        if (length > 0) {
            sb.delete(length - split.length(), length);
        }

        return sb.toString();
    }

    public static String conStringArray(String... array) {
        int size = 0;
        for (String string : array) {
            size += string.length();
        }

        StringBuilder sb = new StringBuilder(size);

        for (String string : array) {
            sb.append(string);
        }

        return sb.toString();
    }

    /**
     * 判断字符串是否全是数字
     *
     * @param str
     * @return 返回true，否则false
     */
    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /**
     * 去掉开头和结尾的"[]"
     *
     * @param list
     * @return
     */
    public static String toString(List<?> list) {
        String v = list.toString();
        return v.substring(1, v.length() - 1);
    }

    /**
     * 去掉开头和结尾的"[]"
     *
     * @param list
     * @return
     */
    public static String toString(Object[] list) {
        return toString(Arrays.asList(list));
    }

    /**
     * 字符串转大写
     *
     * @param s
     * @return
     */
    public static String toUpper(String s) {
        if (StringUtils.isEmptyOrNull(s)) {
            return s;
        }
        return s.toUpperCase();
    }

    /**
     * 判断字符串中，是否包含多个指定的字符串
     *
     * @param orginString
     * @param matchChars
     * @return
     */
    public static boolean isContains(String orginString, String[] matchChars) {
        boolean isContains = true;
        for (String matchChar : matchChars) {
            if (!orginString.contains(matchChar)) {
                isContains = false;
                break;
            }
        }
        return isContains;
    }


    /**
     * @param pathName e.g. Users,Corbett,Download ...
     * @return /Users/Corbett/Download/
     */
    public static String stringToPath(String... pathName) {
        StringBuilder path = new StringBuilder(File.separator);
        for (String name : pathName) {
            path.append(name).append(File.separator);
        }
        return path.toString();
    }

    /**
     * 将数组转换为字符串，并加上分隔符
     *
     * @param array 数组
     * @param separator 分隔符
     * @return 转换结果
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        } else {
            if (separator == null) {
                separator = "";
            }

            int noOfItems = array.length;
            if (noOfItems <= 0) {
                return "";
            } else {
                StringBuilder buf = new StringBuilder(noOfItems * 16);

                for (int i = 0; i <  array.length; ++i) {
                    if (i > 0) {
                        buf.append(separator);
                    }

                    if (array[i] != null) {
                        buf.append(array[i]);
                    }
                }

                return buf.toString();
            }
        }
    }

}
