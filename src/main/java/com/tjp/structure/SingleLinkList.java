package com.tjp.structure;

import java.util.Stack;

/**
 * 单链表学习
 * Created by tujinpeng on 2017/9/18.
 */
public class SingleLinkList {

    //链表头节点 遍历链表的开始
    private Node head;

    //链表头尾部点 链表插入的开始
    private Node tail;

    //链表大小
    private int size;

    public SingleLinkList() {
    }

    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public Node getTail() {
        return tail;
    }

    public void setTail(Node tail) {
        this.tail = tail;
    }

    /**
     * 链表为空
     *
     * @return
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * 添加新节点(默认添加到末尾)
     *
     * @param data
     */
    public void add(Object data) {
        linkLast(data);
    }

    /**
     * 添加新节点到链表头部(FILO 栈)
     *
     * @param data
     */
    public void linkFirst(Object data) {
        Node node = new Node(data);
        //链表为空,直接赋值给头节点
        if (isEmpty()) {
            head = node;
            tail = head;
        } else {//链表不为空,添加节点到head节点前面
            //新节点next指向head及诶单
            node.next = head;
            //设置新的head节点为当前节点
            head = node;
        }
        //链表大小+1
        size++;
    }

    /**
     * 添加新节点到链表尾部(FIFO 队列)
     *
     * @param data
     */
    public void linkLast(Object data) {
        Node node = new Node(data);
        //链表为空,直接赋值给头节点
        if (isEmpty()) {
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
     * 添加新节点
     *
     * @param node
     */
    public void add(Node node) {
        if (node == null) {
            return;
        }
        if (head == null) {
            head = node;
            tail = head;
        } else {
            tail.next = node;
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
        //1.遍历链表,找出要删除的节点以及他的前驱节点
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
        //2.让删除节点的上一个节点next指向删除节点的next
        previous.next = curr.next;
        //3.删除节点设为null 让gc回收
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

    private class Node<E> {
        private E data;
        private Node next;

        public Node() {
        }

        public Node(E data) {
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
     * 求链表倒数第k个节点(快慢指针)
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
        for (int i = 0; i < k; i++) {
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

    /**
     * 查找链表中间节点
     *
     * @return
     */
    public Node findMidNode() {
        if (head == null) {
            return null;
        }
        Node first = head;
        Node second = head;
        //1.两个下标同时跑,first下标每次移动一位,second下标每次移动两位
        while (second != null && second.next != null) {
            first = first.next;
            second = second.next.next;
        }
        //2.直到second下标到末尾时,此时first节点就是中间位置
        return first;
    }

    /**
     * 合并两个有序的单链表，合并之后的链表依然有序：
     * 链表1：
     * 　　1->2->3->4
     * 链表2：
     * 　　2->3->4->5
     * 合并后：
     * 　　1->2->2->3->3->4->4->5
     * 解题思路：
     * 　　挨着比较链表1和链表2。
     * 　　这个类似于归并排序。尤其要注意两个链表都为空、和其中一个为空的情况。只需要O (1) 的空间。时间复杂度为O (max(len1,len2))
     *
     * @return
     */
    public static SingleLinkList mergeSortLink(SingleLinkList list1, SingleLinkList list2) {
        if (list1 == null || list1.isEmpty()) {
            return list2;
        }
        if (list2 == null || list2.isEmpty()) {
            return list1;
        }
        SingleLinkList newList = new SingleLinkList();
        Node head1 = list1.getHead();
        Node head2 = list2.getHead();
        //不停的遍历比较出两个链表最小的节点,有序加入新链表中
        while (head1 != null && head2 != null) {//有一个链表为空,结束遍历
            Object min = null;
            if (((Comparable) head1.data).compareTo(head2.data) <= 0) {
                min = head1.data;
                head1 = head1.next;//比较小的链表head指针向前推进
            } else {
                min = head2.data;
                head2 = head2.next;//比较小的链表head指针向前推进
            }
            //将每次比较出来最小的节点加入到新节点中
            newList.add(min);
        }
        //合并剩余的元素
        while (head1 != null) {//head1有剩余
            newList.add(head1.data);
            head1 = head1.next;
        }
        while (head2 != null) {//head2有剩余
            newList.add(head2.data);
            head2 = head2.next;
        }
        return newList;
    }

    /**
     * 单链表的反转
     *
     * @return
     */
    public static SingleLinkList reverseList(SingleLinkList linkList) {
        if (linkList.isEmpty()) {
            return null;
        }
        //1.新建一个链表
        SingleLinkList reverseLink = new SingleLinkList();
        //2.不停的遍历旧的链表,将数据加入到新链表头部(倒序)
        Node head = linkList.getHead();
        while (head != null) {
            reverseLink.linkFirst(head.data);
            head = head.next;
        }
        return reverseLink;
    }

    /**
     * 从头到尾打印链表
     */
    public static void reversePrint(SingleLinkList linkList) {
        if (linkList.isEmpty()) {
            return;
        }
        Node head = linkList.getHead();
        Stack stack = new Stack();
        while (head != null) {
            stack.push(head.data);
            head = head.next;
        }

        while (stack.size() > 0) {
            System.out.println(stack.pop());
        }

    }

    //判断链表是否有环
    //链表环长度
    //链表环的起始点


    public static void main(String[] args) {
        SingleLinkList list1 = new SingleLinkList();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);
        list1.add(5);
        list1.add(6);
        list1.add(7);

        //删头部
        list1.remove(1);
        //删尾部
        list1.remove(7);

        //求倒数第2个节点
        Node lastNode = list1.findLastNode(2);
        System.out.println("last 2 node:" + lastNode);
        //求中间节点
        Node midNode = list1.findMidNode();
        System.out.println("mid node:" + midNode);
        System.out.println("list1:" + list1);

        //求合并两个有序列表,组成新的有序列表
        SingleLinkList list2 = new SingleLinkList();
        list2.add(3);
        list2.add(4);
        list2.add(8);
        list2.add(9);
        list2.add(10);
        System.out.println("list2:" + list2);
        SingleLinkList mergeList = SingleLinkList.mergeSortLink(list1, list2);
        System.out.println("merge sort list:" + mergeList);

        // 链表反转
        SingleLinkList reverseList = SingleLinkList.reverseList(list1);
        System.out.println("reverseList : " + reverseList);
        SingleLinkList.reversePrint(reverseList);

    }


}
