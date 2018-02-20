package com.github.tjp.structure.tree;

import java.util.List;

/**
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/15 下午7:54
 */
public interface Tree<T extends Comparable> {

    /**
     * 插入
     *
     * @param value
     */
    void insert(T value);

    /**
     * 查找
     *
     * @param value
     * @return
     */
    Node findKey(T value);

    /**
     * 删除
     *
     * @param value
     */
    boolean delete(T value);

    /**
     * 非递归前序遍历
     *
     * @return
     */
    List<T> preOrder();

    /**
     * 非递归中序遍历
     *
     * @return
     */
    List<T> inOrder();

    /**
     * 非递归后序遍历
     *
     * @return
     */
    List<T> postOrder();

    /**
     * 递归前序遍历
     *
     * @return
     */
    List<T> preOrderRecursive();

    /**
     * 递归中序遍历
     *
     * @return
     */
    List<T> inOrderRecursive();

    /**
     * 递归后序遍历
     *
     * @return
     */
    List<T> postOrderRecursive();


    /**
     * 获取二叉树最大值
     *
     * @return
     */
    T first();

    /**
     * 获取二叉树最小值
     *
     * @return
     */
    T last();

    /**
     * 按层遍历
     */
    void levelOrder();

    /**
     * 求最小深度
     *
     * @return
     */
    int minDepth();

    /**
     * 最大深度
     *
     * @return
     */
    int maxDepth();
}
