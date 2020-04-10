package com.lijun.rpc.core;

import com.lijun.rpc.core.tookit.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Class Name RpcConfig ...
 *
 * @author LiJun
 * Created on 2020/4/6 11:01
 */
public class RpcConfig {

    private static final String RESOURCE_FILE = "rpc.properties";
    private static final Logger log = LoggerFactory.getLogger(RpcConfig.class);

    private static Map<String, String> configMap = new LinkedHashMap<>();
    private static boolean initialized = false;

    private static void load() throws Exception {

        Properties properties = new Properties();
        InputStream inputStream = RpcConfig.class.getClassLoader().getResourceAsStream(RESOURCE_FILE);
        properties.load(inputStream);

        properties.forEach((key, value) -> configMap.put((String) key, (String) value));

        log.info("config init finish! the config is : [{}]", configMap);

    }

    public static String get(String key) {
        if (!initialized) {
            try {
                load();
                initialized = true;
            } catch (Exception e) {
                log.error("RpcConfig get error :", e);
                throw ExceptionUtils.mpe(e);
            }
        }
        return configMap.get(key);
    }

}
