package com.tjp.structure;

/**
 * 单链表学习
 * Created by tujinpeng on 2017/9/18.
 */
public class LinkList {

    //链表头节点 遍历链表的开始
    private Node head;

    //链表头尾部点 链表插入的开始
    private Node tail;

    //链表大小
    private int size;

    public LinkList() {
    }

    /**
     * 添加新节点
     *
     * @param data
     */
    public void add(Object data) {
        Node node = new Node(data);
        //链表为空,直接赋值给头节点
        if (head == null) {
            head = node;
            tail = head;
        } else {//链表不为空,添加节点到tail节点后面
            //链表的尾部节点next指向新节点
            tail.next = node;
            //设置新的tail节点为当前节点
            tail = node;
        }
        //链表大小+1
        size++;
    }

    /**
     * 删除数据为data的节点
     *
     * @param data
     */
    public void remove(Object data) {
        Node curr = head;
        Node previous = head;
        //遍历链表,找出要删除的节点以及他的前驱节点
        while (curr != null) {
            if (curr.data.equals(data))
                break;
            previous = curr;
            curr = curr.next;
        }
        //若没有这样的节点
        if (curr == null)
            return;
        //若删除的是头节点,则重置head为原head的下一个节点
        if (curr == head)
            head = head.next;
        //若删除的是尾节点,则重置tail为原tail的上一个节点
        if (curr == tail)
            tail = previous;
        previous.next = curr.next;
        //删除节点设为null 让gc回收
        curr = null;
        //删除一个节点,链表大小-1
        size--;

    }

    /**
     * 获取链表长度
     *
     * @return
     */
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        Node curr = head;
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        while (curr != null) {
            if (curr != head) {
                sb.append("->");
            }
            sb.append(curr.data);
            curr = curr.next;
        }
        sb.append('}');

        return sb.toString();
    }

    class Node {
        private Object data;
        private Node next;

        public Node() {
        }

        public Node(Object data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    ", next=" + next +
                    '}';
        }
    }

    /**
     * 前提:不知道链表长度,不允许遍历链表的长度
     * 求链表倒数第k个节点
     *
     * @param k
     * @return
     */
    public Node findLastNode(int k) {
        if (k == 0 || head == null) {
            return null;
        }
        Node first = head;
        Node second = head;
        //1.先让second向前跑k-1个位置,使之间隔k-1
        for (int i = 0; i < k; k++) {
            second = second.next;
            if (second == null) {
                //k超过了链表长度,直接返回null
                return null;
            }
        }
        //2.两个下标同时跑,直到second跑到链表尾部
        while (second != null) {
            first = first.next;
            second = second.next;
        }
        //3.此时first节点就是倒数第k个位置
        return first;
    }


    public static void main(String[] args) {
        LinkList linkList = new LinkList();
        linkList.add(1);
        linkList.add(2);
        linkList.add(3);
        linkList.add(4);
        linkList.add(5);
        linkList.add(6);
        linkList.add(7);

        //删头部
        linkList.remove(1);
        //删尾部
        linkList.remove(7);
        linkList.remove(3);

        //求倒数第2个节点
        Node lastNode = linkList.findLastNode(2);
        System.out.println("last 2 node:" + lastNode);
        System.out.println(linkList);
    }


}
