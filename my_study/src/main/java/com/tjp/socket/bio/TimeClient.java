package com.tjp.socket.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 客户端:专门向服务端发送"query time order"指令,获取当前时间
 * Created by tujinpeng on 2017/4/2.
 */
public class TimeClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 8080;
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            //1.客户端建立socket连接
            socket = new Socket("127.0.0.1", port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //2.向服务器端发送指令
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("query time order");
            System.out.println("client send msg to server , msg : query time order");
            //3.等待服务器断返回
            //readLine时阻塞函数,只有遇到"/r"、"/n"、"/r/n"才会返回
            String response = in.readLine();
            System.out.println("client recive msg , msg : " + response);

        } finally {
            //4.关闭输入输出流,socket
            if (in != null) {
                in.close();
                in = null;
            }
            if (out != null) {
                out.close();
                out = null;
            }
            if (socket != null) {
                socket.close();
                socket = null;
            }
        }


    }
}
