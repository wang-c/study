package com.github.tjp.socket.netty.buffer;

import java.nio.ByteBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: tujinpeng
 * Date: 2017/5/25
 * Time: 17:57
 * Email:tujinpeng@lvmama.com
 */
public class BufferApi {
    public static void main(String[] args) {
        ByteBuffer buffer= ByteBuffer.allocate(88);
        String value="Netty权威指南";
        buffer.put(value.getBytes());

        buffer.flip();

        byte[] array=new byte[buffer.remaining()];
        buffer.get(array);
        String decodeStr=new String(array);
        System.out.printf(decodeStr);


    }
}
