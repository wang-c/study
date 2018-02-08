package com.tjp.jvm.gc;

/**
 * @author tujinpeng
 * @version V1.0
 * @date 2018/1/31 上午9:57
 */
public class FinalizeCase {

    private static Block holder = null;

    public static void main(String[] args) throws Exception {
        holder = new Block();
        holder = null;
        System.gc();
        //System.in.read();
    }

    static class Block {

        byte[] _2M = new byte[2 * 1024 * 1024];

        @Override
        protected void finalize() throws Throwable {
            System.out.println("invoke finalize");
        }
    }
}
