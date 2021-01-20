package cn.monkeyapp.mavd.util;

import cn.monkeyapp.mavd.common.Properties;

/**
 * 操作系统信息工具类
 *
 * @author Corbett Zhang
 */
public class OsInfoUtils {

    private static String OS = Properties.OS_NAME.toLowerCase();

    private static OsInfoUtils _instance = new OsInfoUtils();

    private PlatFormEnum platform;

    private OsInfoUtils() {
    }

    public static boolean isLinux() {
        return OS.indexOf("linux") >= 0;
    }

    public static boolean isMacOS() {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") < 0;
    }

    public static boolean isMacOSX() {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0 && OS.indexOf("x") > 0;
    }

    public static boolean isMacOS0() {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0;
    }

    public static boolean isWindows() {
        return OS.indexOf("windows") >= 0;
    }

    public static boolean isOS2() {
        return OS.indexOf("os/2") >= 0;
    }

    public static boolean isSolaris() {
        return OS.indexOf("solaris") >= 0;
    }

    public static boolean isSunOS() {
        return OS.indexOf("sunos") >= 0;
    }

    public static boolean isMPEiX() {
        return OS.indexOf("mpe/ix") >= 0;
    }

    public static boolean isHPUX() {
        return OS.indexOf("hp-ux") >= 0;
    }

    public static boolean isAix() {
        return OS.indexOf("aix") >= 0;
    }

    public static boolean isOS390() {
        return OS.indexOf("os/390") >= 0;
    }

    public static boolean isFreeBSD() {
        return OS.indexOf("freebsd") >= 0;
    }

    public static boolean isIrix() {
        return OS.indexOf("irix") >= 0;
    }

    public static boolean isDigitalUnix() {
        return OS.indexOf("digital") >= 0 && OS.indexOf("unix") > 0;
    }

    public static boolean isNetWare() {
        return OS.indexOf("netware") >= 0;
    }

    public static boolean isOSF1() {
        return OS.indexOf("osf1") >= 0;
    }

    public static boolean isOpenVMS() {
        return OS.indexOf("openvms") >= 0;
    }

    /**
     * 获取操作系统名字
     *
     * @return 操作系统名
     */
    public static PlatFormEnum getOSName() {
        if (isAix()) {
            _instance.platform = PlatFormEnum.AIX;
        } else if (isDigitalUnix()) {
            _instance.platform = PlatFormEnum.Digital_Unix;
        } else if (isFreeBSD()) {
            _instance.platform = PlatFormEnum.FreeBSD;
        } else if (isHPUX()) {
            _instance.platform = PlatFormEnum.HP_UX;
        } else if (isIrix()) {
            _instance.platform = PlatFormEnum.Irix;
        } else if (isLinux()) {
            _instance.platform = PlatFormEnum.Linux;
        } else if (isMacOS()) {
            _instance.platform = PlatFormEnum.Mac_OS;
        } else if (isMacOSX()) {
            _instance.platform = PlatFormEnum.Mac_OS_X;
        } else if (isMPEiX()) {
            _instance.platform = PlatFormEnum.MPEiX;
        } else if (isNetWare()) {
            _instance.platform = PlatFormEnum.NetWare_411;
        } else if (isOpenVMS()) {
            _instance.platform = PlatFormEnum.OpenVMS;
        } else if (isOS2()) {
            _instance.platform = PlatFormEnum.OS2;
        } else if (isOS390()) {
            _instance.platform = PlatFormEnum.OS390;
        } else if (isOSF1()) {
            _instance.platform = PlatFormEnum.OSF1;
        } else if (isSolaris()) {
            _instance.platform = PlatFormEnum.Solaris;
        } else if (isSunOS()) {
            _instance.platform = PlatFormEnum.SunOS;
        } else if (isWindows()) {
            _instance.platform = PlatFormEnum.Windows;
        } else {
            _instance.platform = PlatFormEnum.Others;
        }
        return _instance.platform;
    }

    enum PlatFormEnum {

        Any("any"),
        Linux("Linux"),
        Mac_OS("Mac OS"),
        Mac_OS_X("Mac OS X"),
        Windows("Windows"),
        OS2("OS/2"),
        Solaris("Solaris"),
        SunOS("SunOS"),
        MPEiX("MPE/iX"),
        HP_UX("HP-UX"),
        AIX("AIX"),
        OS390("OS/390"),
        FreeBSD("FreeBSD"),
        Irix("Irix"),
        Digital_Unix("Digital Unix"),
        NetWare_411("NetWare"),
        OSF1("OSF1"),
        OpenVMS("OpenVMS"),
        Others("Others");

        private PlatFormEnum(String desc) {
            this.description = desc;
        }

        @Override
        public String toString() {
            return description;
        }

        private String description;

    }
}



