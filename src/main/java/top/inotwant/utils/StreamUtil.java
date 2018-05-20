package top.inotwant.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public final class StreamUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(StreamUtil.class);

    /**
     * 从流中获取字符串
     */
    public static String getString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line);
            return sb.toString();
        } catch (IOException e) {
            LOGGER.error("IO exception in getting string from steam.");
            throw new RuntimeException(e);
        }
    }

    /**
     * 将输入流复制到输出流
     */
    public static void copyStream(InputStream in, OutputStream out) {
        byte[] bytes = new byte[4 * 1024];
        int length;
        try {
            while ((length = in.read(bytes)) > 0) {
                out.write(bytes, 0, length);
            }
            out.flush();
        } catch (IOException e) {
            LOGGER.error("copy stream failure", e);
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                LOGGER.error("close stream fail");
            }
        }
    }

}
