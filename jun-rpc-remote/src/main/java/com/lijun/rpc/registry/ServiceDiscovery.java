package com.lijun.rpc.registry;


import com.lijun.rpc.client.ConnectManager;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class Name ServiceDiscovery ...
 *
 * @author LiJun
 * Created on 2020/4/8 20:30
 */
public class ServiceDiscovery {

    private static final Logger log = LoggerFactory.getLogger(ServiceDiscovery.class);


    private CountDownLatch latch = new CountDownLatch(1);

    private volatile List<String> dataList = new ArrayList<>();

    private String registryAddress;

    private ZooKeeper zooKeeper;

    public ServiceDiscovery(String registryAddress) {
        this.registryAddress = registryAddress;
        zooKeeper = connectServer();
        if (zooKeeper != null) {
            watchNode(zooKeeper);
        }
    }

    public String discover() {
        String data = null;
        int size = dataList.size();
        if (size > 0) {
            if (size == 1) {
                data = dataList.get(0);
                log.info("using only data:{}", data);
            } else {
                data = dataList.get(ThreadLocalRandom.current().nextInt(size));
                log.info("using random data:{}", data);
            }
        }

        return data;
    }

    public ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, event -> {
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }
            });
            latch.await();
        } catch (IOException | InterruptedException e) {
            log.error("connect server error:{}", ExceptionUtils.getStackTrace(e));
        }

        return zk;
    }

    private void watchNode(final ZooKeeper zk) {
        try {
            List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH, event -> {
                if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                    watchNode(zk);
                }
            });
            List<String> dataList = new ArrayList<>();
            for (String node : nodeList) {
                byte[] bytes = zk.getData(Constant.ZK_REGISTRY_PATH + "/" + node, false, null);
                dataList.add(new String(bytes));
            }
            log.info("service discovery triggered updating connected server node,node data:{}", dataList);
            this.dataList = dataList;
            updateConnectedServer();
        } catch (KeeperException | InterruptedException e) {
            log.error("watch node error:{}", ExceptionUtils.getStackTrace(e));
        }
    }

    private void updateConnectedServer() {
        ConnectManager.getInstance().updateConnectedServer(this.dataList);
    }

    public void stop() {
        if (zooKeeper != null) {
            try {
                zooKeeper.close();
            } catch (InterruptedException e) {
                log.error("stop server error:{}", ExceptionUtils.getStackTrace(e));
            }
        }
    }

}
