package com.honeywell.greenhouse.fbb.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


import com.honeywell.greenhouse.fbb.comm.BaseObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "greenhouse.fadada")
public class FddProperties extends BaseObject {

    private boolean enabled;

    private String appId;

    private String secret;

    private String version;

    private String verifyCaUrl;

    private String notifyUrl;

    private String realnameAuthenticationCallbackUrl;

}
