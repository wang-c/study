package com.github.tjp.structure.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.jar.Pack200;

/**
 * <pre>
 * 一、二叉树:
 *  每个节点最多两个子节点的树结构
 *
 * 二、性质:
 *  (1)在二叉树的第i层上,最多有2的i-1次方个节点
 *  (2)深度为k的二叉树至多有(2的k次方-1)个节点
 *  (3)在完全二叉树中,有n个节点的完全二叉树其深度是log(n+1)
 *  (4)在任意一颗二叉树中,终端叶子节点个数为n0,度数为2的节点个数为n2,则n0=n2+1
 *
 * 三、操作:
 * (1)插入
 * (2)删除
 * (3)查找
 * (4)前序,中序,后序遍历
 *
 * 四、时间复杂度:
 * 插入 查找 log(n)
 *
 *
 * </pre>
 *
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/14 下午11:28
 */
public class BinaryTree<T extends Comparable> implements Tree<T> {

    private BinaryNode<T> root;

    //二叉树节点
    private class BinaryNode<T> implements Node<T> {

        private T value;

        private BinaryNode<T> left;

        private BinaryNode<T> right;

        public BinaryNode(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "BinaryNode{" +
                    "value=" + value +
                    '}';
        }
    }

    @Override
    public void insert(T value) {
        BinaryNode newNode = new BinaryNode(value);

        if (value == null) {
            return;
        }
        //空二叉树,新节点作为root
        if (root == null) {
            root = newNode;
            return;
        }

        BinaryNode curr = root;
        BinaryNode parent = null;
        while (true) {
            if (value.equals(curr.value)) {
                //树中已经存在
                return;
            }
            parent = curr;
            if (value.compareTo(curr.value) < 0) {//小,迭代到左子树
                curr = curr.left;
                if (curr == null) {//找到最终的叶子节点,插入到左面
                    parent.left = newNode;
                    return;
                }
            } else {//大,向右子树查找
                curr = curr.right;
                if (curr == null) {//找到最终的叶子节点,插入到右面
                    parent.right = newNode;
                    return;
                }
            }
        }

    }

    @Override
    public Node findKey(T value) {
        if (root == null || value == null) {
            return null;
        }
        BinaryNode<T> curr = root;
        //遍历二叉树,直到叶子节点
        while (curr != null) {
            if (value.equals(curr.value)) {//找到了
                return curr;
            }
            if (value.compareTo(curr.value) < 0) {//小,向左子树找
                curr = curr.left;
            } else {//大,向右子树找
                curr = curr.right;
            }
        }
        return null;
    }

    @Override
    public boolean delete(T value) {
        if (root == null) {//树未初始化
            return false;
        }
        BinaryNode<T> curr = root;
        BinaryNode<T> parentNode = null;//要删除节点的父节点引用
        BinaryNode<T> delNode = null;//要删除节点引用
        boolean isLeftChild = false;//删除的节点是它父节点的左孩子
        //遍历二叉树,找到删除节点,以及它的父节点
        while (curr != null) {
            if (value.equals(curr.value)) {//找到了删除节点
                break;
            }
            parentNode = curr;
            if (value.compareTo(curr.value) < 0) {//小,向左子树找
                isLeftChild = true;
                curr = curr.left;
            } else {//大,向右子树找
                isLeftChild = false;
                curr = curr.right;
            }
        }

        if (curr == null) {//遍历完树,没有找到要删除的节点位置,直接返回
            return false;
        }
        //设置要删除的节点引用
        delNode = curr;

        //找到删除节点位置,进行删除
        //分情况
        //1.当删除节点有两个节点时,将后继节点(右子树中最小的节点)和删除节点的值交换下,转化为删除后继节点(只有一个子节点或者没有子节点)
        if (delNode.left != null && delNode.right != null) {
            //找到真正要删除的后继节点
            BinaryNode<T> successor = delNode.right;
            isLeftChild = false;//重置isLeftChild,一开始默认删除节点是父节点右孩子
            parentNode = delNode;//父节点重新赋值
            while (successor.left != null) {//从右子树中不断向左遍历,找到后继
                isLeftChild = true;
                parentNode = successor;
                successor = successor.left;
            }
            //交换删除节点和后继节点的value
            swapValue(delNode, successor);
            //转化为删除后继节点
            delNode = successor;
        }
        //删除节点是根节点,特殊处理
        if (delNode == root) {
            if (delNode.right != null) {
                root = delNode.right;
            } else if (delNode.left != null) {
                root = delNode.left;
            } else {
                root = null;
            }
        }
        //2.当删除节点只有一个节点,直接将删除节点的父节点孩子指向删除节点的左节点或者右节点
        if (delNode.left != null || delNode.right != null) {
            if (delNode.left != null) {//只有左孩子
                if (isLeftChild)
                    parentNode.left = delNode.left;
                else
                    parentNode.right = delNode.left;
            } else {//只有右孩子
                if (isLeftChild)
                    parentNode.left = delNode.right;
                else
                    parentNode.right = delNode.right;
            }
            delNode = null;//help gc
        }
        //3.当删除节点为叶子节点,直接删除,设置父节点孩子引用为null
        else {
            if (isLeftChild)
                parentNode.left = null;
            else
                parentNode.right = null;

        }

        return true;

    }

