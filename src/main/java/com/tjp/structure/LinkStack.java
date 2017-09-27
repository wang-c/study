package com.tjp.structure;

/**
 * 链表实现的栈(LIFO) 一端进出是栈
 * Created by tujinpeng on 2017/9/27.
 */
public class LinkStack<E> implements Stack<E> {

    private LinkList<E> linkList = new SingleLinkList<E>();

    @Override
    public E push(E data) {
        linkList.addFirst(data);
        return data;
    }

    @Override
    public E pop() {
        return linkList.removeFirst();
    }

    @Override
    public E peek() {
        return linkList.getFirst();
    }

    @Override
    public int size() {
        return linkList.size();
    }

    @Override
    public boolean isEmpty() {
        return linkList.isEmpty();
    }

    @Override
    public String toString() {
        return linkList.toString();
    }

    public static void main(String[] args) {
        Stack stack = new LinkStack();
        //入栈
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);
        System.out.println("stack push : " + stack);

        //出栈
        Object head = stack.pop();
        System.out.println("pop head : " + head);
        System.out.println("after pop , stack : " + stack);

        //查看栈顶元素
        head = stack.peek();
        System.out.println("peek head : " + head);
        System.out.println("after peek , stack  : " + stack);

    }
}
