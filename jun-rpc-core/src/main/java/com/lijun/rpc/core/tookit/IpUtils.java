package com.lijun.rpc.core.tookit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * Class Name IpUtils ...
 * ip 工具类
 *
 * @author LiJun
 * Created on 2020/4/6 10:34
 */
public class IpUtils {
    private static final Logger log = LoggerFactory.getLogger(IpUtils.class);


    public static String getHostIp()  {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            log.error("IpUtils getHostIp error :", e);
            throw ExceptionUtils.mpe(e);
        }
    }

}
