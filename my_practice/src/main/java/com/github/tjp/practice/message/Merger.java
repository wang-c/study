package com.github.tjp.practice.message;

/**
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/11 上午11:36
 */
public interface Merger {

    /**
     * 搜集消息,做消息合并
     *
     * @param message
     */
    void collect(Message message);


    /**
     * 发送合并好的消息
     */
    void send();
}
