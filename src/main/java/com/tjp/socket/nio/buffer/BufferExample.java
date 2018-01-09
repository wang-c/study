package com.tjp.socket.nio.buffer;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * @author tujinpeng
 * @version V1.0
 * @date 2018/1/7 下午1:42
 */
public class BufferExample {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        //1.填充【写模式】 [pos=6 lim=10 cap=10]
        buffer.put((byte) 'H').put((byte) 'e')
                .put((byte) 'l').put((byte) 'l')
                .put((byte) 'o').put((byte) 'w');
        System.out.println("remaining: " + buffer.remaining());
        System.out.println(buffer.toString());

        //2.翻转【切换到读模式】 [pos=0 lim=6 cap=10]
        buffer.flip();
        System.out.println(buffer.toString());

        //注意千万不要翻转两次?   [pos=0 lim=0 cap=10]
//        buffer.flip();
//        System.out.println(buffer.toString());

        //3.释放
        byte[] byteArr = new byte[10];
        int index = 0;
        while (buffer.hasRemaining()) {//判断buffer中元素有没有达到上限
            byteArr[index] = buffer.get();
            index++;

        }
        System.out.println(buffer);


        //4.rewind 重头开始读
        buffer.rewind();
        //先读取第一个字节 还剩"ellow"
        buffer.get();

        //5.压缩,释放读取过的元素 同时会切换到写模式 剩余"ellow"
        buffer.compact();
        System.out.println(buffer);

        //压缩过后,要去读取buffer,之前要翻转flip
        buffer.flip();
        System.out.println(buffer.get());//读取到'e'


        //6.mark()标记位置2 reset()从mark位置开始读
        buffer.position(2).mark().position(4).reset();
        System.out.println(buffer);

        //7.比较两个buffer (1)类型相等 (2)剩余元素相等
        ByteBuffer bufferA = ByteBuffer.allocate(10);
        ByteBuffer bufferB = ByteBuffer.allocate(10);
        //比较两个buffer相等 是否要先切换到读模式?????
        bufferA.put((byte) 'b');
        bufferB.put((byte) 'a');
        bufferA.flip();
        bufferB.flip();
        System.out.println("bufferA==bufferB? : " + bufferA.equals(bufferB));
    }


    private static String[] strs = new String[]{
            "hello world",
            "my name is tujinpeng",
            "i is too shuai"
    };

    private int index = 0;

    /**
     * buffer填充释放的例子
     */
    @Test
    public void bufferFillDrain() {
        CharBuffer buffer = CharBuffer.allocate(100);
        while (fillBuffer(buffer)) {
            //切换到读模式
            buffer.flip();
            drainBuffer(buffer);
            //读取完buffer,清空buffer
            buffer.clear();
        }
    }

    //读取字符串
    private void drainBuffer(CharBuffer buffer) {
        while (buffer.hasRemaining()) {
            System.out.print(buffer.get());
        }
        System.out.println();
    }

    //向buffer写字符串
    private boolean fillBuffer(CharBuffer buffer) {
        if (index >= strs.length) {
            return false;
        }
        String str = strs[index++];
        for (int i = 0; i < str.length(); i++) {
            buffer.put(str.charAt(i));
        }
        return true;
    }

    /**
     * 复制缓存区
     */
    @Test
    public void copyBuffer() {
        CharBuffer buffer = CharBuffer.allocate(10);
        buffer.put("hello");
        System.out.println(buffer);

        /*
         *1.duplicate()复制的缓存区:
         *(1)拥有相同的容量,共享同一个数据元素
         *(2)但是拥有不同的位置,上界以及标记
         */
        CharBuffer copyBuffer = buffer.duplicate();
        copyBuffer.put('!');
        System.out.println(copyBuffer);


        /*
         * 2.asReadOnlyBuffer() 只读缓存区 共享元数据 但是调用put会抛异常ReadOnlyBufferException
         */
        CharBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
//        readOnlyBuffer.put((byte) 'r');

        /*
         *3. slice()分割缓存区 创建一个从2~4的分割缓存区
         */
        buffer.position(2).limit(4);
        CharBuffer sliceBuffer = buffer.slice();//[pos=0 lim=2 cap=2 offset=2]
        System.out.println(sliceBuffer);

    }
}
