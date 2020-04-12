package com.lijun.rpc.core.balance;

import java.util.Map;

/**
 * Class Name SelectFirstLoadBalance ...
 * 默认选第一个
 *
 * @author LiJun
 * Created on 2020/4/6 11:00
 */
public class SelectFirstLoadBalance implements ILoadBalance {
    @Override
    public int select(Map<String, String> config, int amount) throws Exception {
        log.info("SelectFirstLoadBalance has been selected!");
        return 0;
    }
}
