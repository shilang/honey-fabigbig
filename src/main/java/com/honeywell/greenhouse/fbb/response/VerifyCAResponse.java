package com.honeywell.greenhouse.fbb.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.honeywell.greenhouse.fbb.comm.BaseObject;

public class VerifyCAResponse extends BaseObject {

    @JsonProperty("code")
    private String code;

    @JsonProperty("customer_id")
    private String customerId;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("result")
    private String result;

    @JsonIgnore
    private boolean respOk; // default false

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public boolean isRespOk() {
        return respOk;
    }


    public void setRespOk(boolean respOk) {
        this.respOk = respOk;
    }

}
