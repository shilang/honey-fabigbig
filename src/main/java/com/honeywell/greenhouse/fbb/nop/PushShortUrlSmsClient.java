package com.honeywell.greenhouse.fbb.nop;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.fadada.sdk.client.common.FddClient;
import com.fadada.sdk.util.crypt.FddEncryptTool;
import com.fadada.sdk.util.http.HttpsUtil;


/**
 * 自定义短信发送短链接口
 */
public class PushShortUrlSmsClient extends FddClient {

    public PushShortUrlSmsClient(String appId, String secret, String version, String url) {
        super(appId, secret, version, url);
    }

    private String getURLOfPushShortSms() {
        return super.getUrl() + "push_short_url_sms" + API_SUFFIX;
    }


    public String invokePushSms(PushShortSmsReq req) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            String timeStamp = HttpsUtil.getTimeStamp();
            String msgDigest = "";
            params.add(new BasicNameValuePair("app_id", super.getAppId()));
            params.add(new BasicNameValuePair("timestamp", timeStamp));
            params.add(new BasicNameValuePair("v", super.getVersion()));
            params.add(new BasicNameValuePair("source_url", req.getSourceUrl()));
            params.add(new BasicNameValuePair("expire_time", req.getExpireTime()));
            String id_mobile = FddEncryptTool.encrypt( req.getMobile(), super.getSecret());
            params.add(new BasicNameValuePair("mobile", id_mobile));
            params.add(new BasicNameValuePair("message_type", req.getMessageType()));
            params.add(new BasicNameValuePair("message_content", req.getMessageContent()));
            params.add(new BasicNameValuePair("sms_template_type", req.getSmsTemplateType()));


            // Base64(SHA1(app_id+md5(transaction_id+timestamp)+SHA1(app_secret+customer_id)))
            String sha1 = FddEncryptTool.sha1(super.getAppId() + FddEncryptTool.md5Digest( timeStamp) + FddEncryptTool.sha1(super.getSecret() + req.getExpireTime() + req.getMessageContent() +
                    req.getMessageType() + id_mobile + req.getSmsTemplateType() + req.getSourceUrl()));
            msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes())).trim();
            params.add(new BasicNameValuePair("msg_digest", msgDigest));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return HttpsUtil.doPost(this.getURLOfPushShortSms(), params);
    }
}