    private void swapValue(BinaryNode<T> source, BinaryNode<T> target) {
        T temp = source.value;
        source.value = target.value;
        target.value = temp;
    }

    //查找二叉树节点的后继节点(右子树中值最小的节点)
    private BinaryNode successor(BinaryNode<T> node) {
        BinaryNode<T> curr = node.right;
        if (node == null || curr == null)
            return null;
        //从右子树中不断向左遍历,找到后继
        while (curr.left != null) {
            curr = curr.left;
        }
        return curr;
    }


    @Override
    public List<T> preOrder() {
        if (root == null) {
            return null;
        }
        List<T> nodeList = new ArrayList<>();
        BinaryNode<T> curr = root;
        Stack<BinaryNode> stack = new Stack<>();//标记遍历的位置
        while (curr != null || !stack.isEmpty()) {
            if (curr != null) {
                stack.push(curr);
                //先遍历根部
                nodeList.add(curr.value);
                //再遍历左边
                curr = curr.left;
            } else {
                //左子树没了,开始遍历右边
                curr = stack.pop();
                curr = curr.right;
            }
        }

        return nodeList;
    }

    @Override
    public List<T> inOrder() {
        if (root == null) {
            return null;
        }
        List<T> valueList = new ArrayList<>();
        BinaryNode<T> curr = root;
        Stack<BinaryNode> stack = new Stack<>();//标记遍历的位置
        while (curr != null || !stack.isEmpty()) {
            if (curr != null) {
                stack.push(curr);
                //先遍历左边
                curr = curr.left;
            } else {
                /*
                 * 当前节点为空：
                 *  1、当p指向的左儿子时，此时栈顶元素必然是它的父节点
                 *  2、当p指向的右儿子时，此时栈顶元素必然是它的爷爷节点
                 * 取出并访问栈顶元素，赋值为right
                 */
                curr = stack.pop();
                //再遍历根部
                valueList.add(curr.value);
                //最后遍历右部
                curr = curr.right;
            }
        }
        return valueList;
    }

    @Override
    public List<T> postOrder() {
        if (root == null) {
            return null;
        }
        List<T> valueList = new ArrayList<>();
        Stack<BinaryNode<T>> stack = new Stack();//遍历的stack
        Stack<BinaryNode<T>> outPutStack = new Stack();//存放逆后序遍历顺序的结果

        BinaryNode<T> curr = root;
        while (curr != null || !stack.isEmpty()) {
            if (curr != null) {
                stack.push(curr);
                outPutStack.push(curr);
                //遍历根节点,右面节点
                curr = curr.right;
            } else {
                curr = stack.pop();
                //在访问左节点
                curr = curr.left;
            }
        }
        //入栈顺序-》根节点,右节点,左节点    出栈顺序-》左节点,右节点,根节点
        while (!outPutStack.isEmpty()) {
            valueList.add(outPutStack.pop().value);
        }
        return valueList;
    }

    @Override
    public List<T> preOrderRecursive() {
        if (root == null) {
            return null;
        }
        List<T> valueList = new ArrayList<>();
        preOrderRecursive0(root, valueList);
        return valueList;
    }

    private void preOrderRecursive0(BinaryNode<T> node, List<T> valueList) {
        if (node == null) {//注意节点为空的情况
            return;
        }
        valueList.add(node.value);
        preOrderRecursive0(node.left, valueList);
        preOrderRecursive0(node.right, valueList);
    }

    @Override
    public List<T> inOrderRecursive() {
        if (root == null) {
            return null;
        }
        List<T> valueList = new ArrayList<>();
        inOrderRecursive0(root, valueList);
        return valueList;
    }

    private void inOrderRecursive0(BinaryNode<T> node, List<T> valueList) {
        if (node == null) {//注意节点为空的情况
            return;
        }
        inOrderRecursive0(node.left, valueList);
        valueList.add(node.value);
        inOrderRecursive0(node.right, valueList);
    }

    @Override
    public List<T> postOrderRecursive() {
        if (root == null) {
            return null;
        }
        List<T> valueList = new ArrayList<>();
        postOrderRecursive0(root, valueList);
        return valueList;
    }

    private void postOrderRecursive0(BinaryNode<T> node, List<T> valueList) {
        if (node == null) {//注意节点为空的情况
            return;
        }
        postOrderRecursive0(node.left, valueList);
        postOrderRecursive0(node.right, valueList);
        valueList.add(node.value);
    }

    @Override
    public T first() {
        if (root == null) {
            return null;
        }
        //从二叉树根节点,依次向左遍历,直到左孩子为null,找到树种最小值
        BinaryNode<T> curr = root;
        while (curr.left != null) {
            curr = curr.left;
        }
        return curr.value;
    }

    @Override
    public T last() {
        if (root == null) {
            return null;
        }
        //从二叉树根节点,依次向左遍历,直到左孩子为null,找到树种最大值
        BinaryNode<T> curr = root;
        while (curr.right != null) {
            curr = curr.right;
        }
        return curr.value;
    }
}
