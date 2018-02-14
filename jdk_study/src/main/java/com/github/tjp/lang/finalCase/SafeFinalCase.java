package com.github.tjp.lang.finalCase;

public class SafeFinalCase {

    private static SafeMessagePublisher safeMessagePublisher;

    public static void write() {
        if (safeMessagePublisher == null) {
            safeMessagePublisher = new SafeMessagePublisher();
        }
    }

    /*
     * <pre>
     * 为了避免在并发的情况下 读到为初始化完全的messageMap 可以采用final修饰的messageMap
     *
     * final修饰符:保证safeMessagePublisher【对象的安全发布】,防止【引用溢出】:
     *
     * 写final域的重排序规则:
     *   在引用变量被其他线程可见之前,引用变量持有的final域在构造函数中都初始化完毕了
     *   (1)禁止发final域的写(如果final域是对象,还包括final对象的成员域) 重排序到构造函数之外:
     *        意味着safeMessagePublisher引用返回时,messageMap数据已经初始化完毕,不存在当safeMessagePublisher引用不为空,map数据不全的问题
     *   (2)实现原理:编译器在编译期,会将final域的写之后,构造函数return之前,插入一个StoreStore屏障,让构造器等待final写之后才返回(类似并发的CyslicBarrier)
     *
     * 读final域的重排序规则:
     *   (1)保证读对象引用和读对象final域的先后顺序,禁止重排序(有个alpha处理器会进行重排序)
     *   (2)实现原理:编译器在编译器,会在读final域的前面插入一个LoadLLoad屏障,保证读final域前,要先读对象引用
     *
     * </pre>
     */
    public static void read() {
        SafeMessagePublisher safeMessagePublisher = SafeFinalCase.safeMessagePublisher;
        if (safeMessagePublisher != null) {
            System.out.println(safeMessagePublisher.getMessageMap().size());
        }
    }


    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            Thread writeThread = new Thread(new Writer());
            writeThread.start();

            Thread readThread = new Thread(new Reader());
            readThread.start();
        }
    }

    private static class Reader implements Runnable {

        @Override
        public void run() {
            SafeFinalCase.read();
        }
    }

    private static class Writer implements Runnable {

        @Override
        public void run() {
            SafeFinalCase.write();
        }
    }

}
