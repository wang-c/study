package com.tjp.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 建立zookeeper会话
 * Created by tujinpeng on 2017/4/15.
 */
public class ZookeeperClient {

    private static String url = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        //1.第一次连接会话,客户端将收到SyncConnected通知
        ZooKeeper zooKeeper = new ZooKeeper(url, 5000, new SimpleWatch());
        //上面建立好会话,此时会话的状态是CONNECTING,只有等到服务器端发送一个SyncConnected事件通知,才算真正建立建立会话
        System.out.println(zooKeeper.getState());
        //阻塞等待服务器端发来的SyncConnected事件通知
        countDownLatch.await();

        long sessionId = zooKeeper.getSessionId();
        byte[] passwd = zooKeeper.getSessionPasswd();
        System.out.println("sessionId : " + sessionId + " , passwd : " + passwd);

        //2.第二次利用错误的sessionId,passwd建立连接,客户端将收到Expired通知
        zooKeeper = new ZooKeeper(url, 5000, new SimpleWatch(), 1l, "test".getBytes());

        //3.第三次利用错误的sessionId,passwd,复用建立连接,客户端将收到SyncConnected通知
        zooKeeper = new ZooKeeper(url, 5000, new SimpleWatch(), sessionId, passwd);

    }

    private static class SimpleWatch implements Watcher {

        public void process(WatchedEvent watchedEvent) {
            System.out.println("Revice watch event : " + watchedEvent);
            if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                countDownLatch.countDown();
            }

        }
    }


}
