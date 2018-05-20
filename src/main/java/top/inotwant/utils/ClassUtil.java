package top.inotwant.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 获取类加载器
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载指定类
     */
    public static Class<?> loadClass(String className) {
        try {
            // TODO 注意 class.forName 与 ClassLoader.loadClass 的区别（关于 static 是否静态初始化）
            return Class.forName(className, true, getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("class not fond", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载包下所有类
     */
    public static Set<Class<?>> loadClassSet(String packageName) {
        String packagePath = packageName.replace(".", "/");
        try {
            Set<Class<?>> result = new HashSet<>();
            // TODO 理解错了 getResources 的意义。（一定要看 api 的文档）
            Enumeration<URL> resources = getClassLoader().getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL currentURL = resources.nextElement();
                if ("file".equals(currentURL.getProtocol())) {
                    String path = currentURL.getPath().replaceAll("%20", " ");
                    addClass(path, packageName, result);
                } else if ("jar".equals(currentURL.getProtocol())) {
                    // TODO 现不知道 jar包 路径如何表示，故没有测试
                    JarURLConnection conn = (JarURLConnection) currentURL.openConnection();
                    JarFile jarFile = conn.getJarFile();
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry jarEntry = entries.nextElement();
                        String jarEntryName = jarEntry.getName();
                        if (jarEntryName.endsWith(".class")) {
                            String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                            result.add(loadClass(className));
                        }
                    }
                }
            }
            return result;
        } catch (IOException e) {
            LOGGER.error("read package failed", e);
            throw new RuntimeException(e);
        }
    }

    private static void addClass(String path, String packageName, Set<Class<?>> result) {
        File[] files = new File(path).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String subFileName = file.getName();
                    String className = subFileName.substring(0, subFileName.lastIndexOf("."));
                    result.add(loadClass(packageName + "." + className));
                } else if (file.isDirectory()) {
                    addClass(file.getAbsolutePath(), packageName + "." + file.getName(), result);
                }
            }
        }
    }
}
