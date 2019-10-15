package com.honeywell.greenhouse.fbb.nop;

import java.net.URLEncoder;

import com.fadada.sdk.client.common.FddClient;
import com.fadada.sdk.util.crypt.FddEncryptTool;
import com.fadada.sdk.util.http.HttpsUtil;


public class SignClient extends FddClient {

    public SignClient(String appId, String secret, String version, String url) {
        super(appId, secret, version, url);
    }

    public String invokeExtSign(ExtSignWithSignatureReq req) {
        String timeStamp = HttpsUtil.getTimeStamp();
        StringBuilder sb = new StringBuilder(this.getURLOfExtSign());
        try {
            String msgDigest;
            // Base64(SHA1(app_id+md5(transaction_id+timestamp)+SHA1(app_secret+ customer_id)))
            String sha1 = FddEncryptTool.sha1(super.getAppId()
                    + FddEncryptTool.md5Digest(req.getTransaction_id() + timeStamp)
                    + FddEncryptTool.sha1(super.getSecret() + req.getCustomer_id()));
            msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));

            sb.append("?app_id=").append(super.getAppId());
            sb.append("&v=").append(super.getVersion());
            sb.append("&timestamp=").append(timeStamp);

            sb.append("&transaction_id=").append(req.getTransaction_id());
            sb.append("&customer_id=").append(req.getCustomer_id());
            sb.append("&signature_id=").append(req.getSignature_id());
            sb.append("&outh_customer_id=").append(req.getOuth_customer_id());
            sb.append("&contract_id=").append(req.getContract_id());
            sb.append("&doc_title=").append(URLEncoder.encode(req.getDoc_title(), HttpsUtil.charset));
            sb.append("&keyword_strategy=").append(req.getKeyword_strategy());
            if (null != req.getSign_keyword()) {
                sb.append("&sign_keyword=").append(URLEncoder.encode(req.getSign_keyword(), HttpsUtil.charset));
            }
            sb.append("&return_url=").append(URLEncoder.encode(req.getReturn_url(), HttpsUtil.charset));
            sb.append("&notify_url=").append(URLEncoder.encode(req.getNotify_url(), HttpsUtil.charset));
            sb.append("&msg_digest=").append(msgDigest);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
