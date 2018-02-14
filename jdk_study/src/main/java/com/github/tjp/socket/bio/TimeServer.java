package com.github.tjp.socket.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * 服务器端:专门给接受客户端"query time order"指令,返回当前时间
 * Created by tujinpeng on 2017/4/2.
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket server = null;
        try {

            //1.创建server,监听8080端口
            server = new ServerSocket(port);
            TimeServerHandleExecutePool pool = new TimeServerHandleExecutePool(50, 10000);
            System.out.println("the server is start in port : " + port);
            //2.不断accept,接受客户端连接,作处理
            while (true) {
                Socket socket = server.accept();
                //无限制的创建线程,会导致系统无线程可用儿崩溃,要用线程池管理调度线程
                //new Thread(new TimeServerHandle(socket), "bio-server-thread-01").start();
                //因为线程池大小以及任务队列大小都是有限制的,不管客户端并发量多大,都不会造成线程数过于膨胀或者内存溢出
                //pool.execute(new TimeServerHandle(socket));
            }
        } finally {
            //3.关闭server
            if (server != null) {
                server.close();
                System.out.println("the server is close : ");
                server = null;
            }
        }
    }

    /**
     * 处理客户端业务类
     */
    private static class TimeServerHandle implements Runnable {

        private Socket socket;

        public TimeServerHandle(Socket socket) {
            this.socket = socket;
        }

        /**
         * 读取每个客户端的连接socket的输入流,返回输出流
         */
        public void run() {
            //输入流
            BufferedReader in = null;
            //输出流
            PrintWriter out = null;
            try {
                in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                out = new PrintWriter(this.socket.getOutputStream(), true);
                //读取客户端的输入流数据
                String body = null;
                String currentTime = null;
                while (true) {
                    body = in.readLine();
                    if (body == null) {
                        break;
                    }
                    System.out.println("server recive msg , msg : " + body);
                    currentTime = "query time order".equalsIgnoreCase(body) ? new Date().toString() : "bad order";
                    //将服务器结果返回给客户端,结束
                    out.println(currentTime);//注意这里一定要是println,换行写,否则client的readLine操作会一直阻塞
                }
            } catch (Exception e) {
                //关闭输入流,GC回收
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            //关闭输出流,GC回收
            if (out != null) {
                out.close();
                out = null;
            }
            //关闭socket,GC回收
            if (socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
