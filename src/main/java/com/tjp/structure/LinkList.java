package com.tjp.structure;

/**
 * 链表基本操作
 * Created by tujinpeng on 2017/9/26.
 */
public interface LinkList<E> {

    /**
     * 向链表添加元素(默认添加到尾部)
     * @param data
     */
    void add(E data);

    /**
     * 添加节点到链表头部
     * @param data
     */
    void addFirst(E data);

    /**
     * 删除链表头部节点
     */
    void remove();

    /**
     * 删除链表尾部节点
     */
    void removeLast();


    /**
     * 删除指定的data域节点
     * @param data
     */
    void remove(E data);


    /**
     * 判断链表是否为空
     * @return
     */
    boolean isEmpty();

    /**
     * 获取链表长度
     * @return
     */
    int size();


}
