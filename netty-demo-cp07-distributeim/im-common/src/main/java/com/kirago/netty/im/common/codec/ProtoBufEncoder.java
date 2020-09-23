package com.kirago.netty.im.common.codec;

import com.kirago.netty.im.common.protocol.Proto3Msg;
import com.kirago.netty.im.common.constants.ProtoInstant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtoBufEncoder extends MessageToByteEncoder<Proto3Msg.ProtoMsg.Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx,
                          Proto3Msg.ProtoMsg.Message msg, ByteBuf out)
            throws Exception {
        out.writeShort(ProtoInstant.MAGIC_CODE);
        out.writeShort(ProtoInstant.VERSION_CODE);

        byte[] bytes = msg.toByteArray();// 将对象转换为byte

        // 加密消息体
    /*ThreeDES des = channel.channel().attr(Constants.ENCRYPT).get();
    byte[] encryptByte = des.encrypt(bytes);*/
        int length = bytes.length;// 读取消息的长度


        // 先将消息长度写入，也就是消息头
        out.writeInt(length);
        // 消息体中包含我们要发送的数据
        out.writeBytes(msg.toByteArray());

/*        log.debug("send "
                + "[remote ip:" + ctx.channel().remoteAddress()
                + "][total length:" + length
                + "][bare length:" + msg.getSerializedSize() + "]");*/

    }
}
