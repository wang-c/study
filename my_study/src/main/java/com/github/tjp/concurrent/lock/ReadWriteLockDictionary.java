package com.github.tjp.concurrent.lock;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by TJP on 2017/11/2.
 */
public class ReadWriteLockDictionary {

    /**
     * 非线程安全的有序集合
     */
    private final Map<String, Object> data = new TreeMap<String, Object>();

    /**
     * 读写锁 保证data的并发读写线程安全
     */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();

    private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    /**
     * get操作要先获取到读锁，获取到读锁后，其他并发访问的获取读锁的线程不会被阻塞，但是put操作获取写锁的线程会被阻塞
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        readLock.lock();
        try {
            return data.get(key);
        } finally {
            //一定要在try的final块里释放锁 否则异常将导致无法释放锁
            readLock.unlock();
        }
    }

    /**
     * put操作需要先获取写锁，当获取到写锁后，其他线程对于写锁和读锁的获取将被阻塞
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        writeLock.lock();
        try {
            data.put(key, value);
        } finally {
            //一定要在try的final块里释放锁 否则异常将导致无法释放锁
            writeLock.unlock();
        }
    }

    public void clear() {
        writeLock.lock();
        try {
            data.clear();
        } finally {
            writeLock.lock();
        }
    }

    public static void main(String[] args) {
        final ReadWriteLockDictionary store = new ReadWriteLockDictionary();

        Thread writeThread = new Thread(new Runnable() {
            public void run() {
                store.put("key1", "value1");
            }
        }, "store-write-thread-01");


        Thread readThread = new Thread(new Runnable() {
            public void run() {
                //读
                Object value = store.get("key1");
                System.out.println(value);
            }
        }, "store-read-thread-01");
    }


}
