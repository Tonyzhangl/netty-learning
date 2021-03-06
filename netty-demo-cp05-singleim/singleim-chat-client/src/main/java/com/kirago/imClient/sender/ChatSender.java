package com.kirago.imClient.sender;


import com.kirago.imClient.protoBuilder.ChatMsgBuilder;
import com.kirago.imCommon.common.bean.ChatMsg;
import com.kirago.imCommon.common.bean.msg.ProtoMsg3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("ChatSender")
public class ChatSender extends BaseSender {

    public void sendChatMsg(String touid, String content) {
        log.info("发送消息 startConnectServer");
        ChatMsg chatMsg = new ChatMsg(getUser());
        chatMsg.setContent(content);
        chatMsg.setMsgType(ChatMsg.MSGTYPE.TEXT);
        chatMsg.setTo(touid);
        chatMsg.setMsgId(System.currentTimeMillis());
        ProtoMsg3.Message message =
                ChatMsgBuilder.buildChatMsg(chatMsg, getUser(), getSession());

        super.sendMsg(message);
    }

    @Override
    protected void sendSucced(ProtoMsg3.Message message) {
        log.info("发送成功:" + message.getMessageRequest().getContent());
    }



    @Override
    protected void sendfailed(ProtoMsg3.Message message) {
        log.info("发送失败:" + message.getMessageRequest().getContent());
    }
}

