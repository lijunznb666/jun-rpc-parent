package com.lijun.rpc.core.tookit;

import java.util.Map;

/**
 * Class Name MapUtils ...
 *
 * @author LiJun
 * Created on 2020/4/9 15:09
 */
public class MapUtils {

    public static boolean isEmpty(Map<?, ?> map){
        return null == map || map.size() <= 0;
    }

    public static boolean isNotEmpty(Map<?,?> map){
        return !isEmpty(map);
    }

}
