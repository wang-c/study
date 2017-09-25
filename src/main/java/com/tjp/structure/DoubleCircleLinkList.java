package com.tjp.structure;

/**
 * 双向循环链表
 * Created by TJP on 2017/9/25.
 */
public class DoubleCircleLinkList<E> {

    //头节点(数据域为空，next指向链表第一个节点，prev指向尾节点)
    private Node<E> head;

    //链表大小
    private int size;


    public DoubleCircleLinkList() {
        head = new Node<E>(null, null, null);
        //初始化链表 头尾都指向head节点
        head.next = head.prev = head;
    }

    /**
     * 添加新节点(默认添加到末尾)
     *
     * @param data
     */
    public void add(E data) {
        linkLast(data);
    }

    /**
     * 添加新节点到链表尾部(FIFO 队列):
     *
     * @param data
     */
    public void linkLast(E data) {
        //此时链表的尾部=head.prev
        //新加的节点newNode要放入链表尾部,形成: head⇄node1⇄node2⇄head.prev(老链表尾部)⇄newNode(新链表尾部)
        //所以設置newNode.prev=head.prev,newNode.next=head
        Node<E> newNode = new Node<E>(head.prev, data, head);
        //old尾节点的next指向新节点
        head.prev.next = newNode;
        //新的尾部head.prev要指向新节点newNode
        head.prev = newNode;
        //链表大小+1
        size++;
    }

    private boolean isEmpty() {
        return size == 0 || head.next == head;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        int index = 0;
        Node curr = head.next;
        //判断循环链表结束条件是:cur!=head
        while (curr != head) {
            if (index != 0) {
                sb.append("⇄");
            }
            sb.append(curr.data);
            curr = curr.next;
            index++;
        }
        sb.append('}');

        return sb.toString();
    }

    private class Node<E> {
        private E data;
        //前一个节点引用
        private Node prev;
        //后一个节点引用
        private Node next;

        public Node(Node prev, E data, Node next) {
            this.prev = prev;
            this.next = next;
            this.data = data;
        }
    }

    public static void main(String[] args) {
        DoubleCircleLinkList<Object> list1 = new DoubleCircleLinkList<Object>();
        list1.add(7);
        list1.add(2);
        list1.add(3);
        list1.add(1);
        list1.add(5);
        list1.add(6);
        list1.add(4);
        System.out.println("Double circle linked list :" + list1);

    }
}
