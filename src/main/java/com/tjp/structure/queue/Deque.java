package com.tjp.structure.queue;

/**
 * 双端队列:
 * 两端都可以入队出队
 * Created by tujinpeng on 2017/9/29.
 */
public interface Deque<E> extends Queue<E>{
    /**
     * 头部入队 失败抛异常
     *
     * @param e
     * @return
     */
    void addFirst(E e);

    /**
     * 尾部入队 失败抛异常
     *
     * @param e
     * @return
     */
    void addLast(E e);

    /**
     * 头部入队 失败返回false
     *
     * @param e
     * @return
     */
    boolean offerFirst(E e);

    /**
     * 尾部入队 失败返回false
     *
     * @param e
     * @return
     */
    boolean offerLast(E e);

    /**
     * 头部出队 失败抛异常
     *
     * @return
     */
    E removeFirst();

    /**
     * 尾部出队 失败抛异常
     *
     * @return
     */
    E removeLast();

    /**
     * 头部出队 失败返回false
     *
     * @return
     */
    E pollFirst();

    /**
     * 尾部出队 失败返回false
     *
     * @return
     */
    E pollLast();


    /**
     * 获取队列头部 队列为空抛异常
     *
     * @return
     */
    E getFirst();

    /**
     * 获取队列尾部 队列为空抛异常
     * @return
     */
    E getLast();

    /**
     * 查看队首 队列为空返回null
     *
     * @return
     */
    E peekFirst();

    /**
     * 查看队尾 队列为空返回null
     *
     * @return
     */
    E peekLast();

    /**
     * queue大小
     * @return
     */
    public int size();


}
