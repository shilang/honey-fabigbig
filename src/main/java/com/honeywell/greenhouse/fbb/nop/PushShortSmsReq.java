package com.honeywell.greenhouse.fbb.nop;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class PushShortSmsReq {

    private String sourceUrl;

    private String expireTime = "10080";

    private String mobile;

//    1 填充模板：调用方传code，云端将其填充模板生成短信内容并发送
//    2 自定义模板：调用方传message_content，云端将其发送给用户
    private String messageType = "1";

    private String messageContent = "";

//    1：签署时候推送短链 2：实名认证时候推送短链
    private String smsTemplateType = "1";
}
