package com.kirago.netty.im.client.client;

import com.kirago.netty.im.client.ClientSender.ChatSender;
import com.kirago.netty.im.client.ClientSender.LoginSender;
import com.kirago.netty.im.client.clientHandler.ChatMsgHandler;
import com.kirago.netty.im.client.clientHandler.ExceptionHandler;
import com.kirago.netty.im.client.clientHandler.LoginResponseHandler;
import com.kirago.netty.im.common.codec.ProtobufDecoder;
import com.kirago.netty.im.common.codec.ProtobufEncoder;
import com.kirago.netty.im.common.entity.PT.UserPT;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Data
@Service("NettyClient")
public class NettyClient {
    // 服务器ip地址
    private String host;
    // 服务器端口
    private int port;


    @Autowired
    private ChatMsgHandler chatMsgHandler;

    @Autowired
    private LoginResponseHandler loginResponseHandler;


    @Autowired
    private ExceptionHandler exceptionHandler;


    private Channel channel;
    private ChatSender sender;
    private LoginSender loginSender;

    /**
     * 唯一标记
     */
    private boolean initFlag = true;
    private UserPT user;
    private GenericFutureListener<ChannelFuture> connectedListener;

    private Bootstrap b;
    private EventLoopGroup g;

    public NettyClient() {

        /**
         * 客户端的是Bootstrap，服务端的则是 ServerBootstrap。
         * 都是AbstractBootstrap的子类。
         **/

        /**
         * 通过nio方式来接收连接和处理连接
         */

        g = new NioEventLoopGroup();


    }

    /**
     * 重连
     */
    public void doConnect() {
        try {
            b = new Bootstrap();

            b.group(g);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.remoteAddress(host, port);

            // 设置通道初始化
            b.handler(
                    new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast("decoder", new ProtobufDecoder());
                            ch.pipeline().addLast("encoder", new ProtobufEncoder());
                            ch.pipeline().addLast("loginResponseHandler",loginResponseHandler);
                            ch.pipeline().addLast("chatMsgHandler",chatMsgHandler);
                            ch.pipeline().addLast("exceptionHandler",exceptionHandler);
                        }
                    }
            );
            log.info("客户端开始连接 [疯狂创客圈IM]");

            ChannelFuture f = b.connect();
            f.addListener(connectedListener);


            // 阻塞
            // f.channel().closeFuture().sync();

        } catch (Exception e) {
            log.info("客户端连接失败!" + e.getMessage());
        }
    }

    public void close() {
        g.shutdownGracefully();
    }


}
