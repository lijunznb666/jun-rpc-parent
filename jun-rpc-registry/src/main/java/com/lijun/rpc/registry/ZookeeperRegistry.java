package com.lijun.rpc.registry;

import com.lijun.rpc.core.Endpoint;
import com.lijun.rpc.core.tookit.ExceptionUtils;
import com.lijun.rpc.core.tookit.IpUtils;
import com.lijun.rpc.core.tookit.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Class Name ZookeeperRegistry ...
 *
 * @author LiJun
 * Created on 2020/4/10 16:17
 */
public class ZookeeperRegistry implements IRegistry {


    private static final Logger log = LoggerFactory.getLogger(ZookeeperRegistry.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private String registryAddress;

    private Map<String, List<Endpoint>> endpointsByService = new LinkedHashMap<>();

    private ZooKeeper zookeeper = null;


    public ZookeeperRegistry(String registryAddress) {
        this.registryAddress = registryAddress;
        connectServer();
        // addRootNode();
    }

    /**
     * 注册服务
     *
     * @param serviceName
     * @param port
     * @throws Exception
     */
    @Override
    public void register(String serviceName, int port) throws Exception {
        /*格式为 /registry/serivceName/1270.0.0.1:122*/
        if (StringUtils.isNotBlank(serviceName)) {
            String path = Constant.ZK_REGISTRY_PATH + "/" + serviceName + "/" + IpUtils.getHostIp() + ":" + port;
            createPathIfAbsent(path, CreateMode.PERSISTENT);
            createNode(path, serviceName);
        }

    }

    /**
     * 服务发现
     *
     * @param serviceName
     * @return
     */
    @Override
    public List<Endpoint> discover(String serviceName) {
        try {
            List<String> nodeList = zookeeper.getChildren(Constant.ZK_REGISTRY_PATH + "/" + serviceName, event -> {
                if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                    discover(serviceName);
                }
            });
            List<Endpoint> endpoints = new ArrayList<>();
            for (String node : nodeList) {
                // byte[] bytes = zookeeper.getData(Constant.ZK_REGISTRY_PATH + "/" + node, false, null);
                endpoints.add(new Endpoint(node));
            }
            log.info("service discover triggered updating connected server node,node data:{}", endpoints);
            endpointsByService.put(serviceName, endpoints);

            return endpoints;
        } catch (KeeperException | InterruptedException e) {
            log.error("watch node error:", e);
            throw ExceptionUtils.mpe(e);
        }
    }


    private void connectServer() {
        try {
            this.zookeeper = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, (WatchedEvent event) -> {
                //获取事件的状态
                Watcher.Event.KeeperState keeperState = event.getState();
                Watcher.Event.EventType eventType = event.getType();
                //如果是建立连接
                if (Watcher.Event.KeeperState.SyncConnected == keeperState) {
                    if (Watcher.Event.EventType.None == eventType) {
                        //如果建立连接成功，则发送信号量，让后续阻塞程序向下执行
                        latch.countDown();
                        log.info("ZK建立连接");
                    }
                }
            });
            log.info("开始连接ZK服务器");
            latch.await();
        } catch (Exception e) {
            log.error("ZookeeperRegistry connectServer error :", e);
        }
    }


    // private void addRootNode() {
    //     try {
    //         Stat s = zookeeper.exists(Constant.ZK_REGISTRY_PATH, false);
    //         if (s == null) {
    //             zookeeper.create(Constant.ZK_REGISTRY_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    //         }
    //     } catch (KeeperException e) {
    //         log.error(e.toString());
    //     } catch (InterruptedException e) {
    //         log.error(e.toString());
    //     }
    // }

    /**
     * @param path
     * @param createMode
     */
    public void createPathIfAbsent(String path, CreateMode createMode) throws KeeperException, InterruptedException {
        String[] split = path.split("/");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (StringUtils.hasText(split[i])) {
                sb.append(split[i]);
                Stat s = zookeeper.exists(sb.toString(), false);
                if (s == null) {
                    zookeeper.create(sb.toString(), new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
                }
            }
            if (i < split.length - 1) {
                sb.append("/");
            }
        }
    }


    private void createNode(String path, String data) {
        try {
            byte[] bytes = data.getBytes();
            String gen = zookeeper.create(path + "/", bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            log.info("create zookeeper node ({} => {})", path, data);
        } catch (KeeperException | InterruptedException e) {
            log.error("ZookeeperRegistry createNode error :", e);
        }
    }
}
