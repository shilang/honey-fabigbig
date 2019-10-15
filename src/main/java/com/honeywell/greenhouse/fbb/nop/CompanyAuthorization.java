package com.honeywell.greenhouse.fbb.nop;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.fadada.sdk.client.common.FddClient;
import com.fadada.sdk.util.crypt.FddEncryptTool;
import com.fadada.sdk.util.http.HttpsUtil;

/**
 * <h3>概要:</h3> 企业授权/取消授权接口 <br>
 * <h3>功能:</h3>
 * <ol>
 * <li>企业授权/取消授权接口</li>
 * </ol>
 * <h3>履历:</h3>
 * <ol>
 * <li>2017年3月14日[liuyz] 新建</li>
 * </ol>
 */
public class CompanyAuthorization extends FddClient {

    public CompanyAuthorization(String appId, String secret, String version, String url) {
        super(appId, secret, version, url);
    }

    private String getURLOfCompanyAuthorization() {
        return super.getUrl() + "authorization.api";
    }

    public String getURLOfCompanyAuthorizationWithSignature() {
        return super.getUrl() + "authorize_signature" + API_SUFFIX;
    }

    public String invoke(String companyId, String personId, String operateType, String signatureId) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            String timeStamp = HttpsUtil.getTimeStamp();
            String msgDigest = "";
            String sha1 = FddEncryptTool.sha1(super.getAppId() + FddEncryptTool.md5Digest(timeStamp) +
                    FddEncryptTool.sha1(super.getSecret() + companyId + operateType + personId + signatureId));
            msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes())).trim();
            params.add(new BasicNameValuePair("msg_digest", msgDigest));
            params.add(new BasicNameValuePair("app_id", super.getAppId()));
            params.add(new BasicNameValuePair("timestamp", timeStamp));
            params.add(new BasicNameValuePair("v", super.getVersion()));
            params.add(new BasicNameValuePair("company_id", companyId));
            params.add(new BasicNameValuePair("person_id", personId));
            params.add(new BasicNameValuePair("operate_type", operateType));
            params.add(new BasicNameValuePair("signature_id", signatureId));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return HttpsUtil.doPost(this.getURLOfCompanyAuthorizationWithSignature(), params);
    }

    /**
     * 企业授权/取消授权接口
     *
     * @param companyId   企业客户编号
     * @param personId    个人客户编号
     * @param operateType 操作类型 0：取消授权， 1：授权
     * @author: liuyz
     * @date: 2017年3月14日
     */
    public String invoke(String companyId, String personId, String operateType) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        try {
            String timeStamp = HttpsUtil.getTimeStamp();
            String msgDigest = "";
            // Base64(SHA1(app_id+md5(timestamp)+SHA1(app_secret+company_id + person_id + operate_type)))
            String sha1 = FddEncryptTool.sha1(super.getAppId() + FddEncryptTool.md5Digest(timeStamp) + FddEncryptTool.sha1(super.getSecret() + companyId + personId + operateType));
            msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes())).trim();
            params.add(new BasicNameValuePair("msg_digest", msgDigest));
            params.add(new BasicNameValuePair("app_id", super.getAppId()));
            params.add(new BasicNameValuePair("timestamp", timeStamp));
            params.add(new BasicNameValuePair("v", super.getVersion()));
            params.add(new BasicNameValuePair("company_id", companyId));
            params.add(new BasicNameValuePair("person_id", personId));
            params.add(new BasicNameValuePair("operate_type", operateType));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return HttpsUtil.doPost(this.getURLOfCompanyAuthorization(), params);
    }
}
