package com.honeywell.greenhouse.fbb.nop;

import com.fadada.sdk.client.request.ExtsignReq;

import lombok.Getter;
import lombok.Setter;

/**
 * 带印章的签署请求
 */
@Setter
@Getter
public class ExtSignWithSignatureReq extends ExtsignReq {

    private String signature_id = "";
}
