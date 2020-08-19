package com.mashirro.framework.utils;


import com.mashirro.framework.annotation.Action;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类扫描器
 */
public class ClassScanner {

    /**
     * 包名(例如:com.mashirro.web.controller)
     */
    private final String packageName;

    /**
     * 包路径，用于文件中对路径操作(例如:com\mashirro\web\controller)
     */
    private final String packageDirName;

    /**
     * 点
     */
    private final char DOT = '.';

    /**
     * 过滤器
     */
    private final Filter<Class<?>> classFilter;

    /**
     * 扫描结果集
     */
    private final Set<Class<?>> classes = new HashSet<>();

    public ClassScanner(String packageName, Filter<Class<?>> classFilter) {
        this.packageName = packageName;
        this.packageDirName = packageName.replace(DOT, File.separatorChar);
        this.classFilter = classFilter;

    }

    /**
     * 扫描包路径下满足class过滤器条件的所有class文件
     *
     * @return 类集合
     */
    public Set<Class<?>> scan() throws IOException {
        Enumeration<URL> urls = ClassLoaderUtil.getClassLoader().getResources(this.packageDirName);
        while (urls.hasMoreElements()) {
            //URL(Uniform Resource Locator):统一资源定位符
            URL url = urls.nextElement();
            //url.getProtocol():返回URL的协议s
            switch (url.getProtocol()) {
                case "file":
                    scanFile(new File(URLDecoder.decode(url.getFile(), "utf-8")), null);
                    break;
                case "jar":
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    scanJar(jarURLConnection.getJarFile());
                    break;
            }
        }
        //返回一个不可修改的set集合
        return Collections.unmodifiableSet(classes);
    }


    /**
     * 扫描文件或目录中的类
     *
     * @param file    文件或目录
     * @param rootDir 包名对应classpath绝对路径
     */
    private void scanFile(File file, String rootDir) {
        if (file.isFile()) {
            final String fileName = file.getAbsolutePath();
            if (fileName.endsWith(".class")) {
                //减6是因为去掉".class"
                final String className = fileName.substring(rootDir.length(), fileName.length() - 6).replace(File.separatorChar, DOT);
                //加载满足条件的类
                addIfAccpet(className);
            } else if (fileName.endsWith(".jar")) {
                try {
                    scanJar(new JarFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        } else if (file.isDirectory()) {
            final File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    scanFile(f, (rootDir == null) ? subPathBeforePackage(file) : rootDir);
                }
            }
        }
    }

    /**
     * 截取文件绝对路径中包名之前的部分
     *
     * @param file 文件
     * @return 包名之前的部分
     */
    private String subPathBeforePackage(File file) {
        String filePath = file.getAbsolutePath();
        if (StringUtils.isNotEmpty(packageDirName)) {
            final int pos = filePath.lastIndexOf(packageDirName);
            if (pos == -1) {
                return filePath;
            } else if (pos == 0) {
                return StringUtils.EMPTY;
            } else {
                return filePath.substring(0, pos);
            }
        }
        return StringUtils.EMPTY;
    }


    /**
     * 通过过滤器，是否满足接受此类的条件
     *
     * @param className 类名
     */
    private void addIfAccpet(String className) {
        if (StringUtils.isEmpty(className)) {
            return;
        }
        try {
            //加载类
            Class<?> clazz = ClassLoaderUtil.loadClass(className);
            if (clazz != null) {
                //满足条件的放入classes结果集
                if (classFilter == null || classFilter.accept(clazz)) {
                    classes.add(clazz);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    /**
     * 扫描jar包
     *
     * @param jarFile jar包
     */
    private void scanJar(JarFile jarFile) {
        //返回zip文件项的枚举。
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String name = jarEntry.getName();
            if (name.endsWith(".class") && !jarEntry.isDirectory()) {    //以.class结尾并且不是目录
                //减6是因为去掉".class"
                final String className = name.substring(0, name.length() - 6).replace(File.separatorChar, DOT);
                //加载满足条件的类
                addIfAccpet(className);
            }

        }
    }


    /**
     * 测试main方法
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        ClassScanner classScanner = new ClassScanner("com.mashirro.framework.cache", null);
        System.out.println(classScanner.scan());
        System.out.println("----------------------------------------");
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation("com.mashirro.framework.cache", Action.class);
        System.out.println(classes);
    }
}
