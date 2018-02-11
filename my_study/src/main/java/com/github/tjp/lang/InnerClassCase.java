package com.github.tjp.lang;

/**
 * @author tujinpeng
 * @version V1.0
 * @date 2018/2/10 下午2:11
 */
public class InnerClassCase {

    public InnerClass getInner(final Boolean localVaribale) {

        //inner class
        /*
         * 当内部类访问外部类方法的局部变量时,要将变量申明成final类型 ?
         * (1)在外部类方法中构造内部类时,实际上是将外部类的引用、局部变量作为参数传入内部类的构造方法里
         * 这样内部类持有外部类引用和局部变量副本
         *
         * (2)考虑到封装的一致性,局部变量要申明成final
         *
         * (3)localVaribale局部变量的生命周期是当方法执行结束,局部变量引用就消亡,但是对象可能还存在,因为内部类copy了一个对象的引用
         *
         */
        InnerClass inner = new InnerClass() {
            @Override
            public void invokeLocalVaribale() {
                System.out.println(localVaribale);
            }
        };
        return inner;
    }

    public static void main(String[] args) {

        //返回内部类对象
        InnerClass innerObject = new InnerClassCase().getInner(true);

        /**
         * 这里内部类会有两个成员变量:
         * (1)localVaribale 局部变量
         * (2)this$0 外部类引用(方便访问外部类属性)
         */
        innerObject.invokeLocalVaribale();

    }

    private interface InnerClass {
        public void invokeLocalVaribale();
    }
}
