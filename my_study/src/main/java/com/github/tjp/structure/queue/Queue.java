package com.github.tjp.structure.queue;

/**
 * 单向队列:一端入队 一端出队
 * Created by tujinpeng on 2017/9/29.
 */
public interface Queue<E> {

    /**
     * 入队 队头入队失败 抛异常
     *
     * @param e
     * @return
     */
    boolean add(E e);

    /**
     * 入队 对头入队失败,返回false
     *
     * @param e
     * @return
     */
    boolean offer(E e);

    /**
     * 出队 队尾出队失败抛异常
     *
     * @return
     */
    E remove();

    /**
     * 出队 队尾出队失败返回false
     *
     * @return
     */
    E poll();


    /**
     * 查看队首 队列为空抛异常
     *
     * @return
     */
    E element();

    /**
     * 查看队首 队列为空返回null
     *
     * @return
     */
    E peek();

}
