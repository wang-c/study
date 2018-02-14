package com.github.tjp.concurrent.blockqueue.delayqueue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * 应用场景:消息延迟1秒发送,如果用调度去查询执行的话，会有误差
 * Created by tujinpeng on 2017/3/27.
 */
public class MessageDelay {

    public static void main(String[] args) throws InterruptedException {
        DelayQueue<Message> delayMessageQueue = new DelayQueue<Message>();
        //初始化延时队列,每个message,分别延时1s,2s,3s.....10s
        for (long i = 0; i < 10; i++) {
            Message message = new Message(i, System.nanoTime() + SECONDS.toNanos(i));
            delayMessageQueue.add(message);
        }
        //循环取出过期message
        while (!delayMessageQueue.isEmpty()) {
            Message message = delayMessageQueue.take();
            System.out.println("message id : " + message.getObjectId());
        }
        //最终效果
    }

    static class Message implements Delayed {
        private Long objectId;
        private String objectType;
        private String eventType;
        /**
         * 消息创建时间
         */
        private Long createTime;

        public Message(Long objectId, Long createTime) {
            this.objectId = objectId;
            this.createTime = createTime;
        }


        public long getDelay(TimeUnit unit) {
            return unit.convert(createTime + SECONDS.toNanos(1L) - System.nanoTime(), unit);//纳秒为计算单位,延时1s
        }

        public int compareTo(Delayed o) {
            //比较 1是放入队尾  -1是放入队头
            Message that = (Message) o;
            if (this.createTime > that.getCreateTime()) {
                return 1;
            } else if (this.createTime == that.getCreateTime()) {
                return 0;
            } else {
                return -1;
            }
        }

        public Long getObjectId() {
            return objectId;
        }

        public void setObjectId(Long objectId) {
            this.objectId = objectId;
        }

        public String getObjectType() {
            return objectType;
        }

        public void setObjectType(String objectType) {
            this.objectType = objectType;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }
    }


}
