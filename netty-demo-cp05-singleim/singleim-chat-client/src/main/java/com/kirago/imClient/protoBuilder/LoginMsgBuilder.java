package com.kirago.imClient.protoBuilder;

import com.kirago.imClient.client.ClientSession;
import com.kirago.imCommon.common.bean.User;
import com.kirago.imCommon.common.bean.msg.ProtoMsg3;

public class LoginMsgBuilder extends BaseBuilder{
    
    private final User user;

    public LoginMsgBuilder(User user, ClientSession session) {
        super(ProtoMsg3.HeadType.LOGIN_REQUEST, session);
        this.user = user;
    }

    public ProtoMsg3.Message build() {
        ProtoMsg3.Message message = buildCommon(-1);
        ProtoMsg3.LoginRequest.Builder lb =
                ProtoMsg3.LoginRequest.newBuilder()
                        .setDeviceId(user.getDevId())
                        .setPlatform(user.getPlatform().ordinal())
                        .setToken(user.getToken())
                        .setUid(user.getUid());
        return message.toBuilder().setLoginRequest(lb).build();
    }

    public static ProtoMsg3.Message buildLoginMsg(
            User user, ClientSession session) {
        LoginMsgBuilder builder =
                new LoginMsgBuilder(user, session);
        return builder.build();

    }
}
