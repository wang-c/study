package com.tjp.concurrent.blockqueue;

import java.util.concurrent.*;

/**
 * 对比阻塞队列LinkedBlockingQueue ArrayBlockingQueue SynchronousQueue 吞吐量:
 * 吞吐量=单位时间内处理任务的数量
 * <p/>
 * Created by tujinpeng on 2017/12/16.
 */
public class BlockingQueueComparison {
    private static ExecutorService executor;

    //相同的任务数
    private static final int TASK_NUM = 2000000;

    //相同的队列大小
    private static final int QUEUE_SIZE = 100;

    //不断递增的生产者消费者线程
    private static int THREAD_NUM;


    public static void main(String[] args) throws Exception {
        System.out.println("Producer\tConsumer\tcapacity \t LinkedBlockingQueue \t ArrayBlockingQueue \t SynchronousQueue");

        /*
         * 测试场景:
         * 看不同的阻塞队列在固定的的生产者消费者线程数面前,去处理同样大小的任务,看它的消耗时间,算出它的吞吐量
         * 不断扩大生产者消费者竞争,增加线程数,对比吞吐量
         *
         * 结论:
         *   高并发下,LinkedBlockingQueue的吞吐量要高于ArrayBlockingQueue
         *
         */
        for (int j = 0; j < 10; j++) {
            THREAD_NUM = (int) Math.pow(2, j);
            executor = Executors.newFixedThreadPool(THREAD_NUM * 2);

            System.out.print(THREAD_NUM + "\t\t\t\t");
            System.out.print(THREAD_NUM + "\t\t\t");
            System.out.print(QUEUE_SIZE + "\t\t\t");
            System.out.print(doTestBlockingQueue(new LinkedBlockingQueue<Integer>(QUEUE_SIZE), TASK_NUM) + "/s\t\t\t\t");
            System.out.print(doTestBlockingQueue(new ArrayBlockingQueue<Integer>(QUEUE_SIZE), TASK_NUM) + "/s\t\t\t\t");
            System.out.print(doTestBlockingQueue(new SynchronousQueue<Integer>(), TASK_NUM) + "/s");
            System.out.println();

            executor.shutdown();
        }
    }

    private static long doTestBlockingQueue(final BlockingQueue<Integer> q, final int n)
            throws Exception {
        //存放结果完成的service
        CompletionService<Long> completionService = new ExecutorCompletionService<Long>(executor);
        long t = System.nanoTime();
        //启动生产者线程
        for (int i = 0; i < THREAD_NUM; i++) {
            executor.submit(new Producer(n / THREAD_NUM, q));
        }
        //启动消费者线程
        for (int i = 0; i < THREAD_NUM; i++) {
            completionService.submit(new Consumer(n / THREAD_NUM, q));
        }

        //等待所有消费者线程消费完所有任务
        for (int i = 0; i < THREAD_NUM; i++) {
            completionService.take();//消费者线程执行完毕,会将结果future有序地加入到ExecutorCompletionService的阻塞队列中
        }

        t = System.nanoTime() - t;
        //计算吞吐量
        return (long) (1000000000.0 * TASK_NUM / t); // Throughput, items/sec
    }

    private static class Producer implements Runnable {
        int taskNum;
        BlockingQueue<Integer> queue;

        public Producer(int initN, BlockingQueue<Integer> initQ) {
            taskNum = initN;
            queue = initQ;
        }

        public void run() {
            for (int i = 0; i < taskNum; i++)
                try {
                    queue.put(i);
                } catch (InterruptedException ex) {
                }
        }
    }

    private static class Consumer implements Callable<Long> {
        int taskNum;
        BlockingQueue<Integer> queue;

        public Consumer(int initN, BlockingQueue<Integer> initQ) {
            taskNum = initN;
            queue = initQ;
        }

        public Long call() {
            long sum = 0;
            for (int i = 0; i < taskNum; i++)
                try {
                    sum += queue.take();
                } catch (InterruptedException ex) {
                }
            return sum;
        }
    }
}
