package com.github.tjp.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: tujinpeng
 * Date: 2017/4/30
 * Time: 13:48
 * Email:tujinpeng@lvmama.com
 */
public class TimeClient {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

        //申明服务器线程
        new Thread(new TimeClientHandle("127.0.0.1", port), "nio-timeClient-thread-001").start();
    }

    static class TimeClientHandle implements Runnable {
        private String host;
        private int port;
        private Selector selector;
        private SocketChannel socketChannel;
        private boolean stop;

        /**
         * 初始化多路复用器selector,客户端socketChannel
         *
         * @param host
         * @param port
         */
        private TimeClientHandle(String host, int port) {
            this.host = host;
            this.port = port;
            try {
                selector = Selector.open();
                socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void run() {
            //与服务器端建立连接
            try {
                doConnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //循环多路复用器
            while (!stop) {
                try {
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
                            handelSocketChannel(key);
                        } catch (Exception e) {
                            if (key != null) {
                                key.cancel();
                                if (key.channel() != null) {
                                    key.channel().close();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

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

        private void doConnect() throws IOException {
            //若远程服务器连接成功，将读事件注册到多路复用器上，发送请求消息
            if (socketChannel.connect(new InetSocketAddress(host, port))) {
                socketChannel.register(selector, SelectionKey.OP_READ);
                doWrite(socketChannel);
            } else {
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }

        }

        private void handelSocketChannel(SelectionKey key) throws IOException {
            if (key.isValid()) {
                SocketChannel sc = (SocketChannel) key.channel();
                //若是就绪的连接事件，判断是否连接成功，若成功发送写请求，向多路复用器注册读事件
                if (key.isConnectable()) {
                    if (sc.finishConnect()) {
                        sc.register(selector, SelectionKey.OP_READ);
                        doWrite(sc);
                    } else {
                        System.exit(1);
                    }
                }
                //若是就绪的读事件
                if (key.isReadable()) {
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int readBytes = sc.read(readBuffer);
                    if (readBytes > 0) {
                        //读到的字节>0，正常读取
                        readBuffer.flip();//读取之前要调用
                        byte[] bytes = new byte[readBuffer.remaining()];
                        readBuffer.get(bytes);
                        String body = new String(bytes, "UTF-8");
                        System.out.println("client recive msg , msg :  " + body);
                        this.stop = true;
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

        private void doWrite(SocketChannel socketChannel) throws IOException {
            byte[] req = "query time order".getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
            writeBuffer.put(req);
            writeBuffer.flip();
            socketChannel.write(writeBuffer);
            //模拟半包问题,tcp粘包拆包问题
            if (writeBuffer.hasRemaining()) {
                System.out.println("send order 2 server succeed");
            }
        }
    }
}
