package com.github.tjp.concurrent.atomic;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/12 上午10:31
 */
public class AtomicReferenceCase {

    private static int THREAD_NUM = 20;


    private static AtomicReference<Set<DateObj>> reference = new AtomicReference<>();

    private static AtomicReference<String> referenceStr = new AtomicReference<>();



    public static void write() {
        Set current;
        Set update;

        //自旋cas 原子更新对象set
        do {
            current = reference.get();
            if(current==null){
                update = new HashSet<>();
                update.add(new DateObj());
            }else{
                update = new HashSet<>();
                update.add(new DateObj());
            }

        } while (!reference.compareAndSet(current, update));

        System.out.println(Thread.currentThread() + "==> current : " + current + " , update : " + update);
    }

    public static void writeStr() {
        //自旋cas 原子更新对象set
        //compareAndSet进行原子更新
        String current = null;
        String update = null;
        do {
            current = referenceStr.get();
            if (current == null) {
                update = UUID.randomUUID().toString();
            } else {
                update = UUID.randomUUID().toString();
            }
        } while (!referenceStr.compareAndSet(current, update));

        System.out.println("reference : " + referenceStr + " current : " + current + " , update : " + update + " , reference : " + reference.get());
    }

    public static void main(String[] args) {
        Thread[] threads = new Thread[THREAD_NUM];

        for (int i = 0; i < THREAD_NUM; i++) {
            threads[i] = new Thread(new Producter());
        }
        for (int i = 0; i < THREAD_NUM; i++) {
            threads[i].start();
        }

    }

    private static class Producter implements Runnable {
        @Override
        public void run() {
            AtomicReferenceCase.writeStr();
        }
    }

    private static class DateObj {

        private String startTime;

        private String endTime;

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }


}
