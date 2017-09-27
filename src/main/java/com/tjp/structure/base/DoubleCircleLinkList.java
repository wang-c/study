package com.tjp.structure.base;

/**
 * 双向循环链表
 * Created by TJP on 2017/9/25.
 */
public class DoubleCircleLinkList<E> implements LinkList<E>{

    //头节点(数据域为空，next指向链表第一个节点，prev指向尾节点)
    private Node<E> head;

    //链表大小
    private int size;


    public DoubleCircleLinkList() {
        head = new Node<E>(null, null, null);
        //初始化链表 头尾都指向head节点
        head.next = head.prev = head;
    }

    @Override
    public E getFirst() {
        if(isEmpty()){
            return null;
        }
        return head.next.data;
    }

    /**
     * 添加新节点(默认添加到末尾)
     *
     * @param data
     */
    @Override
    public void add(E data) {
        linkLast(data);
    }

    /**
     * 添加新节点到头部
     *
     * @param data
     */
    @Override
    public void addFirst(E data) {
        linkFirst(data);
    }

    @Override
    public void addLast(E data) {
        linkLast(data);
    }


    /**
     * 删除链表第一个节点(head.next)
     */
    @Override
    public E remove(){
        return unLinkFirst();
    }

    @Override
    public E removeFirst() {
        return unLinkFirst();
    }

    /**
     * 删除链表第一个节点(head.next)
     */
    @Override
    public E removeLast(){
        return unLinkLast();
    }


    /**
     * 删除指定数据的节点
     * @param data
     */
    @Override
    public void remove(E data) {
        if (isEmpty() || data == null) {
            return;
        }
        //1.遍历找到指定data的节点
        Node<E> curr = head.next;//遍历从first节点开始
        //循环链表直到head节点,遍历结束
        while (curr != head) {
            //找到data节点结束
            if(data.equals(curr.data)){
                break;
            }
            //遍历下一个节点
            curr = curr.next;
        }
        //2.删除指定节点
        unLink(curr);
    }

    @Override
    public boolean isEmpty() {
        return size == 0 || head.next == head;
    }

    @Override
    public int size() {
        return size;
    }


    /**
     * 添加新节点到链表尾部(FIFO 队列):
     * -----------------------------------------------------
     * head⇄node1⇄node2⇄head.prev(老链表尾部)⇄newNode(新链表尾部)
     *
     * @param data
     */
    private void linkLast(E data) {
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

    /**
     * 添加新节点到链表头部(LIFO 队列):
     * -newNode(新头部)--------------------------------
     *     ↓
     * head⇄head.next(老链表头部)⇄node2⇄head.prev(链表尾部)
     *
     * @param data
     */
    private void linkFirst(E data) {
        //新节点加入到head和老的首节点head.next之间
        //所以newNode.prev=head,newNode.next=head.next
        Node<E> newNode = new Node<E>(head, data, head.next);
        //链表首部节点前驱指向新节点
        head.next.prev = newNode;
        //head节点的next节点指向新节点
        head.next = newNode;
        //链表大小+1
        size++;
    }


    /**
     * 删除链表第一个元素
     * head⇄first(x)⇄node2⇄head.prev(链表尾部)
     * @return
     */
    private E unLinkFirst() {
        //获取链表第一个节点
        Node first = head.next;
        //删除first节点
        return unLink(first);

    }

    /**
     * 删除链表最后一个元素
     * head⇄first⇄node2⇄node3⇄node4⇄last(x)
     * @return
     */
    private E unLinkLast() {
        //获取链表最后一个节点
        Node last = head.prev;
        //删除Last节点
        return unLink(last);
    }

    /**
     * 删除指定节点
     * head⇄first⇄node2⇄node3(x)⇄node4⇄last
     * @param node
     */
    private E unLink(Node<E> node) {
        if (isEmpty()) {
            return null;
        }
        E data=node.data;
        //node节点的prev节点next->node节点的next
        node.prev.next = node.next;
        //node节点的next节点的prev->node节点的prev节点
        node.next.prev = node.prev;
        node=null;
        size--;
        return data;
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
        private Node<E> prev;
        //后一个节点引用
        private Node<E> next;

        public Node(Node prev, E data, Node next) {
            this.prev = prev;
            this.next = next;
            this.data = data;
        }
    }

    public static void main(String[] args) {
        //队列链表实现
        DoubleCircleLinkList<Object> queueLinkedList = new DoubleCircleLinkList<Object>();
        queueLinkedList.add(1);
        queueLinkedList.add(2);
        queueLinkedList.add(3);
        queueLinkedList.add(4);
        queueLinkedList.add(5);
        queueLinkedList.add(6);
        queueLinkedList.add(7);
        System.out.println("Double circle linked queue :" + queueLinkedList);

        //栈链表实现
        DoubleCircleLinkList<Object> stackLinkedList = new DoubleCircleLinkList<Object>();
        stackLinkedList.addFirst(1);
        stackLinkedList.addFirst(2);
        stackLinkedList.addFirst(3);
        stackLinkedList.addFirst(4);
        stackLinkedList.addFirst(5);
        stackLinkedList.addFirst(6);
        stackLinkedList.addFirst(7);
        System.out.println("Double circle linked stack :" + stackLinkedList);

        //删除first节点
        queueLinkedList.remove();
        System.out.println("Double circle removed first linklist :" + queueLinkedList);

        //删除last节点
        queueLinkedList.removeLast();
        System.out.println("Double circle removed last linklist :" + queueLinkedList);


        //删除data=4的节点
        queueLinkedList.remove(4);
        System.out.println("Double circle removed 4-data linklist :" + queueLinkedList);





    }
}
