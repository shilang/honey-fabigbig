package com.honeywell.greenhouse.fbb.nop;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import com.fadada.sdk.client.common.FddClient;
import com.fadada.sdk.util.crypt.FddEncryptTool;
import com.fadada.sdk.util.http.HttpsUtil;

/**
 * <h3>概要:</h3> 合同拒签接口 <br>
 * <h3>功能:</h3>
 * <ol>
 * <li>企业授权/取消授权接口</li>
 * </ol>
 * <h3>履历:</h3>
 * <ol>
 * <li>2017年3月14日[liuyz] 新建</li>
 * </ol>
 */
public class RejectSignContract extends FddClient {

    public RejectSignContract(String appId, String secret, String version, String url) {
        super(appId, secret, version, url);
    }


    private String getURLOfRejectSign() {
        return super.getUrl() + "contract_reject_sign.api";
    }


    public String invoke(String transactionId, String contractId, String customerId, String rejectReason) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        StringBuffer sb = new StringBuffer();
        try {
            //请求接口名
            sb.append(this.getURLOfRejectSign());
            String sort = contractId + customerId + rejectReason + transactionId;
            String timeStamp = HttpsUtil.getTimeStamp();
            String sha1 = FddEncryptTool.sha1(super.getAppId() + FddEncryptTool.md5Digest(timeStamp) + FddEncryptTool.sha1(super.getSecret() + sort));
            String msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));

            sb.append("?app_id=").append(super.getAppId());
            sb.append("&v=").append(super.getVersion());
            sb.append("&timestamp=").append(timeStamp);
            sb.append("&msg_digest=").append(msgDigest);

            sb.append("&contract_id=").append(contractId);
            sb.append("&transaction_id=").append(transactionId);
            sb.append("&customer_id=").append(customerId);
            sb.append("&reject_reason=").append(URLEncoder.encode(rejectReason, HttpsUtil.charset));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return HttpsUtil.doPost(sb.toString(), params);
    }
}
