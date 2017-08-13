package com.tjp.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by tujinpeng on 2017/4/25.
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

        //申明服务器线程
        MultiplexerTimeServer multiplexerTimeServer = new MultiplexerTimeServer(port);
        new Thread(multiplexerTimeServer, "nio-multiplexerTimeServer-thread-001").start();
    }

    /**
     * 服务器线程
     */
    static class MultiplexerTimeServer implements Runnable {

        /**
         * 多路复用器
         */
        private Selector selector;

        private ServerSocketChannel serverChannel;

        private volatile boolean stop;

        /**
         * 初始化多路复用器,绑定监听端口
         *
         * @param port
         */
        public MultiplexerTimeServer(int port) {
            try {
                selector = Selector.open();
                serverChannel = ServerSocketChannel.open();
                serverChannel.configureBlocking(false);
                serverChannel.socket().bind(new InetSocketAddress(port), 1024);
                //在selector注册accept时件
                serverChannel.register(selector, SelectionKey.OP_ACCEPT);
                System.out.println("the time server is start , port : " + port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void run() {
            try {
                while (!stop) {
                    //每个1s,扫描一次selector
                    selector.select(1000);
                    //获取selector上就绪的channel
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    SelectionKey key = null;
                    while (iterator.hasNext()) {
                        key = iterator.next();
                        iterator.remove();
                        try {
                            //处理每一个channel
                            handelChannel(key);
                        } catch (Exception e) {
                            if (key != null) {
                                key.cancel();
                                if (key.channel() != null) {
                                    key.channel().close();
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //关闭selector多路复用器
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        /**
         * 处理每一个socket channel
         *
         * @param key
         */
        private void handelChannel(SelectionKey key) throws IOException {
            if (key.isValid()) {
                //处理新的请求
                if (key.isAcceptable()) {
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                }
                //处理读请求
                if (key.isReadable()) {
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int readBytes = sc.read(readBuffer);
                    if (readBytes > 0) {
                        //读到的字节>0，正常读取
                        readBuffer.flip();//读取之前要调用
                        byte[] bytes = new byte[readBuffer.remaining()];
                        readBuffer.get(bytes);
                        String body = new String(bytes, "UTF-8");
                        System.out.println("server recive msg , msg : " + body);
                        String currentTime = "query time order".equalsIgnoreCase(body) ? new Date().toString() : "bad order";
                        doWrite(sc, currentTime);
                    } else if (readBytes < 0) {
                        //读到的字节<0，channel关闭
                        key.cancel();
                        sc.close();
                    } else {
                        //读到的字节=0，channel里没有可读取的，属于正常情况

                    }
                }
            }
        }

        /**
         * 将数据写回SocketChannel
         *
         * @param sc
         * @param response
         */
        private void doWrite(SocketChannel sc, String response) throws IOException {
            if (response != null && response.length() > 0) {
                byte[] bytes = response.getBytes();
                ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
                writeBuffer.put(bytes);
                //注意，ByteBuffer.put以后一定要执行flip()
                writeBuffer.flip();
                sc.write(writeBuffer);
            }
        }

        public void setStop(boolean stop) {
            this.stop = stop;
        }
    }
}
