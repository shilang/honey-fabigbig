package com.honeywell.greenhouse.fbb.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.honeywell.greenhouse.fbb.comm.BaseObject;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GenerateContractRes extends BaseObject {

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("result")
    private String result;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("download_url")
    private String downloadUrl;

    @JsonProperty("viewpdf_url")
    private String viewpdfUrl;

    @JsonIgnore
    private Integer templateType;

    @JsonIgnore
    private boolean respOk; // default false
}
