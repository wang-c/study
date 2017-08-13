package com.tjp.socket.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by IntelliJ IDEA.
 * User: tujinpeng
 * Date: 2017/4/30
 * Time: 18:11
 * Email:tujinpeng@lvmama.com
 */
public class TimeClient {

    public void connect(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            //绑定端口，同步等待成功
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            //等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅退出，释放线程池资源
            group.shutdownGracefully();
        }
    }

    private class TimeClientHandler extends ChannelInboundHandlerAdapter {

        private final ByteBuf firstMessage;

        public TimeClientHandler() {
            byte[] req = "query time order".getBytes();
            firstMessage = Unpooled.buffer(req.length);
            firstMessage.writeBytes(req);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(firstMessage);
            System.out.println("client send msg to server , msg : query time order");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf buf= (ByteBuf) msg;
            byte[] rep=new byte[buf.readableBytes()];
            buf.readBytes(rep);
            String body=new String(rep,"UTF-8");
            System.out.println("client recive msg , msg : " + body);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }


    }


    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        new TimeClient().connect("127.0.0.1", port);
    }
}
