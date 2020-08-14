package com.mashirro.framework.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * properties文件读取工具类
 */
public class PropsUtil {

    private static final Logger logger = LoggerFactory.getLogger(Properties.class);

    private static Properties prop = new Properties();

    private static InputStream is;

    /**
     * 加载配置文件
     *
     * @param fileName classpath下的properties文件
     * @return
     */
    public static Properties loadProps(String fileName) {
        is = PropsUtil.class.getClassLoader().getResourceAsStream(fileName);
        try {
            prop.load(is);
            for (Object o : prop.keySet()) {
                logger.info("load property--> key: " + o.toString() + " and value: " + prop.getProperty(o.toString()).toString());
            }
        } catch (IOException e) {
            logger.error("加载properties文件失败!", e);
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("关闭输入流失败", e);
                    e.printStackTrace();
                }
            }
        }
        return prop;
    }

}
