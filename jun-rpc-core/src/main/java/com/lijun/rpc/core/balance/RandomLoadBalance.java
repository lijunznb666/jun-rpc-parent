package com.lijun.rpc.core.balance;

import java.util.Map;
import java.util.Random;

/**
 * Class Name RandomLoadBalance ...
 * 随机策略
 *
 * @author LiJun
 * Created on 2020/4/6 11:00
 */
public class RandomLoadBalance implements ILoadBalance {

    private Random random = new Random();

    @Override
    public int select(Map<String, String> config, int amount) throws Exception {
        log.info("RandomLoadBalance has been selected");
        if (amount <= 0) {
            throw new Exception("RandomLoadBalance: no available items to select");
        } else if (amount == 1) {
            return 0;
        } else {
            return random.nextInt(amount);
        }
    }
}
