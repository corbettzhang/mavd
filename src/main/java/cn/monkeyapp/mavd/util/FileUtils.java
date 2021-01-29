package cn.monkeyapp.mavd.util;

import cn.monkeyapp.mavd.Main;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.Properties;

public class FileUtils {

    /**
     * 把一个文件转化为byte字节数组。
     *
     * @return
     */
    public static byte[] fileConvertToByteArray(File file) {
        byte[] data = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            data = baos.toByteArray();

            fis.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public static final String APP_VERSION = "app.version";
    public static final String APP_NAME = "app.name";

    public static String getAppVersion(String key) {
        String appVersion = null;
        Properties properties = new Properties();
        try {
            properties.load(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("app.properties")));
            if (!properties.isEmpty()) {
                appVersion = properties.getProperty(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appVersion;
    }


    /**
     * inputStream转outputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static ByteArrayOutputStream parseInputStream2OutputStream(InputStream in) throws Exception {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            swapStream.write(ch);
        }
        return swapStream;
    }

    /**
     * outputStream转inputStream
     *
     * @param out
     * @return
     * @throws Exception
     */
    public ByteArrayInputStream parseOutputStream2InputStream(OutputStream out) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = (ByteArrayOutputStream) out;
        ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream;
    }

    /**
     * inputStream转String
     *
     * @param in
     * @return
     * @throws Exception
     */
    public String parse_String(InputStream in) throws Exception {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            swapStream.write(ch);
        }
        return swapStream.toString();
    }

    /**
     * OutputStream 转String
     *
     * @param out
     * @return
     * @throws Exception
     */
    public String parse_String(OutputStream out) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = (ByteArrayOutputStream) out;
        ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream.toString();
    }

    /**
     * String转inputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public ByteArrayInputStream parse_inputStream(String in) throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream(in.getBytes());
        return input;
    }

    /**
     * String 转outputStream
     *
     * @param in
     * @return
     * @throws Exception
     */
    public ByteArrayOutputStream parse_outputStream(String in) throws Exception {
        return parseInputStream2OutputStream(parse_inputStream(in));
    }

    public static void download2(String urlString, File file) throws Exception {
        //new一个URL对象
        URL url = new URL(urlString);
        //打开链接
        URLConnection conn = url.openConnection();
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        InputStream inStream = conn.getInputStream();
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性
        byte[] data = readInputStream(inStream);
        //创建输出流
        OutputStream outStream = new FileOutputStream(file);
        //写入数据
        outStream.write(data);
        //关闭输出流
        outStream.close();
    }

    public static void download(String urlString, File file) throws Exception {
        download(urlString, file, null);
    }

    public static void download(String urlString, File file, InetSocketAddress address) throws Exception {
        // 构造URL
        URL url = new URL(urlString);

        // 打开连接
        URLConnection con;
        if (address != null) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
            con = url.openConnection(proxy);
        } else {
            con = url.openConnection();
        }

        //超时响应时间为10秒
        con.setConnectTimeout(10 * 1000);
        // 输入流
        InputStream is = con.getInputStream();
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        OutputStream os = new FileOutputStream(file);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    /**
     * 将InputStream写入本地文件
     *
     * @param in   输入流
     * @param dest 写入本地目录
     * @throws IOException
     */
    public static void copy(InputStream in, File dest) throws IOException {
        OutputStream out = new FileOutputStream(dest);
        byte[] buffer = new byte[1024];
        int length;
        //复制文件内容（以字节为单位）
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        dest.setExecutable(true);
        in.close();
        out.close();
    }
}
