package com.github.tjp.structure.stack;

/**
 * 栈
 * Created by tujinpeng on 2017/9/27.
 */
public interface Stack<E> {

    /**
     * 入栈
     *
     * @param data
     * @return
     */
    E push(E data);

    /**
     * 出栈
     *
     * @return
     */
    E pop();

    /**
     * 查看栈顶元素
     *
     * @return
     */
    E peek();

    /**
     * 栈大小
     *
     * @return
     */
    int size();

    /**
     * 栈是否为空
     *
     * @return
     */
    boolean isEmpty();

}
