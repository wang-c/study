package com.github.tjp.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by tujinpeng on 2017/4/15.
 */
public class ZookeeperApi {
    private static String url = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zk = null;
    //每个znode节点包含的元素
    private static Stat stat = new Stat();

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        /**
         * 建立会话,客户端将收到SyncConnected通知
         */
        zk = new ZooKeeper(url, 5000, new SimpleWatch());
        //阻塞等待服务器端发来的SyncConnected事件通知
        countDownLatch.await();

        /**
         * 创建节点
         */
        //同步创建临时的节点
        String syncCreatePath1 = zk.create("/zk-test-sync-ephemeral", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("sync create node : " + syncCreatePath1);
        //同步创建临时有序的节点
        String syncCreatePath2 = zk.create("/zk-test-sync-ephemeral", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("sync create node : " + syncCreatePath2);
        //Async异步创建节点,通过回调函数IStringCallBack接收异步通知
        zk.create("/zk-test-async-ephemeral", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, new IStringCallBack(), "create context");
        zk.create("/zk-test-async-ephemeral", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, new IStringCallBack(), "create context");

        /**
         * 删除节点
         */
        //同步
        zk.delete(syncCreatePath1, 0);
        //Async异步删除节点,通过回调函数IVoidCallback接收异步通知
        zk.delete(syncCreatePath2, 0, new IVoidCallback(), "delete ndoe context");

        /**
         * 获取节点
         */
        String path = "/zk-test";
        zk.create(path, "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.create(path + "/c1", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        //同步获取children节点
        List<String> syncChildren = zk.getChildren(path, true);
        System.out.println("sync /zk-test children node : " + syncChildren);
        //异步获取children节点
        zk.getChildren(path, true, new IChildrenCallback(), "children node context");
        ///zk-test下孩子节点变化,会通知客户端的SimpleWatch
        zk.create(path + "/c2", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        /**
         * 获取更新节点data
         */
        //同步获取节点data,stat用来返回具体的zndoe节点信息
        System.out.println("sync get node " + path + " data : " + new String(zk.getData(path, true, stat)));
        //异步获取节点data
        zk.getData(path, true, new IDataCallBack(), "get node data context");
        //更新节点data,使path节点data变化,服务器端会通知客户端的SimpleWatch
        zk.setData(path, "test01".getBytes(), stat.getVersion());//并发cas原理,更新data时要传递上一次更新时的版本号,同时用版本号规避了ABA问题


        /**
         * 检测节点是否存在
         *
         */
        String existPath = "/zk-book";
        Stat existNode = zk.exists(existPath, true);
        System.out.println("node [" + existPath + "] exist : " + existNode);

        //下面existPath节点的创建,更新数据,删除变化,会通知到客户端SimpleWatch,但是子节点的变化不会通知
        zk.create(existPath, "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.setData(existPath, "124".getBytes(), -1);
        zk.create(existPath + "/c1", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.delete(existPath + "/c1", -1);
        zk.delete(existPath, -1);
        /**
         * 线程无限睡眠,让会话一直保持
         */
        Thread.sleep(Integer.MAX_VALUE);
    }

    private static class SimpleWatch implements Watcher {

        public void process(WatchedEvent event) {
            System.out.println("Revice watch event : " + event);
            try {
                //监听服务器端建立会话通知
                //注意第一次建立会话的通知event里,path为null
                if (Event.KeeperState.SyncConnected == event.getState() && event.getPath() == null) {
                    countDownLatch.countDown();
                } else if (event.getType() == Event.EventType.NodeChildrenChanged) {
                    //监听event.getPath()的child节点变化通知
                    System.out.println(event.getPath() + " children is change , last children:" + zk.getChildren(event.getPath(), true));
                } else if (event.getType() == Event.EventType.NodeDataChanged) {
                    //监听event.getPath()的节点数据变化通知,异步处理
                    //zk.getData(event.getPath(), true, new IDataCallBack(), "get node data context");
                    System.out.println("node [" + event.getPath() + "] NodeDataChanged");
                    zk.exists(event.getPath(), true);
                } else if (event.getType() == Event.EventType.NodeCreated) {
                    System.out.println("node [" + event.getPath() + "] NodeCreated");
                    zk.exists(event.getPath(), true);
                } else if (event.getType() == Event.EventType.NodeDeleted) {
                    System.out.println("node [" + event.getPath() + "] NodeDeleted");
                    zk.exists(event.getPath(), true);
                }
            } catch (Exception e) {

            }


        }
    }


    /**
     * 创建节点异步通知类
     */
    static class IStringCallBack implements AsyncCallback.StringCallback {
        /**
         * 服务器端异步创建节点之后调用的函数
         *
         * @param rc   服务器端返回的错误码(0-成功)
         * @param path 客户端创建节点时传入的节点路径
         * @param ctx  客户端创建节点时传入的,用于传递上下文
         * @param name 服务器端实际的节点名
         */
        public void processResult(int rc, String path, Object ctx, String name) {
            System.out.println("async create ndoe result : [" + rc + ", " + path + ", " + ctx + ", real name : " + name + "]");
        }
    }

    /**
     * 删除节点异步通知类
     */
    static class IVoidCallback implements AsyncCallback.VoidCallback {
        public void processResult(int rc, String path, Object ctx) {
            System.out.println("async delete node result : [" + rc + ", " + path + ", " + ctx + "]");

        }
    }

    /**
     * 获取节点异步通知类
     */
    static class IChildrenCallback implements AsyncCallback.ChildrenCallback {
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            System.out.println("async children node result : [" + rc + ", " + path + ", " + ctx + ",children list : " + children + "]");

        }
    }

    /**
     * 获取节点data异步通知类
     */
    static class IDataCallBack implements AsyncCallback.DataCallback {
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            System.out.println("async get node" + path + " data : " + new String(data));
            System.out.println("async get node" + path + " stat : " + stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());
        }
    }


}
