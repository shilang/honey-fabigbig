package com.honeywell.greenhouse.fbb.nop;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import com.fadada.sdk.client.common.FddClient;
import com.fadada.sdk.util.crypt.FddEncryptTool;
import com.fadada.sdk.util.http.HttpsUtil;

/**
 * 印章接口
 */
public class SignatureClient extends FddClient {

    public SignatureClient(String appId, String secret, String version, String url) {
        super(appId, secret, version, url);
    }

    private String getURLOfQuerySignature() {
        return super.getUrl() + "query_signature" + API_SUFFIX;
    }

    private String getURLOfReplaceSignature() {
        return super.getUrl() + "replace_signature" + API_SUFFIX;
    }

    private String getURLOfRemoveSignature() {
        return super.getUrl() + "remove_signature" + API_SUFFIX;
    }

    private String getURLOfDefaultSignature() {
        return super.getUrl() + "default_signature" + API_SUFFIX;
    }

    public String invokeQuerySignature(String customerId, String signatureId) {
        List<NameValuePair> params = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        try {
            //请求接口名
            sb.append(this.getURLOfQuerySignature());
            String sort = customerId + signatureId;
            String timeStamp = HttpsUtil.getTimeStamp();
            String sha1 = FddEncryptTool.sha1(super.getAppId() + FddEncryptTool.md5Digest(timeStamp) + FddEncryptTool.sha1(super.getSecret() + sort));
            String msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));

            sb.append("?app_id=").append(super.getAppId());
            sb.append("&v=").append(super.getVersion());
            sb.append("&timestamp=").append(timeStamp);
            sb.append("&msg_digest=").append(msgDigest);

            sb.append("&customer_id=").append(customerId);
            sb.append("&signature_id=").append(signatureId);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return HttpsUtil.doPost(sb.toString(), params);
    }

    public String invokeReplaceSignature(String customerId, String signatureId, String signatureImgBase64) {
        List<NameValuePair> params = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        try {
            //请求接口名
            sb.append(this.getURLOfReplaceSignature());
            String sort = customerId + signatureId + signatureImgBase64;
            String timeStamp = HttpsUtil.getTimeStamp();
            String sha1 = FddEncryptTool.sha1(super.getAppId() + FddEncryptTool.md5Digest(timeStamp) + FddEncryptTool.sha1(super.getSecret() + sort));
            String msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));

            sb.append("?app_id=").append(super.getAppId());
            sb.append("&v=").append(super.getVersion());
            sb.append("&timestamp=").append(timeStamp);
            sb.append("&msg_digest=").append(msgDigest);

            sb.append("&customer_id=").append(customerId);
            sb.append("&signature_id=").append(signatureId);
            sb.append("&signature_img_base64=").append(signatureImgBase64);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return HttpsUtil.doPost(sb.toString(), params);
    }

    public String invokeRemoveSignature(String customerId, String signatureId) {
        List<NameValuePair> params = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        try {
            //请求接口名
            sb.append(this.getURLOfRemoveSignature());
            String sort = customerId + signatureId;
            String timeStamp = HttpsUtil.getTimeStamp();
            String sha1 = FddEncryptTool.sha1(super.getAppId() + FddEncryptTool.md5Digest(timeStamp) + FddEncryptTool.sha1(super.getSecret() + sort));
            String msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));

            sb.append("?app_id=").append(super.getAppId());
            sb.append("&v=").append(super.getVersion());
            sb.append("&timestamp=").append(timeStamp);
            sb.append("&msg_digest=").append(msgDigest);

            sb.append("&customer_id=").append(customerId);
            sb.append("&signature_id=").append(signatureId);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return HttpsUtil.doPost(sb.toString(), params);
    }

    public String invokeDefaultSignature(String customerId, String signatureId) {
        List<NameValuePair> params = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        try {
            //请求接口名
            sb.append(this.getURLOfDefaultSignature());
            String sort = customerId + signatureId;
            String timeStamp = HttpsUtil.getTimeStamp();
            String sha1 = FddEncryptTool.sha1(super.getAppId() + FddEncryptTool.md5Digest(timeStamp) + FddEncryptTool.sha1(super.getSecret() + sort));
            String msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));

            sb.append("?app_id=").append(super.getAppId());
            sb.append("&v=").append(super.getVersion());
            sb.append("&timestamp=").append(timeStamp);
            sb.append("&msg_digest=").append(msgDigest);

            sb.append("&customer_id=").append(customerId);
            sb.append("&signature_id=").append(signatureId);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return HttpsUtil.doPost(sb.toString(), params);
    }
}
