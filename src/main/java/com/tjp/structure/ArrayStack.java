package com.tjp.structure;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 * 数组实现的栈(LIFO)  一端进出是栈
 * Created by tujinpeng on 2017/9/27.
 */
public class ArrayStack<E> implements Stack<E> {

    private int size;

    private List<E> list = new ArrayList<E>();

    public ArrayStack() {
    }


    @Override
    public E push(E data) {
        //若数组容量大于栈大小,则直接添加到栈尾部的next位置
        if (list.size() > size) {
            list.set(size, data);
        } else {
            //若数组容量等于栈大小,则直接添加到数组中
            list.add(data);
        }
        size++;
        return data;
    }

    @Override
    public E pop() {
        //栈为空 出栈抛异常
        if (size == 0)
            throw new EmptyStackException();
        //设置数组最后一个元素为null 栈大小-1
        return list.set(--size, null);
    }

    @Override
    public E peek() {
        //栈为空 出栈抛异常
        if (size == 0)
            throw new EmptyStackException();
        //查看数组最后一个元素(站头)
        return list.get(size - 1);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sb.append("->");
            }
            sb.append(list.get(i));
        }
        sb.append('}');
        return sb.toString();
    }

    public static void main(String[] args) {
        Stack stack = new ArrayStack();
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
