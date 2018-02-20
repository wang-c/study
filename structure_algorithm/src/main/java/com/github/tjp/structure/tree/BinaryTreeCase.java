package com.github.tjp.structure.tree;

import java.util.List;

/**
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/16 下午12:20
 */
public class BinaryTreeCase {

    public static void main(String[] args) {
        //构造二叉树
        Tree<Integer> tree = new BinaryTree<>();
        tree.insert(20);
        tree.insert(10);
        tree.insert(9);
        tree.insert(12);
        tree.insert(40);
        tree.insert(21);
        tree.insert(50);
        tree.insert(100);

        //按层遍历节点
        System.out.println("按层遍历节点：");
        tree.levelOrder();
        System.out.println("---------------------");

        //最大深度
        System.out.println("最大深度：");
        System.out.println(tree.maxDepth());
        System.out.println("---------------------");

        //最小深度
        System.out.println("最小深度：");
        System.out.println(tree.minDepth());
        System.out.println("---------------------");

        //先序遍历
        System.out.println("先序遍历（非递归）：");
        List<Integer> preList = tree.preOrder();
        for (Integer e : preList) {
            System.out.print(e + " ");
        }
        System.out.println();
        System.out.println("先序遍历（递归）：");
        List<Integer> preRecusiveList = tree.preOrderRecursive();
        for (Integer e : preRecusiveList) {
            System.out.print(e + " ");
        }
        System.out.println();
        System.out.println("----------------------");


        //中序遍历(有序)
        System.out.println("中序(有序)遍历（非递归）：");
        List<Integer> inList = tree.inOrder();
        for (Integer e : inList) {
            System.out.print(e + " ");
        }
        System.out.println();
        System.out.println("中序遍历（递归）：");
        List<Integer> inRecusiveList = tree.inOrderRecursive();
        for (Integer e : inRecusiveList) {
            System.out.print(e + " ");
        }
        System.out.println();
        System.out.println("---------------------");

        //后序遍历
        System.out.println("后序遍历（非递归）：");
        List<Integer> postList = tree.postOrder();
        for (Integer e : postList) {
            System.out.print(e + " ");
        }
        System.out.println();
        System.out.println("后序遍历（递归）：");
        List<Integer> postRecusiveList = tree.postOrderRecursive();
        for (Integer e : postRecusiveList) {
            System.out.print(e + " ");
        }
        System.out.println();
        System.out.println("---------------------");


        //获取树中最小值
        System.out.println("树中最小值：");
        System.out.println(tree.first());
        System.out.println("树中最大值：");
        System.out.println(tree.last());
        System.out.println("---------------------");

        //查找
        System.out.println("查找树中value值是50的节点：");
        System.out.println(tree.findKey(50));
        System.out.println("---------------------");


        //删除40
        System.out.println("删除树中value值是40的节点：");
        System.out.println(tree.delete(20));
        System.out.println("删除后的二叉树：");
        List<Integer> listAfterDel = tree.inOrderRecursive();
        for (Integer e : listAfterDel) {
            System.out.print(e + " ");
        }
        System.out.println();
        System.out.println("---------------------");

    }
}
