package com.tjp.concurrent.lock;

/**
 * Created by TJP on 2017/11/2.
 */
public class ReadWriteLockExample {

    private static final ReadWriteLockDictionary store = new ReadWriteLockDictionary();

    public static void main(String[] args) {

        //写线程 当写操作时其他后续的读操作阻塞
        Thread writeThread = new Thread(new Runnable() {
            public void run() {
                store.put("key1", "value1");
            }
        }, "store-write-thread-01");


        //读线程1 当读操作时后续的写线程阻塞 读线程不阻塞
        Thread readThread1 = new Thread(new Runnable() {
            public void run() {
                //读
                Object value = store.get("key1");
                System.out.println(value);
            }
        }, "store-read-thread-01");

        //读线程2
        Thread readThread2 = new Thread(new Runnable() {
            public void run() {
                //读
                Object value = store.get("key1");
                System.out.println(value);
            }
        }, "store-read-thread-02");

        writeThread.start();
        readThread1.start();
        readThread2.start();
    }


}
