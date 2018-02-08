package com.github.tjp.concurrent.blockqueue.delayqueue;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 模拟一个考试的日子，考试时间为120分钟，30分钟后才可交卷，当时间到了，或学生都交完卷了考试结束。
 * 分析:
 * 1.考试时间为120分钟，30分钟后才可交卷，初始化考生完成试卷时间最小应为30分钟
 * 2.对于能够在120分钟内交卷的考生，如何实现这些考生交卷
 * 3.对于120分钟内没有完成考试的考生，在120分钟考试时间到后需要让他们强制交卷
 * 4.在所有的考生都交完卷后，需要将控制线程关闭
 * Created by tujinpeng on 2017/3/28.
 */
public class ExamDelay {

    public static void main(String[] args) throws InterruptedException {
        //TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS)等价于TimeUnit.SECONDS.toNanos(1L)
//        System.out.println(TimeUnit.NANOSECONDS.convert(1, TimeUnit.SECONDS));
//        System.out.println(TimeUnit.SECONDS.toNanos(1L));
//        System.out.println(TimeUnit.NANOSECONDS.convert(130, TimeUnit.SECONDS) + System.nanoTime());
//        System.out.println(TimeUnit.NANOSECONDS.convert(120, TimeUnit.SECONDS) + System.nanoTime());

        System.out.println("开始考试.......");
        int studentNum = 10;
        DelayQueue<Student> queue = new DelayQueue<Student>();
        CountDownLatch countDownLatch = new CountDownLatch(studentNum + 1);//所有交卷完毕标志
        Random random = new Random();
        //1.初始化学生交卷时间,放入延时队列中,把交卷时间最小的放在队首,
        //注意:
        //  延时队列中元素不是绝对的从小到大排列,只在2的次方上保证顺序,并且保证队首最小(每次poll保证);
        //  每次offer时,都会把大的元素往后排;每次poll,都会把小的往前排。
        for (int i = 0; i < studentNum; i++) {
            //用毫秒模拟考试分钟数
            int stuWorkTime = 30 + random.nextInt(120);
            System.out.println(stuWorkTime);
            queue.put(new Student("student" + (i + 1), stuWorkTime, countDownLatch));
        }
        //2.初始化教师线程
        Thread teacherThread = new Thread(new Teacher(queue), "teacher-thread");
        //3.添加考试结束延时任务
        queue.put(new ExamEnd(queue, teacherThread, countDownLatch));
        //4.启动教师监控线程
        teacherThread.start();
        countDownLatch.await();//当所有学生交卷完毕,这里才结束等待返回
        System.out.println("考试结束.......");
    }


    /**
     * 学生任务线程.
     */
    static class Student implements Delayed, Runnable {
        //名称
        private String name;
        //考试耗时(单位s 模拟考试分钟数)
        private long workTime;
        //提交时间
        private long submitTime;
        //用来停止该线程的
        private boolean stop = false;
        //有一个学生交卷,这里countDownLatch.countDown(),释放一个阻塞状态
        private CountDownLatch countDownLatch;

        public Student(String name, long workTime, CountDownLatch countDownLatch) {
            this.name = name;
            this.workTime = workTime;
            //交卷时间=workTime+当前纳秒数
            this.submitTime = TimeUnit.NANOSECONDS.convert(workTime, TimeUnit.SECONDS) + System.nanoTime();
            this.countDownLatch = countDownLatch;
        }

        public long getDelay(TimeUnit unit) {
            return unit.convert(submitTime - System.nanoTime(), TimeUnit.NANOSECONDS);
        }

        public int compareTo(Delayed o) {
            Student that = (Student) o;
            if (this.submitTime > that.getSubmitTime()) {
                return 1;
            } else if (this.submitTime == that.getSubmitTime()) {
                return 0;
            } else {
                return -1;
            }
        }

        /**
         * 当学生到达交卷时间,执行下面run 交卷
         */
        public void run() {
            if (!stop) {
                //代表学生线程在120分钟内交了卷
                System.out.println(name + "交卷,预期用时:" + workTime + "分钟,实际用时:" + workTime + "分钟");
            } else {
                //代表考试时间已到120分钟,该student线程关闭
                System.out.println(name + "被强制交卷,预期用时:" + workTime + "分钟,实际用时:120分钟");
            }
            //有一个学生交卷,这里countDownLatch.countDown(),释放一个阻塞状态
            countDownLatch.countDown();
        }

        public boolean isStop() {
            return stop;
        }

        public void setStop(boolean stop) {
            this.stop = stop;
        }

        public long getSubmitTime() {
            return submitTime;
        }

        public void setSubmitTime(long submitTime) {
            this.submitTime = submitTime;
        }


    }

    /**
     * 老师线程,监控线程延时队列,负责收卷
     * 职责:不停地从学生的延时队列中,取出到期(到达每个学生的交卷时间)的学生线程,run
     */
    static class Teacher implements Runnable {
        private DelayQueue<Student> queue;

        public Teacher(DelayQueue<Student> queue) {
            this.queue = queue;
        }


        public void run() {
            try {
                //只要老师线程不中断,不停地去学生延时队列中尝试取到期的学生线程
                while (!Thread.interrupted()) {
                    queue.take().run();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 结束考试任务
     * 当到达120分钟时,自动触发此任务,强制关闭队列中还未交卷的学生,以及监控学生交卷的Teacher线程
     */
    static class ExamEnd extends Student {

        private DelayQueue<Student> queue;
        private Thread teacherThread;
        private CountDownLatch countDownLatch;

        public ExamEnd(DelayQueue<Student> queue, Thread teacherThread, CountDownLatch countDownLatch) {
            super("强制交卷", 120, countDownLatch);
            this.queue = queue;
            this.teacherThread = teacherThread;
            this.countDownLatch = countDownLatch;
        }

        public void run() {
            //1.考试时间到,先关闭老师线程,不在获取任务延时队列
            teacherThread.interrupt();
            //2.强制让延时队列中还未到交卷时间的学生,交卷
            Iterator<Student> iterator = queue.iterator();
            while (iterator.hasNext()) {
                Student stu = iterator.next();
                stu.setStop(true);
                stu.run();
            }
            countDownLatch.countDown();
        }


    }


}
