package com.honeywell.greenhouse.fbb.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.honeywell.greenhouse.fbb.comm.BaseObject;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FddResponse extends BaseObject {

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("data")
    private Object data; // could be string or object or ...

    @JsonIgnore
    private boolean respOk; // default false

}
